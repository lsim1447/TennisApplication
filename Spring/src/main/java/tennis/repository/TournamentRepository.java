package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.Player;
import tennis.domain.Tournament;
import tennis.domain.Tourney;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, String> {
    Iterable<Tournament> findAllByPlayer(Player player);
    Iterable<Tournament> findAllByTourney(Tourney tourney);
}
