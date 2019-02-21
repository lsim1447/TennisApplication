package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.Match;
import tennis.domain.Stats;

@Repository
public interface StatsRepository extends CrudRepository<Stats, String> {
    Stats findByMatch(Match match);
}
