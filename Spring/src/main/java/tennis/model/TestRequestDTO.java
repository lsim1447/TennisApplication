package tennis.model;

public class TestRequestDTO {
    private String test_data_filename;
    private String weights_filename;
    private String biases_filename;
    private int nr_of_test_input_data;

    public TestRequestDTO() {
    }

    public TestRequestDTO(String test_data_filename, String weights_filename, String biases_filename, int nr_of_test_input_data) {
        this.test_data_filename = test_data_filename;
        this.weights_filename = weights_filename;
        this.biases_filename = biases_filename;
        this.nr_of_test_input_data = nr_of_test_input_data;
    }

    public String getTest_data_filename() {
        return test_data_filename;
    }

    public void setTest_data_filename(String test_data_filename) {
        this.test_data_filename = test_data_filename;
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

    public int getNr_of_test_input_data() {
        return nr_of_test_input_data;
    }

    public void setNr_of_test_input_data(int nr_of_test_input_data) {
        this.nr_of_test_input_data = nr_of_test_input_data;
    }
}
