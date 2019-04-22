import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class BodyVsBrain {
	public static void main(String[] args) {
		int FLAG = Integer.parseInt(args[0]);

		DecimalFormat df = new DecimalFormat("0.0000");

		ArrayList<Double> body = new ArrayList<Double>();
		ArrayList<Double> brain = new ArrayList<Double>();

		// Read csv
		try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
			String line;
			br.readLine(); // Skip titles
			while ((line = br.readLine()) != null) {
				String[] pair = line.split(",");
				body.add(Double.parseDouble(pair[0]));
				brain.add(Double.parseDouble(pair[1]));
			}
		} catch (IOException e) {
			System.err.println("IO Exception thrown.");
			e.printStackTrace();
			return;
		}

		if (FLAG == 100) {
			double body_mean = getMean(body);
			double body_stdDev = getStdDev(body, body_mean);
			double brain_mean = getMean(brain);
			double brain_stdDev = getStdDev(body, brain_mean);
			System.out.println(body.size());
			System.out.println(df.format(body_mean) + " " + df.format(body_stdDev));
			System.out.println(df.format(brain_mean) + " " + df.format(brain_stdDev));
		} else if (FLAG == 200) {
			double b0 = Double.parseDouble(args[1]);
			double b1 = Double.parseDouble(args[2]);
			System.out.println(df.format(MSE(body, brain, b0, b1)));
		} else if (FLAG == 300) {
			double b0 = Double.parseDouble(args[1]);
			double b1 = Double.parseDouble(args[2]);
			double b0_MSE = 0.0;
			double b1_MSE = 0.0;
			for (int i = 0; i < body.size(); i++) {
				b0_MSE += b0 + b1 * body.get(i) - brain.get(i);
				b1_MSE += (b0 + b1 * body.get(i) - brain.get(i)) * body.get(i);
			}
			b0_MSE = 2 * b0_MSE / body.size();
			b1_MSE = 2 * b1_MSE / body.size();

			System.out.println(df.format(b0_MSE));
			System.out.println(df.format(b1_MSE));
		} else if (FLAG == 400) {
			double n = Double.parseDouble(args[1]);
			int T = Integer.parseInt(args[2]);

			double b0 = 0.0;
			double b1 = 0.0;
			for (int t = 1; t <= T; t++) {
				double b0_MSE = 0.0;
				double b1_MSE = 0.0;
				for (int i = 0; i < body.size(); i++) {
					b0_MSE += b0 + b1 * body.get(i) - brain.get(i);
					b1_MSE += (b0 + b1 * body.get(i) - brain.get(i)) * body.get(i);
				}
				b0 -= n * (2 * b0_MSE / body.size());
				b1 -= n * (2 * b1_MSE / body.size());

				System.out.println(
						t + " " + df.format(b0) + " " + df.format(b1) + " " + df.format(MSE(body, brain, b0, b1)));
			}

		} else if (FLAG == 500) {
			double body_mean = getMean(body);
			double brain_mean = getMean(brain);

			double cfb1 = ClosedFormB1(body, brain, body_mean, brain_mean);
			double cfb0 = ClosedFormB0(body_mean, brain_mean, cfb1);
			System.out.println(cfb0 + " " + cfb1 + " " + MSE(body, brain, cfb0, cfb1));
		} else if (FLAG == 600) {
			double _body = Double.parseDouble(args[1]);

			double body_mean = getMean(body);
			double brain_mean = getMean(brain);
			double cfb1 = ClosedFormB1(body, brain, body_mean, brain_mean);
			double cfb0 = ClosedFormB0(body_mean, brain_mean, cfb1);

			double _brain = cfb0 + cfb1 * _body;
			System.out.println(df.format(_brain));
		} else if (FLAG == 700) {
			// Git merge
		} else if (FLAG == 800) {
			Random rng = new Random();
			double body_mean = getMean(body);
			double body_std = getStdDev(body, body_mean);

			double n = Double.parseDouble(args[1]);
			int T = Integer.parseInt(args[2]);

			double b0 = 0.0;
			double b1 = 0.0;
			for (int t = 1; t <= T; t++) {
				int randJ = rng.nextInt(body.size());
				double randBodyStd = (body.get(randJ) - body_mean) / body_std;
				double randBrain = brain.get(randJ);
				double b0_MSE = b0 + b1 * randBodyStd - randBrain;
				double b1_MSE = (b0 + b1 * randBodyStd - randBrain) * randBodyStd;
				b0 -= n * (2 * b0_MSE / body.size());
				b1 -= n * (2 * b1_MSE / body.size());

				System.out.println(
						t + " " + df.format(b0) + " " + df.format(b1) + " " + df.format(MSE(body, brain, b0, b1)));
			}
		}

	}

	private static double getMean(ArrayList<Double> items) {
		double sum = 0.0;
		for (double item : items) {
			sum += item;
		}
		return sum / items.size();
	}

	private static double getStdDev(ArrayList<Double> items, double mean) {
		double stdDev = 0.0;
		for (double item : items) {
			stdDev += Math.pow(item - mean, 2);
		}
		return Math.sqrt(stdDev / (items.size() - 1));
	}

	private static double MSE(ArrayList<Double> x, ArrayList<Double> y, double b0, double b1) {
		if (x.size() != y.size()) {
			System.err.println("MSE lists are different sizes!");
			return 0.0;
		}

		double sum = 0.0;
		for (int i = 0; i < x.size(); i++) {
			sum += Math.pow(b0 + b1 * x.get(i) - y.get(i), 2);
		}
		return sum / x.size();
	}

	private static double ClosedFormB1(ArrayList<Double> x, ArrayList<Double> y, double x_mean, double y_mean) {
		double top = 0.0;
		double bot = 0.0;

		for (int i = 0; i < x.size(); i++) {
			double x_diff = x.get(i) - x_mean;
			top += x_diff * (y.get(i) - y_mean);
			bot += Math.pow(x_diff, 2);
		}

		return top / bot;
	}

	private static double ClosedFormB0(double x_mean, double y_mean, double cfb1) {
		return y_mean - cfb1 * x_mean;
	}
}