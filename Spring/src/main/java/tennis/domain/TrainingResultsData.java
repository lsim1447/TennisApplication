package tennis.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TrainingResultsData {
    @Id
    @SequenceGenerator(name = "seq_gen")
    @GeneratedValue(generator = "seq_gen")
    @Column(name = "training_results_data_id")
    private long training_results_data_id;

    @ManyToOne
    @JoinColumn(name="training_id")
    private TrainingDescription trainingDescription;

    private double percentage;

    public TrainingResultsData() {
    }

    public TrainingResultsData(long training_results_data_id, TrainingDescription trainingDescription, double percentage) {
        this.training_results_data_id = training_results_data_id;
        this.trainingDescription = trainingDescription;
        this.percentage = percentage;
    }

    public long getTraining_results_data_id() {
        return training_results_data_id;
    }

    public void setTraining_results_data_id(long training_results_data_id) {
        this.training_results_data_id = training_results_data_id;
    }

    public TrainingDescription getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(TrainingDescription trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
