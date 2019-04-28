import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

class NeuralNet {

	static Perceptron[] input_layer = new Perceptron[5];
	static Perceptron[] hidden_layer = new Perceptron[3];
	static Perceptron output_layer = new Perceptron();

	public static void main(String[] args) {
		for (int i = 0; i < input_layer.length; i++)
			input_layer[i] = new Perceptron();
		for (int i = 0; i < hidden_layer.length; i++)
			hidden_layer[i] = new Perceptron();

		input_layer[0].SetActivation(1);
		hidden_layer[0].SetActivation(1);

		int flag = Integer.parseInt(args[0]);
		DecimalFormat df = new DecimalFormat("0.00000");

		if (flag == 100) {
			// Arg parsing
			int arg_index = 1;
			for (int i = 1; i < hidden_layer.length; i++) {
				for (Perceptron input : input_layer) {
					input.SetWeight(hidden_layer[i], Double.parseDouble(args[arg_index++]));
				}
			}
			for (Perceptron hidden : hidden_layer) {
				hidden.SetWeight(output_layer, Double.parseDouble(args[arg_index++]));
			}
			for (int i = 1; i < input_layer.length; i++) {
				input_layer[i].SetActivation(Double.parseDouble(args[arg_index++]));
			}

			// Activation calculation
			ArrayList<String> hidden_activations = new ArrayList<String>(hidden_layer.length);
			for (Perceptron hidden : hidden_layer) {
				hidden.UpdateActivation();
				hidden_activations.add(df.format(hidden.GetActivation()));
			}
			output_layer.UpdateActivation();

			System.out.println(String.join(" ", hidden_activations));
			System.out.println(df.format(output_layer.GetActivation()));
		}
	}

	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
}

class Perceptron {
	private double activation;
	private HashMap<Perceptron, Double> weights;
	private HashSet<Perceptron> inputs;

	public Perceptron() {
		activation = 0.0;
		weights = new HashMap<Perceptron, Double>();
		inputs = new HashSet<Perceptron>();
	}

	public double GetWeight(Perceptron p) {
		return this.weights.get(p);
	}

	public double GetActivation() {
		return this.activation;
	}

	public void UpdateActivation() {
		if (this.inputs.size() == 0) {
			return;
		}

		double newActivation = 0.0;
		for (Perceptron input : inputs) {
			newActivation += input.GetActivation() * input.GetWeight(this);
		}
		this.SetActivation(NeuralNet.sigmoid(newActivation));
	}

	public void SetActivation(double a) {
		this.activation = a;
	}

	public void SetWeight(Perceptron p, double weight) {
		this.weights.put(p, weight);
		p.AddInput(this);
	}

	public void AddInput(Perceptron p) {
		this.inputs.add(p);
	}
}

class Layer implements Iterable<Perceptron> {
	private HashSet<Perceptron> nodes;
	private Perceptron bias;

	public Layer(int size) {
		nodes = new HashSet<Perceptron>(size);
		for (int i = 0; i < size; i++)
			nodes.add(new Perceptron());
		bias = new Perceptron();
		bias.SetActivation(1.0);
	}

	public HashSet<Perceptron> GetAll() {
		HashSet<Perceptron> all = new HashSet<Perceptron>(this.nodes);
		all.add(this.bias);
		return all;
	}

	public Iterator<Perceptron> iterator() {
		return nodes.iterator();
	}

	public void UpdateActivations() {
		for (Perceptron p : nodes) {
			p.UpdateActivation();
		}
	}

	public void SetBias(double newBias) {
		this.bias.SetActivation(newBias);
	}
}