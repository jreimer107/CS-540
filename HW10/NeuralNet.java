import java.text.DecimalFormat;

class NeuralNet {

	static double[][] input_weights = new double[2][5];
	static double[] hidden_weights = new double[3];
	static double[] input_activations = new double[5];
	static double[] hidden_activations = new double[3];

	public static void main(String[] args) {
		// Biases
		input_activations[0] = 1;
		hidden_activations[0] = 1;

		int flag = Integer.parseInt(args[0]);
		DecimalFormat df = new DecimalFormat("0.00000");

		if (flag == 100) {
			// Arg parsing
			int arg_index = 1;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 5; j++) {
					input_weights[i][j] = Double.parseDouble(args[arg_index++]);
				}
			}
			for (int i = 0; i < 3; i++) {
				hidden_weights[i] = Double.parseDouble(args[arg_index++]);
			}
			for (int i = 1; i < 5; i++) {
				input_activations[i] = Double.parseDouble(args[arg_index++]);
			}

			// Activation calculation
			double output_activation = 0;
			for (int i = 1; i < hidden_activations.length; i++) {
				for (int j = 0; j < input_activations.length; j++) {
					hidden_activations[i] += input_weights[i - 1][j] * input_activations[j];
				}
				hidden_activations[i] = sigmoid(hidden_activations[i]);
				System.out.print(df.format(hidden_activations[i]));
			}
			System.out.println();
			for (int i = 0; i < hidden_activations.length; i++) {
				output_activation += hidden_weights[i] * hidden_activations[i];
			}
			output_activation = sigmoid(output_activation);
			System.out.println(df.format(output_activation));
		}
	}

	private static double getMean(double[] items) {
		double sum = 0.0;
		for (double item : items) {
			sum += item;
		}
		return sum / items.length;
	}

	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
}