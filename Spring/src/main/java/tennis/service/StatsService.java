package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Match;
import tennis.domain.Stats;
import tennis.repository.MatchRepository;
import tennis.repository.StatsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsService {

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private MatchRepository matchRepository;

    public Stats findStatsByMatchId(String id){ return statsRepository.findById(id).orElse(null); }

    public Iterable<Stats> findAll(){ return statsRepository.findAll(); }

    public Stats save(Stats stats){ return statsRepository.save(stats); }

    public Iterable<Stats> saveAll(Iterable<Stats> stats) { return statsRepository.saveAll(stats); }

    public void deleteByMatchId(String id) { statsRepository.deleteById(id); }

    public Iterable<Stats> findAllStatsByMatchIds(List<Match> matchList){
        return matchList.stream()
                .map(match -> statsRepository.findByMatch(match))
                .collect(Collectors.toList());
    }
}
