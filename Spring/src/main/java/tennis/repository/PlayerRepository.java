package tennis.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import tennis.domain.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, String> {
    Player findByFirstNameAndLastName(String firstName, String lastName);
}
