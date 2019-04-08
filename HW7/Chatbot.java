
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class Chatbot {
	private static String filename = "./corpus.txt";

	private static ArrayList<Integer> readCorpus() {
		ArrayList<Integer> corpus = new ArrayList<Integer>();
		try {
			File f = new File(filename);
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				if (sc.hasNextInt()) {
					int i = sc.nextInt();
					corpus.add(i);
				} else {
					sc.next();
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found.");
		}
		return corpus;
	}

	static public void main(String[] args) {
		ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);

		int v = 4700;
		DecimalFormat df = new DecimalFormat("0.0000000");

		if (flag == 100) {
			int w = Integer.valueOf(args[1]);
			int count = 0;
			for (int token : corpus) {
				if (token == w) {
					count++;
				}
			}
			double prob = (count + 1) / (double) (corpus.size() + v);

			System.out.println(count);
			System.out.println(df.format(prob));

		} else if (flag == 200) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);

			double r = n1 / (double) n2;

			int counts[] = new int[v];
			double probs[] = new double[v];

			for (int i = 0; i < corpus.size(); i++) {
				counts[corpus.get(i)]++;
			}
			for (int i = 0; i < v; i++) {
				probs[i] = (counts[i] + 1) / (double) (corpus.size() + v);
			}

			// Get li and ri
			double li = 0;
			double ri = probs[0];
			int j = 1;
			while (ri < r && j < v) {
				li = ri;
				ri += probs[j++];
			}

			System.out.println(j - 1);
			System.out.println(df.format(li));
			System.out.println(df.format(ri));

		} else if (flag == 300) {
			int h = Integer.valueOf(args[1]);
			int w = Integer.valueOf(args[2]);
			int hcount = 0;
			int hwcount = 0;
			for (int i = 0; i < corpus.size() - 1; i++) {
				if (corpus.get(i) == h) {
					hcount++;
					if (corpus.get(i + 1) == w) {
						hwcount++;
					}
				}
			}
			double prob = (hwcount + 1) / (double) (hcount + v);

			System.out.println(hwcount);
			System.out.println(hcount);
			System.out.println(df.format(prob));

		} else if (flag == 400) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h = Integer.valueOf(args[3]);

			double r = n1 / (double) n2;

			int counts[] = new int[v];
			double probs[] = new double[v];

			int hcount = 0;
			for (int i = 0; i < corpus.size() - 1; i++) {
				if (corpus.get(i) == h) {
					hcount++;
					counts[corpus.get(i + 1)]++;
				}
			}
			for (int i = 0; i < v; i++) {
				probs[i] = (counts[i] + 1) / (double) (hcount + v);
			}

			// Get li and ri
			double li = 0;
			double ri = probs[0];
			int j = 1;
			while (ri < r && j < v) {
				li = ri;
				ri += probs[j++];
			}

			System.out.println(j - 1);
			System.out.println(df.format(li));
			System.out.println(df.format(ri));

		} else if (flag == 500) {
			int h1 = Integer.valueOf(args[1]);
			int h2 = Integer.valueOf(args[2]);
			int w = Integer.valueOf(args[3]);
			int count = 0;
			ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
			// TODO

		} else if (flag == 600) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h1 = Integer.valueOf(args[3]);
			int h2 = Integer.valueOf(args[4]);
			// TODO
		} else if (flag == 700) {
			int seed = Integer.valueOf(args[1]);
			int t = Integer.valueOf(args[2]);
			int h1 = 0, h2 = 0;

			Random rng = new Random();
			if (seed != -1)
				rng.setSeed(seed);

			if (t == 0) {
				// TODO Generate first word using r
				double r = rng.nextDouble();
				System.out.println(h1);
				if (h1 == 9 || h1 == 10 || h1 == 12) {
					return;
				}

				// TODO Generate second word using r
				r = rng.nextDouble();
				System.out.println(h2);
			} else if (t == 1) {
				h1 = Integer.valueOf(args[3]);
				// TODO Generate second word using r
				double r = rng.nextDouble();
				System.out.println(h2);
			} else if (t == 2) {
				h1 = Integer.valueOf(args[3]);
				h2 = Integer.valueOf(args[4]);
			}

			while (h2 != 9 && h2 != 10 && h2 != 12) {
				double r = rng.nextDouble();
				int w = 0;
				// TODO Generate new word using h1,h2
				System.out.println(w);
				h1 = h2;
				h2 = w;
			}
		}

		return;
	}
}
