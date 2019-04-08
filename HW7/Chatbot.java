
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
        Integer[] corpusArray = new Integer[corpus.size()];
        corpus.toArray(corpusArray);

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
            int[] history = {};

            ngram(r, history, corpusArray, v).print();

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
            int[] history = { h };

            ngram(r, history, corpusArray, v).print();

        } else if (flag == 500) {
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int hhcount = 0;
            int hhwcount = 0;

            for (int i = 0; i < corpus.size() - 2; i++) {
                if (corpus.get(i) == h1 && corpus.get(i + 1) == h2) {
                    hhcount++;
                    if (corpus.get(i + 2) == w) {
                        hhwcount++;
                    }
                }
            }
            double prob = (hhwcount + 1) / (double) (hhcount + v);

            System.out.println(hhwcount);
            System.out.println(hhcount);
            System.out.println(df.format(prob));

        } else if (flag == 600) {
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);

            double r = n1 / (double) n2;
            int[] history = { h1, h2 };

            ngram(r, history, corpusArray, v).print();

        } else if (flag == 700) {
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1 = 0, h2 = 0;

            Random rng = new Random();
            if (seed != -1)
                rng.setSeed(seed);

            if (t == 0) {
                // Generate first word using r
                double r = rng.nextDouble();
                int[] history = {};
                h1 = ngram(r, history, corpusArray, v).pos;

                System.out.println(h1);
                if (h1 == 9 || h1 == 10 || h1 == 12) {
                    return;
                }
                history = new int[] { h1 };

                // Generate second word using r
                r = rng.nextDouble();
                h2 = ngram(r, history, corpusArray, v).pos;
                System.out.println(h2);
            } else if (t == 1) {
                h1 = Integer.valueOf(args[3]);
                // Generate second word using r
                double r = rng.nextDouble();
                int[] history = { h1 };
                h2 = ngram(r, history, corpusArray, v).pos;

                System.out.println(h2);
            } else if (t == 2) {
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while (h2 != 9 && h2 != 10 && h2 != 12) {
                double r = rng.nextDouble();
                int w = 0;
                // Generate new word using h1,h2
                int[] history = { h1, h2 };
                w = ngram(r, history, corpusArray, v).pos;
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }

    public static PosProbs ngram(double r, int[] history, Integer[] corpus, int vocabSize) {
        int counts[] = new int[vocabSize];
        double probs[] = new double[vocabSize];

        // Count number of times entire history appears in corpus
        int hcount = 0;
        for (int i = 0; i < corpus.length - history.length; i++) {
            // Check to see if we find the entire history starting at position i
            boolean found = true;
            for (int j = 0; j < history.length; j++) {
                if (corpus[i + j] != history[j]) {
                    found = false;
                    break;
                }
            }

            // If history found, increment history count and the count of the word after.
            if (found) {
                hcount++;
                counts[corpus[i + history.length]]++;
            }
        }

        // Now that we have the counts, use laplace smoothing to find probabilities
        for (int i = 0; i < vocabSize; i++) {
            probs[i] = (counts[i] + 1) / (double) (hcount + vocabSize);
        }

        // Get lower and higher probabilities and position of random number r
        double li = 0;
        double ri = probs[0];
        int j = 1;
        while (ri < r && j < vocabSize) {
            li = ri;
            ri += probs[j++];
        }

        // Return wrapper object for the probabilities and position
        return new PosProbs(j - 1, li, ri);
    }
}

// Wrapper object for a position and the lower and higher probabilities.
class PosProbs {
    public int pos;
    public double lowerProb;
    public double higherProb;

    public PosProbs(int pos, double lowerProb, double higherProb) {
        this.pos = pos;
        this.lowerProb = lowerProb;
        this.higherProb = higherProb;
    }

    public void print() {
        DecimalFormat df = new DecimalFormat("0.0000000");
        System.out.println(this.pos);
        System.out.println(df.format(this.lowerProb));
        System.out.println(df.format(this.higherProb));
    }
}