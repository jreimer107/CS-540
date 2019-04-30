import java.text.DecimalFormat;
import java.util.*;

class NeuralNet {

	// static Perceptron[] input_layer = new Perceptron[5];
	// static Perceptron[] hidden_layer = new Perceptron[3];
	// static Perceptron output_layer = new Perceptron();

	static Layer input_layer = new Layer(4);
	static Layer hidden_layer = new Layer(2);
	static Layer output_layer = new Layer(1);

	public static void main(String[] args) {
		int flag = Integer.parseInt(args[0]);
		DecimalFormat df = new DecimalFormat("0.00000");

		int arg_index = 1;
		double[] output_errors = new double[output_layer.size()];
		double[] hidden_errors = new double[hidden_layer.size()];
		double[] input_errors = new double[input_layer.size()];

		// Arg parsing
		for (Perceptron hidden : hidden_layer) {
			for (Perceptron input : input_layer.All()) {
				input.SetWeight(hidden, Double.parseDouble(args[arg_index++]));
			}
		}
		for (Perceptron hidden : hidden_layer.All()) {
			hidden.SetWeight(output_layer.get(0), Double.parseDouble(args[arg_index++]));
		}
		for (Perceptron input : input_layer) {
			input.activation = Double.parseDouble(args[arg_index++]);
		}

		// Activation calculation
		input_layer.UpdateActivations();
		hidden_layer.UpdateActivations();
		output_layer.UpdateActivations();

		// Calculate errors on output nodes
		for (int i = 0; i < output_layer.size(); i++) {
			output_errors[i] = OutputPDError(Double.parseDouble(args[args.length - 1]), output_layer.get(i).activation);
		}

		// Calculate errors on hidden nodes
		for (int i = 0; i < output_layer.size(); i++) {
			for (int j = 0; j < hidden_layer.size(); j++) {
				hidden_errors[j] += HiddenPDError(output_errors[i], hidden_layer.get(j).activation,
						hidden_layer.get(j).GetWeight(output_layer.get(i)));
			}
		}

		// Printing
		if (flag == 100) {
			ArrayList<String> hidden_activations = new ArrayList<String>(hidden_layer.size());
			for (Perceptron hidden : hidden_layer) {
				hidden_activations.add(df.format(hidden.activation));
			}
			System.out.println(String.join(" ", hidden_activations));
			System.out.println(df.format(output_layer.get(0).activation));
		} else if (flag == 200) {
			String[] output_errors_f = new String[output_errors.length];
			for (int i = 0; i < output_errors.length; i++) {
				output_errors_f[i] = df.format(output_errors[i]);
			}
			System.out.println(String.join(" ", output_errors_f));
		} else if (flag == 300) {
			String[] hidden_errors_f = new String[hidden_errors.length];
			for (int i = 0; i < hidden_errors.length; i++) {
				hidden_errors_f[i] = df.format(hidden_errors[i]);
			}
			System.out.println(String.join(" ", hidden_errors_f));
		} else if (flag == 400) {

		}

		/*
		 * // if (flag == 100) { // // Arg parsing // int arg_index = 1; // for
		 * (Perceptron hidden : hidden_layer) { // for (Perceptron input :
		 * input_layer.All()) { // input.SetWeight(hidden,
		 * Double.parseDouble(args[arg_index++])); // } // } // for (Perceptron hidden :
		 * hidden_layer.All()) { // hidden.SetWeight(output_layer.get(0),
		 * Double.parseDouble(args[arg_index++])); // } // for (Perceptron input :
		 * input_layer) { // input.SetActivation(Double.parseDouble(args[arg_index++]));
		 * // }
		 * 
		 * // // Activation calculation // input_layer.UpdateActivations(); //
		 * hidden_layer.UpdateActivations(); // output_layer.UpdateActivations(); //
		 * ArrayList<String> hidden_activations = new //
		 * ArrayList<String>(hidden_layer.size()); // for (Perceptron hidden :
		 * hidden_layer) { // hidden_activations.add(df.format(hidden.activation)); // }
		 * // System.out.println(String.join(" ", hidden_activations)); //
		 * System.out.println(df.format(output_layer.get(0).activation)); // } else if
		 * (flag == 200) { // // Arg parsing // int arg_index = 1; // for (Perceptron
		 * hidden : hidden_layer) { // for (Perceptron input : input_layer.All()) { //
		 * input.SetWeight(hidden, Double.parseDouble(args[arg_index++])); // } // } //
		 * for (Perceptron hidden : hidden_layer.All()) { //
		 * hidden.SetWeight(output_layer.get(0), Double.parseDouble(args[arg_index++]));
		 * // } // for (Perceptron input : input_layer) { //
		 * input.SetActivation(Double.parseDouble(args[arg_index++])); // }
		 * 
		 * // // Activation calculation // input_layer.UpdateActivations(); //
		 * hidden_layer.UpdateActivations(); // output_layer.UpdateActivations(); //
		 * double error = OutputPDError(Double.parseDouble(args[arg_index++]), //
		 * output_layer.get(0).activation);
		 * 
		 * // System.out.println(df.format(error)); // } else if (flag == 300) { // //
		 * Arg parsing // int arg_index = 1; // for (Perceptron hidden : hidden_layer) {
		 * // for (Perceptron input : input_layer.All()) { // input.SetWeight(hidden,
		 * Double.parseDouble(args[arg_index++])); // } // } // for (Perceptron hidden :
		 * hidden_layer.All()) { // hidden.SetWeight(output_layer.get(0),
		 * Double.parseDouble(args[arg_index++])); // } // for (Perceptron input :
		 * input_layer) { // input.SetActivation(Double.parseDouble(args[arg_index++]));
		 * // }
		 * 
		 * // // Activation calculation // input_layer.UpdateActivations(); //
		 * hidden_layer.UpdateActivations(); // output_layer.UpdateActivations(); //
		 * double output_error = OutputPDError(Double.parseDouble(args[arg_index++]), //
		 * output_layer.get(0).activation);
		 * 
		 * // double[] hidden_errors = new double[hidden_layer.size()]; // String[]
		 * hidden_error_formatted = new String[hidden_layer.size()]; // for (int i = 0;
		 * i < hidden_layer.size(); i++) { // hidden_errors[i] =
		 * HiddenPDError(output_error, // hidden_layer.get(i).activation, //
		 * hidden_layer.get(i).GetWeight(output_layer.get(0))); //
		 * hidden_error_formatted[i] = df.format(hidden_errors[i]); // } //
		 * System.out.println(String.join(" ", hidden_error_formatted)); // }
		 */

	}

	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}

	public static double GetError(double expected, double actual) {
		return Math.pow(expected - actual, 2) / 2;
	}

	public static double OutputPDError(double expected, double actual) {
		return (actual - expected) * actual * (1 - actual);
	}

	public static double HiddenPDError(double nextLayerError, double actual, double weight) {
		return nextLayerError * weight * actual * (1 - actual);
	}
}

