package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import tennis.domain.Player;
import org.springframework.stereotype.Service;
import tennis.repository.PlayerRepository;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Iterable<Player> findAll(){
        return  playerRepository.findAll();
    }

    public Player save(Player player){
        return playerRepository.save(player);
    }

    public Iterable<Player> saveAll(Iterable<Player> players){
        return playerRepository.saveAll(players);
    }

    public Player findPlayerById(String id){
        return playerRepository.findById(id).orElse(null);
    }

    public void deletePlayerById(String id){ playerRepository.deleteById(id); }

    public Player findByFirstNameLastName(String firstName, String lastName){
        return playerRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
