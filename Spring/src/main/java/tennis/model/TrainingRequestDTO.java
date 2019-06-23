package tennis.model;

public class TrainingRequestDTO {
    private String training_data_filename;
    private String weights_filename;
    private String biases_filename;
    private boolean with_new_settings;
    private int nr_of_input_data;

    public TrainingRequestDTO() { }

    public TrainingRequestDTO(String training_data_filename, String weights_filename, String biases_filename, boolean with_new_settings, int nr_of_input_data) {
        this.training_data_filename = training_data_filename;
        this.weights_filename = weights_filename;
        this.biases_filename = biases_filename;
        this.with_new_settings = with_new_settings;
        this.nr_of_input_data = nr_of_input_data;
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

    public void setWeights_filename(String weights_filename) {
        this.weights_filename = weights_filename;
    }

    public String getBiases_filename() {
        return biases_filename;
    }

    public void setBiases_filename(String biases_filename) {
        this.biases_filename = biases_filename;
    }

    public boolean isWith_new_settings() {
        return with_new_settings;
    }

    public void setWith_new_settings(boolean with_new_settings) {
        this.with_new_settings = with_new_settings;
    }

    public int getNr_of_input_data() {
        return nr_of_input_data;
    }

    public void setNr_of_input_data(int nr_of_input_data) {
        this.nr_of_input_data = nr_of_input_data;
    }
}
