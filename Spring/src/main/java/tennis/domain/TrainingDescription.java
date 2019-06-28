package tennis.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class TrainingDescription {
    @Id
    private String training_id;
    private String description;
    private String date;
    private double highestRate;

    public TrainingDescription() {
    }

    public TrainingDescription(String training_id, String description, String date, double highestRate) {
        this.training_id = training_id;
        this.description = description;
        this.date = date;
        this.highestRate = highestRate;
    }

    public String getTraining_id() {
        return training_id;
    }

    public void setTraining_id(String training_id) {
        this.training_id = training_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getHighestRate() {
        return highestRate;
    }

    public void setHighestRate(double highestRate) {
        this.highestRate = highestRate;
    }
}
