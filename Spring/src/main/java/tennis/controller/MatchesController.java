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

    @GetMapping("/player/all/between/two")
    public Iterable<Match> getAllMatchesBetweenTwoPlayer(){
        return matchService.findAllMatchesBetweenTwoPlayer("Rafael", "Nadal", "Novak", "Djokovic");
    }

    @GetMapping("/player/all/one/year")
    public Iterable<Match> getAllMatchesByPlayerNameInSelectedYear(){
        return matchService.findAllMatchesByPlayerNameInSelectedYear("Rafael", "Nadal", 2017);
    }

    @GetMapping("/player/all/between/two/year")
    public Iterable<Match> getAllMatchesBetweenTwoPlayerInSelectedYear(){
        return matchService.findAllMatchesBetweenTwoPlayerInSelectedYear("Rafael", "Nadal", "Novak", "Djokovic", 2017);
    }

    @GetMapping("/player/all/one/tourney")
    public Iterable<Match> getAllMatchesByPlayerNameAndTourneyName(){
        return matchService.findAllMatchesByPlayerNameAndTourneyName("Rafael", "Nadal", "Wimbledon");
    }

    @GetMapping("/player/all/between/two/tourney")
    public Iterable<Match> getAllMatchesBetweenTwoPlayerAndTourneyName(){
        return matchService.findAllMatchesBetweenTwoPlayerAndTourneyName("Rafael", "Nadal", "Roger", "Federer", "Wimbledon");
    }

    @GetMapping("/player/all/between/two/tourney/year")
    public Iterable<Match> getAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyName(){
        return matchService.findAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyName("Rafael", "Nadal", "Roger", "Federer", "Wimbledon", 2017);
    }

    @GetMapping("/player/all/one/surface")
    public Iterable<Match> getAllMatchesByPlayerNameAndSurface(){
        return matchService.findAllMatchesByPlayerNameAndSurface("Rafael", "Nadal", "Grass");
    }

    @GetMapping("/player/all/one/tourney/surface")
    public Iterable<Match> getAllMatchesByPlayerNameAndTourneyNameAndSurface(){
        return matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurface("Rafael", "Nadal", "Wimbledon", "Grass");
    }

    @GetMapping("/player/all/one/tourney/surface/year")
    public Iterable<Match> getAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear(){
        return matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear("Rafael", "Nadal", "Wimbledon", "Grass", 2017);
    }

    @GetMapping("/player/all/between/two/surface")
    public Iterable<Match> getAllMatchesBetweenTwoPlayerAndSurface(){
        return matchService.findAllMatchesBetweenTwoPlayerAndSurface("Rafael", "Nadal", "Roger", "Federer", "Grass");
    }

    @GetMapping("/player/all/between/two/tourney/surface")
    public Iterable<Match> getAllMatchesBetweenTwoPlayerAndTourneyNameAndSurface(){
        return matchService.findAllMatchesBetweenTwoPlayerAndTourneyNameAndSurface("Rafael", "Nadal", "Roger", "Federer", "Wimbledon", "Grass");
    }

    @GetMapping("/player/all/between/two/tourney/surface/year")
    public Iterable<Match> getAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyNameAndSurface(){
        return matchService.findAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyNameAndSurface("Rafael", "Nadal", "Roger", "Federer", "Wimbledon", 2017, "Grass");
    }
}
