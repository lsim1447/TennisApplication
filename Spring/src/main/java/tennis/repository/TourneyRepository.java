package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.Tourney;

@Repository
public interface TourneyRepository extends CrudRepository<Tourney, String> {
    Tourney findBySlug(String slug);
}
