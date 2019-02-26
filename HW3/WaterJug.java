import java.util.*;

class State implements Comparable<State> {
	int cap_jug1;
	int cap_jug2;
	int curr_jug1;
	int curr_jug2;
	int goal;
	int depth;
	State parentPt;

	public State(int cap_jug1, int cap_jug2, int curr_jug1, int curr_jug2, int goal, int depth) {
		this.cap_jug1 = cap_jug1;
		this.cap_jug2 = cap_jug2;
		this.curr_jug1 = curr_jug1;
		this.curr_jug2 = curr_jug2;
		this.goal = goal;
		this.depth = depth;
		this.parentPt = null;
	}

	public State(State other) {
		this.cap_jug1 = other.cap_jug1;
		this.cap_jug2 = other.cap_jug2;
		this.curr_jug1 = other.curr_jug1;
		this.curr_jug2 = other.curr_jug2;
		this.goal = other.goal;
		this.depth = other.depth;
		this.parentPt = other.parentPt;
	}

	@Override
	public int compareTo(State other) {
		if (this.curr_jug1 != other.curr_jug1) {
			return Integer.compare(this.curr_jug1, other.curr_jug1);
		} else {
			return Integer.compare(this.curr_jug2, other.curr_jug2);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof State)) {
			return false;
		}

		State other = (State) o;
		boolean result = this.curr_jug1 == other.curr_jug1 && this.curr_jug2 == other.curr_jug2;
		if (!result) {
			// System.out.println("Not equals:" + this.toString() + " " + other.toString());
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.curr_jug1 + this.curr_jug2;
	}

	@Override
	public String toString() {
		return Integer.toString(this.curr_jug1) + Integer.toString(this.curr_jug2);
	}

	public State[] getSuccessors() {

		// TO DO: get all successors and return them in proper order
		// PriorityQueue<State> successors = new PriorityQueue<>();
		List<State> successors = new ArrayList<>();
		// Dump first bucket

		if (this.curr_jug1 != 0) {
			State e1 = new State(this);
			e1.curr_jug1 = 0;
			successors.add(e1);
		}

		// Dump second bucket
		if (this.curr_jug2 != 0) {
			State e2 = new State(this);
			e2.curr_jug2 = 0;
			successors.add(e2);
		}

		// Fill first bucket
		if (this.curr_jug1 != this.cap_jug1) {
			State f1 = new State(this);
			f1.curr_jug1 = this.cap_jug1;
			successors.add(f1);
		}

		// Fill second bucket
		if (this.curr_jug2 != this.cap_jug2) {
			State f2 = new State(this);
			f2.curr_jug2 = this.cap_jug2;
			successors.add(f2);
		}

		// Pour first bucket into second
		if (this.curr_jug2 != this.cap_jug2 && this.curr_jug1 != 0) {
			State p12 = new State(this);
			int space = this.cap_jug2 - this.curr_jug2;
			int pourAmount = this.curr_jug1;
			if (pourAmount > space) {
				pourAmount = space;
			}
			p12.curr_jug1 -= pourAmount;
			p12.curr_jug2 += pourAmount;
			successors.add(p12);
		}

		// Pour second bucket into first
		if (this.curr_jug1 != this.cap_jug1 && this.curr_jug2 != 0) {
			State p21 = new State(this);
			int space = this.cap_jug1 - this.curr_jug1;
			int pourAmount = this.curr_jug2;
			if (pourAmount > space) {
				pourAmount = space;
			}
			p21.curr_jug2 -= pourAmount;
			p21.curr_jug1 += pourAmount;
			successors.add(p21);
		}

		State[] succArray = new State[successors.size()];
		succArray = successors.toArray(succArray);
		Arrays.sort(succArray);
		return succArray;
	}

	public boolean isGoalState() {
		// TO DO: determine if the state is a goal node or not and return boolean

		return this.curr_jug1 == this.goal || this.curr_jug2 == this.goal;
	}

	public void printState(int option, int depth) {

		// TO DO: print a State based on option (flag)

		// Collections.sort(succList);
		if (option <= 2) {
			State[] successors = this.getSuccessors();
			for (State succ : successors) {
				System.out.print(succ);
				if (option == 2) {
					System.out.println(" " + Boolean.toString(succ.isGoalState()));
				} else {
					System.out.println();
				}
			}
		} else if (option == 3) {
			UninformedSearch.bfs(this);
		}

	}

	public String getOrderedPair() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.curr_jug1);
		builder.append(this.curr_jug2);
		return builder.toString().trim();
	}

	// TO DO: feel free to add/remove/modify methods/fields

}

class UninformedSearch {
	public static void bfs(State curr_state) {
		//Create open queue and closed list
		LinkedHashSet<State> open = new LinkedHashSet<>();
		LinkedHashSet<State> closed = new LinkedHashSet<>();
		open.add(curr_state);
		while (!open.isEmpty()) {
			// Pop item off of priority queue
			Iterator<State> i = open.iterator();
			curr_state = i.next();
			i.remove();
			System.out.print(curr_state);

			// Check if curr is goal and quit if so
			if (curr_state.isGoalState()) {
				break;
			}

			// If we've already checked this node, continue
			if (closed.contains(curr_state)) {
				continue;
			}

			// Expand current node, get semesters and add to queue
			State[] successors = curr_state.getSuccessors();
			for (State successor : successors) {
				// Only expand nodes that have not been expanded before
				if (!closed.contains(successor)) {
					successor.parentPt = curr_state;
					open.add(successor);
				}
			}

			// Done expanding, add to closed
			closed.add(curr_state);
			System.out.println(" " + open.toString() + " " + closed.toString());
		}

		// Curr state is goal state, print goal and path
		System.out.println(" goal");
		// Print path
		String path = "";
		while (curr_state.parentPt != null) {
			path = curr_state.toString() + " " + path;
			curr_state = curr_state.parentPt;
		}
		System.out.println("Path " + curr_state + " " + path);
	}

	private static void dfs(State curr_state) {
		// TO DO: run depth-first search algorithm
	}

	private static void iddfs(State curr_state, int depth) {
		// TO DO: run IDDFS search algorithm
	}

	public static void run(State curr_state, int option, int depth) {
		// TO DO: run either bfs, dfs or iddfs according to option (flag)
	}
}

public class WaterJug {
	public static void main(String args[]) {
		if (args.length != 6) {
			System.out.println("Invalid Number of Input Arguments");
			return;
		}

		int flag = Integer.valueOf(args[0]);
		int cap_jug1 = Integer.valueOf(args[1]);
		int cap_jug2 = Integer.valueOf(args[2]);
		int curr_jug1 = Integer.valueOf(args[3]);
		int curr_jug2 = Integer.valueOf(args[4]);
		int goal = Integer.valueOf(args[5]);

		int option = flag / 100;
		int depth = flag % 100;

		if (option < 1 || option > 5) {
			System.out.println("Invalid flag input");
			return;
		}
		if (cap_jug1 > 9 || cap_jug2 > 9 || curr_jug1 > 9 || curr_jug2 > 9) {
			System.out.println("Invalid input: 2-digit jug volumes");
			return;
		}
		if (cap_jug1 < 0 || cap_jug2 < 0 || curr_jug1 < 0 || curr_jug2 < 0) {
			System.out.println("Invalid input: negative jug volumes");
			return;
		}
		if (cap_jug1 < curr_jug1 || cap_jug2 < curr_jug2) {
			System.out.println("Invalid input: jug volume exceeds its capacity");
			return;
		}
		State init = new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, 0);
		init.printState(option, depth);

	}
}