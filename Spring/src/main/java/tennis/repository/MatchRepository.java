package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.Match;
import tennis.domain.Player;

@Repository
public interface MatchRepository extends CrudRepository<Match, String> {
    Iterable<Match> findAllByWinnerPlayerOrLoserPlayer(Player winnerPlayer, Player loserPlayer);
    Iterable<Match> findAllByWinnerPlayerAndLoserPlayer(Player winnerPlayer, Player loserPlayer);
}
