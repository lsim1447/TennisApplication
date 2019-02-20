package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Stats;
import tennis.repository.StatsRepository;

@Service
public class StatsService {

    @Autowired
    private StatsRepository statsRepository;

    public Stats findStatsByMatchId(String id){ return statsRepository.findById(id).orElse(null); }

    public Iterable<Stats> findAll(){ return statsRepository.findAll(); }

    public Stats save(Stats stats){ return statsRepository.save(stats); }

    public Iterable<Stats> saveAll(Iterable<Stats> stats) { return statsRepository.saveAll(stats); }

    public void deleteByMatchId(String id) { statsRepository.deleteById(id); }
}
