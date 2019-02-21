package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tennis.domain.Match;
import tennis.domain.Stats;
import tennis.service.MatchService;
import tennis.service.StatsService;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private StatsService statsService;

    @GetMapping("/player/all/one")
    public Iterable<Stats> getAllStatsByPlayerName(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesByPlayerName("Roger", "Federer");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/one/year")
    public Iterable<Stats> getAllStatsByPlayerNameInSelectedYear(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesByPlayerNameInSelectedYear("Roger", "Federer", 2017);
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/between/two")
    public Iterable<Stats> getAllStatsBetweenTwoPlayer(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayer("Rafael", "Nadal", "Novak", "Djokovic");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/between/two/year")
    public Iterable<Stats> getAllStatsBetweenTwoPlayerInSelectedYear(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayerInSelectedYear("Rafael", "Nadal", "Novak", "Djokovic", 2017);
        return statsService.findAllStatsByMatchIds(matches);
    }
}
