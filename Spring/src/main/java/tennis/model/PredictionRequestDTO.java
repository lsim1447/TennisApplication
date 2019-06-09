package tennis.model;

import java.util.List;

public class PredictionRequestDTO {
    private String firstPlayerSlug;
    private String secondPlayerSlug;

    private String weights_filename;
    private String biases_filename;

    private List<Double> inputs;

    public PredictionRequestDTO() { }

    public PredictionRequestDTO(String firstPlayerSlug, String secondPlayerSlug, String weights_filename, String biases_filename, List<Double> inputs) {
        this.firstPlayerSlug = firstPlayerSlug;
        this.secondPlayerSlug = secondPlayerSlug;
        this.weights_filename = weights_filename;
        this.biases_filename = biases_filename;
        this.inputs = inputs;
    }

    public String getFirstPlayerSlug() {
        return firstPlayerSlug;
    }

    public void setFirstPlayerSlug(String firstPlayerSlug) {
        this.firstPlayerSlug = firstPlayerSlug;
    }

    public String getSecondPlayerSlug() {
        return secondPlayerSlug;
    }

    public void setSecondPlayerSlug(String secondPlayerSlug) {
        this.secondPlayerSlug = secondPlayerSlug;
    }

    public String getWeights_filename() {
        return weights_filename;
    }

    public void setWeights_filename(String weights_filename) {
        this.weights_filename = weights_filename;
    }

    public String getBiases_filename() {
        return biases_filename;
    }

    public void setBiases_filename(String biases_filename) {
        this.biases_filename = biases_filename;
    }

    public List<Double> getInputs() {
        return inputs;
    }

    public void setInputs(List<Double> inputs) {
        this.inputs = inputs;
    }
}
