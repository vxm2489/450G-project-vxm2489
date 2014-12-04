package edu.louisiana.cacs.csce450GProject;

//Representation of a ParseTree

public class Node {

	Node[] next;
	int numNext = 0;
	String symbol;
	boolean isTerminal;
	String prevRep; //Representation of the parse tree stack below this parse tree 

	Node(String symbol, String prevRep) {
		this.symbol = symbol;
		isTerminal = true;
		this.prevRep = prevRep;
	}

	Node(int nNext, String symbol, String prevRep) {
		numNext = nNext;
		next = new Node[nNext];
		this.symbol = symbol;
		isTerminal = false;
		this.prevRep = prevRep;
	}

	void setPrevRep(String prevRep) {
		this.prevRep = prevRep;
	}

	void setNextNode(Node node, int index) {
		next[index] = node;
	}

	//Returns the representation of this parse tree
	String getNodeRep() {
		if (isTerminal)
			return symbol;
		else {
			String nextNodeRep = "";

			if (next[0].isTerminal)
				nextNodeRep = " ";

			for (int i = 0; i < numNext; i++)
				nextNodeRep = nextNodeRep.concat(next[i].getNodeRep());

			return new String("[").concat(symbol).concat(nextNodeRep)
					.concat("]");
		}

	}

	//Returns the representation of the parse tree stack up until this point
	//Concatenate the representation of this parse tree with prevRep explained above	
	String getCompleteRep() {
		return getNodeRep().concat(prevRep);
	}
	
	//Pretty print this parse tree to the output stream
	void printNode(String prefix)
	{
		System.out.println(prefix+symbol);
		
		for(int i=0;i<numNext;i++)
			next[i].printNode(prefix.concat(" "));
		
	}

}
