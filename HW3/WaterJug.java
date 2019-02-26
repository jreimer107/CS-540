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
		return this.curr_jug1 == other.curr_jug1 && this.curr_jug2 == other.curr_jug2;
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

		// Convert list to array and sort it
		State[] succArray = new State[successors.size()];
		succArray = successors.toArray(succArray);
		Arrays.sort(succArray);
		return succArray;
	}

	public boolean isGoalState() {
		return this.curr_jug1 == this.goal || this.curr_jug2 == this.goal;
	}

	public void printState(int option, int depth) {
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
		} else {
			UninformedSearch.run(this, option, depth);
		}

	}

}

class UninformedSearch {
	private static void bfs(State curr_state) {
		// Create open queue and closed list
		// LinkedHashSet for uniqueness, easy contains(), and FIFO iterating
		// HashSet for uniqueness and easy contains()
		LinkedHashSet<State> open = new LinkedHashSet<>();
		HashSet<State> closed = new HashSet<>();

		open.add(curr_state);
		System.out.println(curr_state);
		while (!open.isEmpty()) {
			// Pop item off queue
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
		String path = "";
		while (curr_state.parentPt != null) {
			path = curr_state.toString() + " " + path;
			curr_state = curr_state.parentPt;
		}
		System.out.println("Path " + curr_state + " " + path);
	}

	private static void dfs(State curr_state) {
		// Stack for LIFO ordering, uniqueness done manually
		// HashSet for uniqueness and easy contains()
		Stack<State> open = new Stack<>();
		HashSet<State> closed = new HashSet<>();

		open.push(curr_state);
		System.out.println(curr_state);
		while (!open.isEmpty()) {
			curr_state = open.pop();
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
				if (!closed.contains(successor) && !open.contains(successor)) {
					successor.parentPt = curr_state;
					open.push(successor);
				}
			}

			// Done expanding, add to closed
			closed.add(curr_state);
			System.out.println(" " + open.toString() + " " + closed.toString());
		}

		// Curr state is goal state, print goal and path
		System.out.println(" goal");
		String path = "";
		while (curr_state.parentPt != null) {
			path = curr_state.toString() + " " + path;
			curr_state = curr_state.parentPt;
		}
		System.out.println("Path " + curr_state + " " + path);
	}

	private static void iddfs(State init_state, int depth) {
		// Stack for LIFO ordering, uniqueness done manually
		// HashSet for uniqueness and easy contains()
		Stack<State> open = new Stack<>();
		HashSet<State> closed = new HashSet<>();
		boolean foundGoal = false;
		State curr_state = new State(init_state);

		// For each level of iterative deepening
		for (int d = 0; d <= depth; d++) {
			// Do DFS to that level
			open.push(init_state);
			System.out.println(d + ":" + init_state);
			while (!open.isEmpty()) {
				curr_state = open.pop();
				System.out.print(d + ":" + curr_state);

				// Check if curr is goal and quit if so
				if (curr_state.isGoalState()) {
					foundGoal = true;
					break;
				}

				// If we've already checked this node, continue
				if (closed.contains(curr_state)) {
					continue;
				}

				// Only expand if node depth is less than max depth
				if (curr_state.depth < d) {
					State[] successors = curr_state.getSuccessors();
					for (State successor : successors) {
						// Only expand nodes that have not been expanded before
						if (!closed.contains(successor) && !open.contains(successor)) {
							successor.parentPt = curr_state;
							successor.depth = curr_state.depth + 1;
							open.push(successor);
						}
					}
				}
				// Done expanding, add to closed
				closed.add(curr_state);
				System.out.println(" " + open.toString() + " " + closed.toString());
			}

			if (foundGoal)
				break;

			open.clear();
			closed.clear();
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

	public static void run(State curr_state, int option, int depth) {
		if (option == 3) {
			bfs(curr_state);
		} else if (option == 4) {
			dfs(curr_state);
		} else {
			iddfs(curr_state, depth);
		}
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