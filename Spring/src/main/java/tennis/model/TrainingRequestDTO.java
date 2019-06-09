package tennis.model;

public class TrainingRequestDTO {
    private String training_data_filename;
    private String weights_filename;
    private String biases_filename;

    public TrainingRequestDTO() { }

    public TrainingRequestDTO(String training_data_filename, String weights_filename, String biases_filename) {
        this.training_data_filename = training_data_filename;
        this.weights_filename = weights_filename;
        this.biases_filename = biases_filename;
    }

    public String getTraining_data_filename() {
        return training_data_filename;
    }

    public void setTraining_data_filename(String training_data_filename) {
        this.training_data_filename = training_data_filename;
    }

    public String getWeights_filename() {
        return weights_filename;
    }

    public void setWeights_filename(String weight_filename) {
        this.weights_filename = weight_filename;
    }

    public String getBiases_filename() {
        return biases_filename;
    }

    public void setBiases_filename(String biases_filename) {
        this.biases_filename = biases_filename;
    }
}
