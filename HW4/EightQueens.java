import java.util.*;
import java.lang.Math;

class State {
	char[] board;

	public State(char[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
	}

	public void printState(int option, int iteration, int seed) {

		// TO DO: print output based on option (flag)
		if (option == 1) {
			System.out.println(this.getHeuristicCost());
		}

	}

	// TO DO: feel free to add/remove/modify methods/fields
	private int getHeuristicCost() {
		int cost = 0;
		for (int queen = 0; queen < 8; queen++) {
			int queenRow = getRow(queen);
			int queenCol = queen;

			// Count number of queens in same row
			// Dont look at any queens to our left, they have already seen us
			for (int col = queenCol + 1; col < 8; col++) {
				// If the row number matches that of the analyzing queen
				if (queenRow == getRow(col)) {
					cost++;
				}
			}

			// Count number of queens in same diagonal
			// Dont count any queens to our left, they have already seen us
			// Only look at right diagonals
			// Upper right diagonal
			for (int col = queenCol + 1, row = queenRow + 1; col < 8 && row < 8; col++, row++) {
				if (getRow(col) == row) {
					cost++;
				}
			}

			// Lower right diagonal
			for (int col = queenCol + 1, row = queenRow - 1; col < 8 && row >= 0; col++, row--) {
				if (getRow(col) == row) {
					cost++;
				}
			}
		}
		return cost;
	}

	private int getRow(int col) {
		return Character.getNumericValue(board[col]);
	}

}

class EightQueen {
	public static void main(String args[]) {
		if (args.length != 2 && args.length != 3) {
			System.out.println("Invalid Number of Input Arguments");
			return;
		}

		int flag = Integer.valueOf(args[0]);
		int option = flag / 100;
		int iteration = flag % 100;
		char[] board = new char[8];
		int seed = -1;
		int board_index = -1;

		if (args.length == 2 && (option == 1 || option == 2 || option == 4)) {
			board_index = 1;
		} else if (args.length == 3 && (option == 3 || option == 5)) {
			seed = Integer.valueOf(args[1]);
			board_index = 2;
		} else {
			System.out.println("Invalid Number of Input Arguments");
			return;
		}

		if (board_index == -1)
			return;
		for (int i = 0; i < 8; i++) {
			board[i] = args[board_index].charAt(i);
			int pos = board[i] - '0';
			if (pos < 0 || pos > 7) {
				System.out.println("Invalid input: queen position(s)");
				return;
			}
		}

		State init = new State(board);
		init.printState(option, iteration, seed);
	}
}