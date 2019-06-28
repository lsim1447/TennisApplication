package tennis.model;

import tennis.domain.TrainingDescription;
import tennis.domain.TrainingResultsData;

import java.util.List;

public class TrainingDetails {
    private List<TrainingResultsData> list;
    private TrainingDescription trainingDescription;

    public TrainingDetails() {
    }

    public TrainingDetails(List<TrainingResultsData> list, TrainingDescription trainingDescription) {
        this.list = list;
        this.trainingDescription = trainingDescription;
    }

    public List<TrainingResultsData> getList() {
        return list;
    }

    public void setList(List<TrainingResultsData> list) {
        this.list = list;
    }

    public TrainingDescription getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(TrainingDescription trainingDescription) {
        this.trainingDescription = trainingDescription;
    }
}
