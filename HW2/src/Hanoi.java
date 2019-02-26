import java.util.*;

public class Hanoi {

	public static List<String> getSuccessor(String[] hanoi) {
		List<String> result = new ArrayList<>();

		//Loop through each rod
		for(int i = 0; i < hanoi.length; i++) {
			//Check that rod has disks
			String transferResult = null;
			//Attempt to transfer disk left
			transferResult = transferDisk(hanoi, i, i - 1);
			if (transferResult != null) {
				result.add(transferResult);
			}
		
			//Attempt to transfer disk right
			transferResult = transferDisk(hanoi, i, i + 1);
			if (transferResult != null) {
				result.add(transferResult);
			}
		}

		return result;
	}

	public static String transferDisk(String[] hanoi, int from, int to) {
		//Check that we're in range of array of rods
		if (to < 0 || to >= hanoi.length) {
			return null;
		}

		//Check that the sending rod has a disk
		if (hanoi[from].equals("0")) {
			return null;
		}

		//Check that the receiving rod's top disk isn't smaller than the transferring disk
		if (hanoi[to].charAt(0) != '0') {
			if (hanoi[to].charAt(0) < hanoi[from].charAt(0)) {
				return null;
			}
		}


		String[] rods = hanoi.clone(); //to avoid pass-by-reference nonsense

		//Remove top disk from rodFrom, set rodFrom to 0 if no disks left
		String disk = rods[from].substring(0, 1);
		rods[from] = rods[from].substring(1);
		if (rods[from].isEmpty()) {
			rods[from] = "0";
		}

		//Add removed disk to top (beginning) of rodTo
		if (rods[to].equals("0")) {
			rods[to] = disk;
		}
		else {
			rods[to] = disk.concat(rods[to]);
		}

		String allRods = String.join(" ", rods);
		return allRods;
	}

	public static void main(String[] args) {
		if (args.length < 3) {
			return;
		}

		List<String> sucessors = getSuccessor(args);
		for (int i = 0; i < sucessors.size(); i++) {
			System.out.println(sucessors.get(i));
		}
	}

}