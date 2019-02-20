package tennis.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import tennis.domain.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, String> {
    Player findByFirst_nameAndLast_name(String firstName, String lastName);
}
