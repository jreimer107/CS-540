import java.util.*;

public class intWrapper implements Comparable<intWrapper> {
	public int value;
	public int F;
	public intWrapper prev;
	
	public intWrapper(int value, int F, intWrapper prev) {
		this.value = value;
		this.F = F;
		this.prev = prev;
	}

	public boolean isInList(List<intWrapper> haystack) {
		for (intWrapper straw : haystack) {
			if (value == straw.value) {
				return true;
			}
		}
		   return false;
	}

	public int compareTo(intWrapper other) {
		return (F - other.F);
	}

	public ArrayList<intWrapper> getSuccessors() {
		ArrayList<intWrapper> successors = new ArrayList<>();
		successors.add(new intWrapper(value + 1, Integer.MAX_VALUE, this));
		successors.add(new intWrapper(value - 1, Integer.MAX_VALUE, this));
		successors.add(new intWrapper(value * 3, Integer.MAX_VALUE, this));
		successors.add(new intWrapper((int)(Math.pow(value, 2)), Integer.MAX_VALUE, this));
		return successors;
	}
}