package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.TrainingDescription;
import tennis.repository.TrainingDescriptionRepository;

import java.util.List;

@Service
public class TrainingDescriptionService {
    @Autowired
    private TrainingDescriptionRepository descriptionRepository;

    public TrainingDescription save(TrainingDescription trainingDescription){
        return descriptionRepository.save(trainingDescription);
    }

    public List<TrainingDescription> findAll(){
        return (List<TrainingDescription>) descriptionRepository.findAll();
    }

    public TrainingDescription findById(String id){
        return descriptionRepository.findById(id).orElse(null);
    }
}
