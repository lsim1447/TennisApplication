package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Tourney;
import tennis.repository.TourneyRepository;

@Service
public class TourneyService {

    @Autowired
    private TourneyRepository tourneyRepository;

    public Tourney findTourneyById(String id){
        return tourneyRepository.findById(id).orElse(null);
    }

    public Iterable<Tourney> findAll(){ return tourneyRepository.findAll(); }

    public Tourney save(Tourney tourney){
        return tourneyRepository.save(tourney);
    }

    public Iterable<Tourney> saveAll(Iterable<Tourney> tourneys){ return tourneyRepository.saveAll(tourneys); }
}
