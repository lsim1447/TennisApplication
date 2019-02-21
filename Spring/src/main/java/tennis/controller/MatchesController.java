package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tennis.domain.Match;
import tennis.service.MatchService;

@RestController
@RequestMapping("/api/matches")
public class MatchesController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/player/all/one")
    public Iterable<Match> getAllMatchesByPlayerName(){
        return matchService.findAllMatchesByPlayerName("Rafael", "Nadal");
    }

    @GetMapping("/player/all/one/year")
    public Iterable<Match> getAllMatchesByPlayerNameInSelectedYear(){
        return matchService.findAllMatchesByPlayerNameInSelectedYear("Rafael", "Nadal", 2017);
    }

    @GetMapping("/player/all/between/two")
    public Iterable<Match> getAllMatchesBetweenTwoPlayer(){
        return matchService.findAllMatchesBetweenTwoPlayer("Rafael", "Nadal", "Novak", "Djokovic");
    }

    @GetMapping("/player/all/between/two/year")
    public Iterable<Match> getAllMatchesBetweenTwoPlayerInSelectedYear(){
        return matchService.findAllMatchesBetweenTwoPlayerInSelectedYear("Rafael", "Nadal", "Novak", "Djokovic", 2017);
    }
}
