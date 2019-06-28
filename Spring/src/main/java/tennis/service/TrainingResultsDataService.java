package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.TrainingDescription;
import tennis.domain.TrainingResultsData;
import tennis.repository.TrainingResultsDataRepository;

import java.util.List;

@Service
public class TrainingResultsDataService {

    @Autowired private TrainingResultsDataRepository trainingResultsDataRepository;

    public TrainingResultsData save(TrainingResultsData trainingResultsData){
        return trainingResultsDataRepository.save(trainingResultsData);
    }

    public Iterable<TrainingResultsData> findAll(){
        return trainingResultsDataRepository.findAll();
    }

    public List<TrainingResultsData> findAllByTrainingDescription(TrainingDescription trainingDescription){
        return (List<TrainingResultsData>) trainingResultsDataRepository.findAllByTrainingDescription(trainingDescription);
    }
}
