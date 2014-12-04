package edu.louisiana.cacs.csce450GProject;

//     (    )    +    *    id    $      E    T    F
//   +----+----+----+----+----+------||----+----+----+
//0  | s4 |    |    |    | s5 |      || 1  | 2  | 3  |
//   +----+----+----+----+----+------||----+----+----+
//1  |    |    | s6 |    |    |accept||    |    |    |
//   +----+----+----+----+----+------||----+----+----+
//2  |    | r2 | r2 | s7 |    |  r2  ||    |    |    |
//   +----+----+----+----+----+------||----+----+----+
//3  |    | r4 | r4 | r4 |    |  r4  ||    |    |    |
//   +----+----+----+----+----+------||----+----+----+
//4  | s4 |    |    |    | s5 |      || 8  | 2  | 3  |
//   +----+----+----+----+----+------||----+----+----+
//5  |    | r6 | r6 | r6 |    |  r6  ||    |    |    |
//   +----+----+----+----+----+------||----+----+----+
//6  | s4 |    |    |    | s5 |      ||    | 9  | 3  |
//   +----+----+----+----+----+------||----+----+----+
//7  | s4 |    |    |    | s5 |      ||    |    | 10 |
//   +----+----+----+----+----+------||----+----+----+
//8  |    |s11 | s6 |    |    |      ||    |    |    |
//   +----+----+----+----+----+------||----+----+----+
//9  |    | r1 | r1 | s7 |    |  r1  ||    |    |    |
//   +----+----+----+----+----+------||----+----+----+
//10 |    | r3 | r3 | r3 |    |  r3  ||    |    |    |
//   +----+----+----+----+----+------||----+----+----+
//11 |    | r5 | r5 | r5 |    |  r5  ||    |    |    |	

public class SLRTable {

	private int[][] slrTable = new int[12][9];

	SLRTable() {
		
		//The values obtained above are stored as follows in the 2d array
		//s5 -> 5
		//r5 -> -5
		//goto5 -> 5
		//Remaining values are 0s to pick up errors
		//Accept -> -7
		
		slrTable[0][0] = 4;
		slrTable[0][4] = 5;
		slrTable[0][6] = 1;
		slrTable[0][7] = 2;
		slrTable[0][8] = 3;

		slrTable[1][2] = 6;
		slrTable[1][5] = -7;

		slrTable[2][1] = -2;
		slrTable[2][2] = -2;
		slrTable[2][3] = 7;
		slrTable[2][5] = -2;

		slrTable[3][1] = -4;
		slrTable[3][2] = -4;
		slrTable[3][3] = -4;
		slrTable[3][5] = -4;

		slrTable[4][0] = 4;
		slrTable[4][4] = 5;
		slrTable[4][6] = 8;
		slrTable[4][7] = 2;
		slrTable[4][8] = 3;

		slrTable[5][1] = -6;
		slrTable[5][2] = -6;
		slrTable[5][3] = -6;
		slrTable[5][5] = -6;

		slrTable[6][0] = 4;
		slrTable[6][4] = 5;
		slrTable[6][7] = 9;
		slrTable[6][8] = 3;

		slrTable[7][0] = 4;
		slrTable[7][4] = 5;
		slrTable[7][8] = 10;

		slrTable[8][1] = 11;
		slrTable[8][2] = 6;

		slrTable[9][1] = -1;
		slrTable[9][2] = -1;
		slrTable[9][3] = 7;
		slrTable[9][5] = -1;

		slrTable[10][1] = -3;
		slrTable[10][2] = -3;
		slrTable[10][3] = -3;
		slrTable[10][5] = -3;

		slrTable[11][1] = -5;
		slrTable[11][2] = -5;
		slrTable[11][3] = -5;
		slrTable[11][5] = -5;
	}

	//Given a symbol (eg E) returns its index which was used to create the 2D array
	int getSymIndex(char ch) {
		if (ch == 'E')
			return 6;
		else if (ch == 'T')
			return 7;
		else if (ch == 'F')
			return 8;
		else if (ch == '(')
			return 0;
		else if (ch == ')')
			return 1;
		else if (ch == '+')
			return 2;
		else if (ch == '*')
			return 3;
		else if (ch == 'i')
			return 4;
		else //$
			return 5;
	}

	//Given the state of the DFA, for a symbol returns the action to be performed
	//from the table created above
	
	int getSLRAction(int state, char symbol) {
		int action = slrTable[state][getSymIndex(symbol)];

		if (action == 0) {
			System.out.println("UNGRAMMATICAL");
			System.exit(0);
		}

		return action;
	}

}
