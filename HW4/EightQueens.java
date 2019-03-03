import java.util.*;
import java.lang.Math;

class State {
	char[] board;
	int cost;

	public State(char[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
		this.cost = this.getHeuristicCost();
	}

	public State(char[] arr, int index, int value) {
		this.board = Arrays.copyOf(arr, arr.length);
		this.board[index] = (char) (value + '0');
		this.cost = this.getHeuristicCost();
	}

	public String toString() {
		return new String(this.board);
	}

	public void printState(int option, int iteration, int seed) {

		// TO DO: print output based on option (flag)
		if (option == 1) {
			System.out.println(cost);
		} else if (option == 2) {
			State[] minSuccessors = getBestSucessors();
			if (minSuccessors.length > 0) {
				for (State succ : minSuccessors) {
					System.out.println(succ);
				}
				System.out.println(minSuccessors[0].cost);
			}
		} else if (option == 3) {
			hillClimb(iteration, seed);
		} else if (option == 4) {
			firstChoiceHillClimb(iteration);
		} else if (option == 5) {
			simulatedAnneal(iteration, seed);
		}
	}

	private void simulatedAnneal(int iteration, int seed) {
		Random rng = new Random();
		if (seed != -1) {
			rng.setSeed(seed);
		}

		State move = this;
		for (int i = 0; i <= iteration; i++) {
			// Print state of board, quit if solved
			System.out.println(i + ":" + move + " " + move.cost);
			if (move.cost == 0) {
				System.out.println("Solved");
				return;
			}

			// Pick random values per assignment description
			int index = rng.nextInt(7);
			int value = rng.nextInt(7);
			double prob = rng.nextDouble();

			// Use random values to move random queen (select random successor)
			State newMove = new State(move.board, index, value);

			// If new move is better, accept it. Else accept with small probability
			if (move.cost > newMove.cost) {
				move = newMove;
			} else {
				double p = Math.exp((newMove.cost - move.cost) / (100 - i));
				if (prob < p) {
					move = newMove;
				}
			}
		}
	}

	private void firstChoiceHillClimb(int iteration) {
		State move = this;
		for (int i = 0; i <= iteration; i++) {
			// Print status of board, quit if solved
			System.out.println(i + ":" + move + " " + move.cost);
			if (move.cost == 0) {
				System.out.println("Solved");
				return;
			}

			// Look for a successor in a better state. If found, move there.
			boolean foundBetterSucc = false;
			for (State succ : move.getSuccessors()) {
				if (succ.cost < move.cost) {
					move = succ;
					foundBetterSucc = true;
					break;
				}
			}

			// If we have not found a better state, are in local optimum
			if (!foundBetterSucc) {
				System.out.println("Local optimum");
				return;
			}
		}
	}

	private void hillClimb(int iteration, int seed) {
		Random rng = new Random();
		if (seed != -1) {
			rng.setSeed(seed);
		}

		State move = this;
		for (int i = 0; i <= iteration; i++) {
			// Print status of board, quit if solved
			System.out.println(i + ":" + move + " " + move.cost);
			if (move.cost == 0) {
				System.out.println("Solved");
				return;
			}

			// Move one peice
			State[] successors = move.getBestSucessors();
			int r = rng.nextInt(successors.length);
			move = successors[r];
		}
	}

	private State[] getBestSucessors() {
		int minCost = this.cost;
		List<State> minSuccessors = new ArrayList<>();
		for (State succ : getSuccessors()) {
			if (succ.cost < minCost) {
				minCost = succ.cost;
				minSuccessors.clear();
				minSuccessors.add(succ);
			} else if (succ.cost == minCost) {
				minSuccessors.add(succ);
			}
		}
		State[] minArray = new State[minSuccessors.size()];
		return minSuccessors.toArray(minArray);
	}

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

	// Need to update heuristic cost
	private State[] getSuccessors() {
		List<State> successors = new ArrayList<>();
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				// Dont add self to successors
				if (getRow(col) != row) {
					// Move one peice
					successors.add(new State(this.board, col, row));
				}
			}
		}
		State[] succArr = new State[successors.size()];
		return successors.toArray(succArr);
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