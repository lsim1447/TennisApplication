package tennis.model;

import java.util.List;

public class TrainData {
    private List<Double> inputs;
    private List<Integer> outputs;

    public TrainData() { }

    public TrainData(List<Double> inputs, List<Integer> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public List<Double> getInputs() {
        return inputs;
    }

    public void setInputs(List<Double> inputs) {
        this.inputs = inputs;
    }

    public List<Integer> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Integer> outputs) {
        this.outputs = outputs;
    }
}
