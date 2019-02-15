import java.util.*;

public class Number{
           
    public static String getStep(int x, int y) {
        // TO DO
        // Implement the getStep method
        String result = "";
        
        List<intWrapper> openList = new ArrayList<>();
        List<intWrapper> closedList = new ArrayList<>();
        List<intWrapper> successors;
        openList.add(new intWrapper(x, Math.abs(y - x), null));
        
        int G = 1;
        intWrapper curr;
        do {
            curr = openList.remove(0);
            closedList.add(curr);
            
            if (curr.value == y) {
                break;
            }

            successors = curr.getSuccessors();
            for (intWrapper successor : successors) {
                int F = G + Math.abs(y - successor.value);

                if (!successor.isInList(openList)) {
                    successor.F = F;
                    successor.prev = curr;
                    openList.add(successor);
                    Collections.sort(openList);
                }
                else if (F < successor.F) {
                    successor.F = F;
                    successor.prev = curr;
                }
            }
            G++;
        } while (openList.size() > 0);

        
        
        






        return result;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }
        
        System.out.println(getStep(Integer.parseInt(args[0]), Integer.parseInt(args[1])));

    }
}

private class intWrapper implements Comparable {
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
        List<intWrapper> successors = new ArrayList<>();
        successors.add(new intWrapper(curr.value + 1, Integer.MAX_VALUE, curr));
        successors.add(new intWrapper(curr.value - 1, Integer.MAX_VALUE, curr));
        successors.add(new intWrapper(curr.value * 3, Integer.MAX_VALUE, curr));
        successors.add(new intWrapper((int)(Math.pow(curr.value, 2)), Integer.MAX_VALUE, curr));
    }
}