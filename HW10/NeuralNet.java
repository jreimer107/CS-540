import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

class NeuralNet {

	static Layer input_layer = new Layer(4);
	static Layer hidden_layer = new Layer(2);
	static Layer output_layer = new Layer(1);

	public static void main(String[] args) {
		int flag = Integer.parseInt(args[0]);
		DecimalFormat df = new DecimalFormat("0.00000");

		int arg_index = 1;

		// Set initial weights based on args
		for (Perceptron hidden : hidden_layer) {
			for (Perceptron input : input_layer.All()) {
				input.SetWeight(hidden, Double.parseDouble(args[arg_index++]));
			}
		}
		for (Perceptron hidden : hidden_layer.All()) {
			hidden.SetWeight(output_layer.get(0), Double.parseDouble(args[arg_index++]));
		}

		ArrayList<Double[]> training_inputs = new ArrayList<Double[]>();
		ArrayList<Double[]> training_outputs = new ArrayList<Double[]>();
		ArrayList<Double[]> eval_inputs = new ArrayList<Double[]>();
		ArrayList<Double[]> eval_outputs = new ArrayList<Double[]>();
		if (flag < 500) {
			Double[] arg_inputs = new Double[input_layer.size()];
			for (int i = 0; i < input_layer.size(); i++) {
				arg_inputs[i] = Double.parseDouble(args[arg_index++]);
			}
			training_inputs.add(arg_inputs);
			Double[] single_output = { Double.parseDouble(args[args.length - 1]) };
			training_outputs.add(single_output);
		} else {
			// Read in training data
			try (BufferedReader br = new BufferedReader(new FileReader("train.csv"))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] data = line.split(",");
					Double[] input = new Double[input_layer.size()];
					Double[] output = new Double[output_layer.size()];
					int data_pointer = 0;
					for (int i = 0; i < input_layer.size(); i++) {
						input[i] = Double.parseDouble(data[data_pointer++]);
					}
					for (int i = 0; i < output_layer.size(); i++) {
						output[i] = Double.parseDouble(data[data_pointer++]);
					}
					training_inputs.add(input);
					training_outputs.add(output);
				}
			} catch (IOException e) {
				System.err.println("IO Exception thrown.");
				e.printStackTrace();
				return;
			}

