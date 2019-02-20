package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Match;
import tennis.repository.MatchRepository;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchesRepository;

    public Match findMatchById(String id){ return matchesRepository.findById(id).orElse(null); }

    public Iterable<Match> findAll(){ return matchesRepository.findAll(); }

    public Match save(Match match){ return matchesRepository.save(match); }

    public Iterable<Match> saveAll(Iterable<Match> matches) { return matchesRepository.saveAll(matches); }

    public void deleteMatchById(String id) { matchesRepository.deleteById(id); }
}
