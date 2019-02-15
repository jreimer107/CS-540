import java.util.*;

public class Number{
           
    public static String getStep(int x, int y) {
        // TO DO
        // Implement the getStep method
        String result = "";
        
        ArrayList<intWrapper> openList = new ArrayList<intWrapper>();
        ArrayList<intWrapper> closedList = new ArrayList<intWrapper>();
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
	


