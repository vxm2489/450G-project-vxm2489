package edu.louisiana.cacs.csce450GProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.SwingUtilities;

public class Parser{
	
	SLRTable slrTable; //Action table
	Stack<PState> stack = new Stack<PState>(); //DFA
	Stack<Node> pTStack = new Stack<Node>(); //Parse tree stack
	ArrayList<TableRow> table = new ArrayList<TableRow>(); //A Table to hold all the data
	String pString;

	public Parser(String fileName) {
				
		File file = new File(fileName);		
		FileInputStream fis;
		
		try
		{
			fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();

			pString = new String(data, "UTF-8").trim();			
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(fileName+" doesn't exist");
			System.exit(0);
		} 
		catch (UnsupportedEncodingException e) 
		{
			System.out.println("Problem reading input from "+fileName);
			System.exit(0);
		} 
		catch (IOException e) 
		{
			System.out.println("Problem reading input from "+fileName);
			System.exit(0);
		}		
		
		if(pString.charAt(pString.length()-1)!='$')
		{
			System.out.println("Invalid input format");
			System.exit(0);			
		}				
		else if (!verifyString()) {
			System.out.println("UNGRAMMATICAL");
			System.exit(0);
		}
				
		slrTable = new SLRTable();

		//Initialize the DFA
		PState nState = new PState(0, ' ', "");
		stack.push(nState);		
	}
	
	public void printParseTree(){		
		System.out.println("ACCEPT");
		pTStack.peek().printNode("");		
	}	

	public void parse() {
		char ch = pString.charAt(0);
			
		if(ch==' ' || ch=='\t' || ch=='\n')
		{
			pString=pString.substring(1);
			parse();			
		}		
		else
			handleSymbol(ch);
	}	

	//Based on the state of the DFA from the stack and the next symbol encountered, if
	//we come across a shift action from the SLR table, then this data is appended to
	//the table and the parse tree stack is updated accordingly
	void addShiftTableRow(int presentS, String symbol, int aVal) {
		TableRow sTRow = new TableRow();

		sTRow.stack = stack.peek().sString;
		sTRow.iTokens = pString;
		sTRow.aLookup = new String("[").concat(Integer.toString(presentS))
				.concat(",").concat(symbol).concat("]");
		sTRow.aValue = new String("S").concat(Integer.toString(aVal));

		sTRow.sAction = new String("Push ").concat(symbol).concat(
				Integer.toString(aVal));

		stack.push(new PState(aVal, symbol.charAt(0), stack.peek().sString));

		if (pTStack.empty())
			pTStack.push(new Node(symbol, ""));
		else
			pTStack.push(new Node(symbol, pTStack.peek().getCompleteRep()));

		table.add(sTRow);
	}

	//getSLRAction returns "-7", the accept state 
	void addAcceptTableRow(int presentS, String symbol, int aVal) {
		TableRow sTRow = new TableRow();

		sTRow.stack = stack.peek().sString;
		sTRow.iTokens = pString;
		sTRow.aLookup = new String("[").concat(Integer.toString(presentS))
				.concat(",").concat(symbol).concat("]");
		sTRow.aValue = "Accept";

		table.add(sTRow);
	}

	//Based on the state of the DFA from the stack and the next symbol encountered, if
	//we come across a reduce action from the SLR table, then this data is appended to
	//the table and the parse tree stack is updated accordingly	
	void addReduceTableRow(int presentS, String symbol, int aVal) {
		TableRow sTRow = new TableRow();

		sTRow.stack = stack.peek().sString;
		sTRow.iTokens = pString;
		sTRow.aLookup = new String("[").concat(Integer.toString(presentS))
				.concat(",").concat(symbol).concat("]");
		sTRow.aValue = new String("R").concat(Integer.toString(aVal));
		sTRow.vLHS = getVLHS(aVal);

		int numPop = getLRHS(aVal);
		sTRow.lRHS = Integer.toString(numPop);

		for (int i = 0; i < numPop; i++)
			stack.pop();

		sTRow.tStack = stack.peek().sString;

		symbol = sTRow.vLHS;

		sTRow.gLookup = new String("[")
				.concat(Integer.toString(stack.peek().state)).concat(",")
				.concat(symbol).concat("]");

		aVal = slrTable.getSLRAction(stack.peek().state, symbol.charAt(0));
		sTRow.gValue = Integer.toString(aVal);

		sTRow.sAction = new String("Push ").concat(symbol).concat(
				Integer.toString(aVal));

		stack.push(new PState(aVal, symbol.charAt(0), stack.peek().sString));

		Node nNode = new Node(numPop, symbol, "");

		for (int i = numPop - 1; i >= 0; i--)
			nNode.setNextNode(pTStack.pop(), i);

		if (!pTStack.empty())
			nNode.setPrevRep(pTStack.peek().getCompleteRep());

		pTStack.push(nNode);

		sTRow.ptStack = pTStack.peek().getCompleteRep();

		table.add(sTRow);
	}

	void handleSymbol(char ch) {
				
		PState present = stack.peek();

		int action = slrTable.getSLRAction(present.state, ch);

		if (action > 0)// Shift
		{
			addShiftTableRow(present.state, getSymbolString(ch), action);

			if (ch == 'i')
				pString = pString.substring(2);
			else
				pString = pString.substring(1);
		} else {
			if (action == -7)// Accept
			{
				addAcceptTableRow(present.state, getSymbolString(ch), action);
				
		        SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	new CreateGUI(table); //Pop up the GUI to show the whole table
		            }
		        });
							
				printParseTree();				
				return;
				
			} else
				// Reduce
				addReduceTableRow(present.state, getSymbolString(ch), action*-1);
		}

		parse();

	}	

	int getLRHS(int aVal) {
		if (aVal == 1 || aVal == 3 || aVal == 5)
			return 3;
		else
			return 1;
	}

	String getVLHS(int aVal) {
		if (aVal == 1 || aVal == 2)
			return "E";
		else if (aVal == 3 || aVal == 4)
			return "T";
		else
			return "F";
	}

	String getSymbolString(char symbol) {
		if (symbol == ' ')
			return "";
		else if (symbol == 'i')
			return "id";
		else
			return Character.toString(symbol);
	}

	public boolean verifyString() {
		String sStr = "()+*$ \t\n";

		for (int i = 0, len = pString.length(); i < len;) {
			char ch = pString.charAt(i);

			if (ch == 'i' && (i + 1) < len && pString.charAt(i + 1) == 'd')
				i += 2;
			else if (sStr.indexOf(ch) == -1)
				return false;
			else
				i++;
		}

		return true;
	}

	//Node with state, symbol info to be used on the stack.
	public class PState {
		int state;
		char symbol;
		String sString; //Representation of the entire stack up until this point.

		PState(int state, char symbol, String prefix) {
			this.symbol = symbol;
			this.state = state;

			this.sString = prefix.concat(getSymbolString(symbol)).concat(
					Integer.toString(state));
		}

	}

	public class TableRow {
		String stack = "", iTokens = "", aLookup = "", aValue = "", vLHS = "",
				lRHS = "", tStack = "", gLookup = "", gValue = "",
				sAction = "", ptStack = "";
	}

}
