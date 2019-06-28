package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.TrainingDescription;

@Repository
public interface TrainingDescriptionRepository extends CrudRepository<TrainingDescription, String> {

}
