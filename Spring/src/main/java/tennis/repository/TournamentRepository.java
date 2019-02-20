package tennis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tennis.domain.Tournament;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, String> {
}
