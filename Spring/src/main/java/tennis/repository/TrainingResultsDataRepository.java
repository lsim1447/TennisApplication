package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.TrainingDescription;
import tennis.domain.TrainingResultsData;

@Repository
public interface TrainingResultsDataRepository extends CrudRepository<TrainingResultsData, String> {
    Iterable<TrainingResultsData> findAllByTrainingDescription(TrainingDescription trainingDescription);
}
