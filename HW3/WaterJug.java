import java.util.*;

class State {
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
		this.curr_jug1 = other.curr_jug2;
		this.curr_jug2 = other.curr_jug2;
		this.goal = other.goal;
		this.depth = other.depth;
		this.parentPt = other.parentPt;
	}

	public State[] getSuccessors() {

		// TO DO: get all successors and return them in proper order
		List<State> successors = new ArrayList<State>();

		// Dump first bucket
		if (this.curr_jug1 != 0) {
			State e1 = new State(this);
			e1.curr_jug1 = 0;
			successors.add(e1);
			System.out.print("yo");
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
		}

		// Fill second bucket
		if (this.curr_jug2 != this.cap_jug2) {
			State f2 = new State(this);
			f2.curr_jug2 = this.cap_jug2;
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
			p12.cap_jug2 += pourAmount;
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
			p21.cap_jug1 += pourAmount;
		}

		State[] succArray = new State[successors.size()];
		return successors.toArray(succArray);
	}

	public boolean isGoalState() {
		boolean isGoal = false;
		// TO DO: determine if the state is a goal node or not and return boolean

		return isGoal;
	}

	public void printState(int option, int depth) {

		// TO DO: print a State based on option (flag)

		if (option == 1) {
			State[] successors = this.getSuccessors();
			System.out.println(successors.length);
			for (State successor : successors) {
				System.out.println(Integer.toString(successor.curr_jug1) + Integer.toString(successor.curr_jug2));
				;
			}
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
	private static void bfs(State curr_state) {
		// TO DO: run breadth-first search algorithm
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