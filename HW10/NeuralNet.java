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

		if (flag == 100) {
			// Arg parsing
			int arg_index = 1;
			for (Perceptron hidden : hidden_layer) {
				for (Perceptron input : input_layer.All()) {
					input.SetWeight(hidden, Double.parseDouble(args[arg_index++]));
				}
			}
			for (Perceptron hidden : hidden_layer.All()) {
				hidden.SetWeight(output_layer.get(0), Double.parseDouble(args[arg_index++]));
			}
			for (Perceptron input : input_layer) {
				input.SetActivation(Double.parseDouble(args[arg_index++]));
			}

			// for (int i = 1; i < hidden_layer.length; i++) {
			// for (Perceptron input : input_layer) {
			// input.SetWeight(hidden_layer[i], Double.parseDouble(args[arg_index++]));
			// }
			// }
			// for (Perceptron hidden : hidden_layer) {
			// hidden.SetWeight(output_layer, Double.parseDouble(args[arg_index++]));
			// }
			// for (int i = 1; i < input_layer.length; i++) {
			// input_layer[i].SetActivation(Double.parseDouble(args[arg_index++]));
			// }

			// Activation calculation
			input_layer.UpdateActivations();
			hidden_layer.UpdateActivations();
			output_layer.UpdateActivations();
			ArrayList<String> hidden_activations = new ArrayList<String>(hidden_layer.size());
			for (Perceptron hidden : hidden_layer) {
				hidden_activations.add(df.format(hidden.GetActivation()));
			}
			System.out.println(String.join(" ", hidden_activations));
			System.out.println(df.format(output_layer.get(0).GetActivation()));
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
		// this.SetActivation(NeuralNet.sigmoid(newActivation));
		this.SetActivation(newActivation);
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
	private ArrayList<Perceptron> nodes;
	private Perceptron bias;

	public Layer(int size) {
		nodes = new ArrayList<Perceptron>(size);
		for (int i = 0; i < size; i++)
			nodes.add(new Perceptron());
		bias = new Perceptron();
		bias.SetActivation(1.0);
	}

	public ArrayList<Perceptron> All() {
		ArrayList<Perceptron> all = new ArrayList<Perceptron>(this.nodes);
		all.add(this.bias);
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

	public void SetBias(double newBias) {
		this.bias.SetActivation(newBias);
	}

	public int size() {
		return this.nodes.size();
	}
}