package tennis.model;

import java.util.ArrayList;
import java.util.List;

public class TrainingResponseDTO {

    private String training_data_filename;
    private double highest_percentage;
    private List<Double> history = new ArrayList<>();

    public TrainingResponseDTO() { }

    public TrainingResponseDTO(String training_data_filename, double highest_percentage, List<Double> history) {
        this.training_data_filename = training_data_filename;
        this.highest_percentage = highest_percentage;
        this.history = history;
    }

    public String getTraining_data_filename() {
        return training_data_filename;
    }

    public void setTraining_data_filename(String training_data_filename) {
        this.training_data_filename = training_data_filename;
    }

    public double getHighest_percentage() {
        return highest_percentage;
    }

    public void setHighest_percentage(double highest_percentage) {
        this.highest_percentage = highest_percentage;
    }

    public List<Double> getHistory() {
        return history;
    }

    public void setHistory(List<Double> history) {
        this.history = history;
    }
}
