import java.util.*;

public class Number{

    private static String getStep(int x, int y) {
        int add, sub, mult, square;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(x);
        int checked = 0;
        int curr;
        while (!queue.isEmpty()) {
            curr = queue.remove();
            if (curr == y) {
                //Get log base 4 of number checked, this is depth in tree we've descended
                return Integer.toString((int)Math.ceil(Math.log(checked) / Math.log(4)));
            }
            //Queue successors
            add = curr + 1;
            sub = curr - 1;
            mult = curr * 3;
            square = (int)Math.pow(curr,2);
            if (add > 0 && add < 100) {
                queue.add(add);
            }
            if (sub > 0 && sub < 100) {
                queue.add(sub);
            }
            if (mult > 0 && mult < 100) {
                queue.add(mult);
            }
            if (square > 0 && square < 100) {
                queue.add(square);
            }
            checked++;
        }
        return "";
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }
        
        System.out.println(getStep(Integer.parseInt(args[0]), Integer.parseInt(args[1])));

	}
	
	

}
	