class Perceptron {
	public double activation;
	public double error;
	private HashMap<Perceptron, Double> weights;
	private HashSet<Perceptron> inputs;

	public Perceptron() {
		activation = 0.0;
		error = 0.0;
		weights = new HashMap<Perceptron, Double>();
		inputs = new HashSet<Perceptron>();
	}

	public double GetWeight(Perceptron p) {
		return this.weights.get(p);
	}

	public void UpdateActivation() {
		if (this.inputs.size() == 0) {
			return;
		}

		double newActivation = 0.0;
		for (Perceptron input : inputs) {
			newActivation += input.activation * input.GetWeight(this);
		}
		this.activation = NeuralNet.sigmoid(newActivation);
	}

	public void SetWeight(Perceptron p, double weight) {
		this.weights.put(p, weight);
		p.AddInput(this);
	}

	public void AddInput(Perceptron p) {
		this.inputs.add(p);
	}

	public void UpdateError() {
		this.error = 0;
		for (Map.Entry<Perceptron, Double> weight : this.weights.entrySet()) {
			Perceptron p_next = weight.getKey();
			this.error += NeuralNet.HiddenPDError(p_next.error, p_next.activation, weight.getValue());
		}
	}
}

class Layer implements Iterable<Perceptron> {
	private ArrayList<Perceptron> nodes;
	private Perceptron bias;

	public Layer(int size) {
		nodes = new ArrayList<Perceptron>(size);
		for (int i = 0; i < size; i++)
			nodes.add(new Perceptron());
		bias = new Perceptron();
		bias.activation = 1.0;
	}

	public ArrayList<Perceptron> All() {
		ArrayList<Perceptron> all = new ArrayList<Perceptron>(this.nodes);
		all.add(0, this.bias);
		return all;
	}

	public Perceptron get(int index) {
		return nodes.get(index);
	}

	public Iterator<Perceptron> iterator() {
		return nodes.iterator();
	}

	public void UpdateActivations() {
		for (Perceptron p : nodes) {
			p.UpdateActivation();
		}
	}

	// public void Link(Layer next) {
	// for (Perceptron p : next) {
	// bias.SetWeight(p, weight);
	// }
	// }

	public void UpdateErrors() {
		for (Perceptron p : this.All()) {
			p.UpdateError();
		}
		// for (Perceptron p : this.All()) {
		// p.error = 0;
		// for (Perceptron p_next : next) {
		// p.error += NeuralNet.HiddenPDError(p_next.error, p_next.activation,
		// p.GetWeight(p_next));
		// }
		// }
	}

	public void SetBias(double newBias) {
		this.bias.activation = newBias;
	}

	public int size() {
		return this.nodes.size();
	}
}