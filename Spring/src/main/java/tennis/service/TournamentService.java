package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Tournament;
import tennis.repository.TournamentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

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

    public Iterable<Tournament> getTournamentsByPlayerName(String firstName, String lastName){
        List<Tournament> tournaments = (List<Tournament>)tournamentRepository.findAll();
        tournaments.stream()
                .filter(tournament -> {

                     return tournament.getPlayer().getFirst_name().equals(firstName) &&
                            tournament.getPlayer().getLast_name().equals(lastName);
                })
                .collect(Collectors.toList());
        return null;
    }

}