			// Read in eval data
			String eval_file = "eval.csv";
			if (flag == 600) {
				eval_file = "test.csv";
			}
			try (BufferedReader br = new BufferedReader(new FileReader(eval_file))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] data = line.split(",");
					Double[] input = new Double[input_layer.size()];
					Double[] output = new Double[output_layer.size()];
					int data_pointer = 0;
					for (int i = 0; i < input_layer.size(); i++) {
						input[i] = Double.parseDouble(data[data_pointer++]);
					}
					for (int i = 0; i < output_layer.size(); i++) {
						output[i] = Double.parseDouble(data[data_pointer++]);
					}
					eval_inputs.add(input);
					eval_outputs.add(output);
				}
			} catch (IOException e) {
				System.err.println("IO Exception thrown.");
				e.printStackTrace();
				return;
			}
		}

		// Train
		for (int i = 0; i < training_inputs.size(); i++) {
			Run(training_inputs.get(i), training_outputs.get(i));

			if (flag <= 400) {
				break;
			}

			// Adjust weights based on errors
			double n = Double.parseDouble(args[args.length - 1]);
			output_layer.AdjustWeights(n);
			hidden_layer.AdjustWeights(n);

			if (flag == 500) {
				double e_eval = 0;
				for (int j = 0; j < eval_inputs.size(); j++) {
					Run(eval_inputs.get(j), eval_outputs.get(j));
					for (int k = 0; k < output_layer.size(); k++) {
						e_eval += GetError(eval_outputs.get(j)[k], output_layer.get(k).activation);
					}
				}

				// Printing
				ArrayList<String> weights = new ArrayList<String>();
				for (Perceptron hidden : hidden_layer) {
					for (Perceptron input : input_layer.All()) {
						weights.add(df.format(input.GetWeight(hidden)));
					}
				}
				for (Perceptron output : output_layer) {
					for (Perceptron hidden : hidden_layer.All()) {
						weights.add(df.format(hidden.GetWeight(output)));
					}
				}
				System.out.println(String.join(" ", weights));
				System.out.println(df.format(e_eval));
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
			ArrayList<String> output_errors = new ArrayList<String>(output_layer.size());
			for (Perceptron p : output_layer) {
				output_errors.add(df.format(p.error));
			}
			System.out.println(String.join(" ", output_errors));
		} else if (flag == 300) {
			ArrayList<String> hidden_errors = new ArrayList<String>(hidden_layer.size());
			for (Perceptron p : hidden_layer) {
				hidden_errors.add(df.format(p.error));
			}
			System.out.println(String.join(" ", hidden_errors));
		} else if (flag == 400) {
			for (Perceptron output : output_layer) {
				ArrayList<String> output_errors = new ArrayList<String>(hidden_layer.size());
				for (Perceptron hidden : hidden_layer.All()) {
					output_errors.add(df.format(output.weight_errors.get(hidden)));
				}
				System.out.println(String.join(" ", output_errors));
			}
			for (Perceptron hidden : hidden_layer) {
				ArrayList<String> hidden_errors = new ArrayList<String>(input_layer.size());
				for (Perceptron input : input_layer.All()) {
					hidden_errors.add(df.format(hidden.weight_errors.get(input)));
				}
				System.out.println(String.join(" ", hidden_errors));
			}
		} else if (flag == 600) {
			// Test
			int numCorrect = 0;
			int numTests = 0;
			for (int i = 0; i < eval_inputs.size(); i++) {
				Run(eval_inputs.get(i), eval_outputs.get(i));
				int expected = (int) (double) eval_outputs.get(i)[0];
				double confidence = output_layer.get(0).activation;
				int prediction = (int) Math.round(confidence);
				if (expected == prediction) {
					numCorrect++;
				}
				numTests++;
				System.out.println(expected + " " + prediction + " " + df.format(confidence));
			}

			//Print accuracy
			df = new DecimalFormat("0.00");
			System.out.println(df.format(numCorrect / (double) numTests));
		}
	}

	public static void Run(Double[] inputs, Double[] expected_outputs) {
		// Set inputs
		for (int i = 0; i < input_layer.size(); i++) {
			input_layer.get(i).activation = inputs[i];
		}

		// Activation calculation
		input_layer.UpdateActivations();
		hidden_layer.UpdateActivations();
		output_layer.UpdateActivations();

		// Set output errors based on expected value args
		for (int i = 0; i < output_layer.size(); i++) {
			Perceptron p = output_layer.get(i);
			p.error = OutputPDError(expected_outputs[i], p.activation);
		}

		// Calculate activation errors
		hidden_layer.UpdateErrors();
		input_layer.UpdateErrors();

		// Calculate Weight Errors
		output_layer.UpdateWeightErrors();
		hidden_layer.UpdateWeightErrors();
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
	public HashMap<Perceptron, Double> weight_errors;
	private HashSet<Perceptron> inputs;

	public Perceptron() {
		activation = 0.0;
		error = 0.0;
		weights = new HashMap<Perceptron, Double>();
		weight_errors = new HashMap<Perceptron, Double>();
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
			this.error += NeuralNet.HiddenPDError(p_next.error, this.activation, weight.getValue());
		}
	}

	public void UpdateWeightErrors() {
		for (Perceptron input : this.inputs) {
			this.weight_errors.put(input, this.error * input.activation);
		}
	}

	public void AdjustWeights(double n) {
		for (Perceptron input : this.inputs) {
			input.SetWeight(this, input.GetWeight(this) - n * this.weight_errors.get(input));
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

	public void UpdateErrors() {
		for (Perceptron p : this.All()) {
			p.UpdateError();
		}
	}

	public void UpdateWeightErrors() {
		for (Perceptron p : nodes) {
			p.UpdateWeightErrors();
		}
	}

	public int size() {
		return this.nodes.size();
	}

	public ArrayList<Double> GetErrors() {
		ArrayList<Double> errors = new ArrayList<Double>();
		for (Perceptron p : this.All()) {
			errors.add(p.error);
		}
		return errors;
	}

	public void AdjustWeights(double n) {
		for (Perceptron p : nodes) {
			p.AdjustWeights(n);
		}
	}
}