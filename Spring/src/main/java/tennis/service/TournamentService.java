package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Player;
import tennis.domain.Tournament;
import tennis.repository.PlayerRepository;
import tennis.repository.TournamentRepository;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Tournament findTournamentById(String id){
        return tournamentRepository.findById(id).orElse(null);
    }

    public Iterable<Tournament> findAll(){
        return tournamentRepository.findAll();
    }

    public Tournament save(Tournament tournament){
        return tournamentRepository.save(tournament);
    }

    public Iterable<Tournament> saveAll(Iterable<Tournament> tournaments){ return tournamentRepository.saveAll(tournaments); }

    public void deleteTournamentById(String id){ tournamentRepository.deleteById(id); }

    public Iterable<Tournament> getWonTournamentsByPlayerName(String firstName, String lastName){
        Player player = playerRepository.findByFirstNameAndLastName(firstName, lastName);
        return tournamentRepository.findAllByPlayer(player);
    }
}
