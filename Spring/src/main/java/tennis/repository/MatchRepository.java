package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.Match;

@Repository
public interface MatchRepository extends CrudRepository<Match, String> {

}
