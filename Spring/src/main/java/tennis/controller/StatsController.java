package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.Match;
import tennis.domain.Stats;
import tennis.service.MatchService;
import tennis.service.StatsService;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:3000")
public class StatsController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private StatsService statsService;

    @GetMapping("/all")
    public Iterable<Stats> getAll(){
        List<Match> matches = (List<Match>) matchService.findAll();
        return null;
    }

    @GetMapping("/one")
    @ResponseBody
    public Stats getStatsById(@RequestParam String id){
        return statsService.findStatsByMatchId(id);
    }

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

    @GetMapping("/player/all/one/tourney")
    public Iterable<Stats> getAllMatchesByPlayerNameAndTourneyName(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName("Rafael", "Nadal", "Wimbledon");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/between/two/tourney")
    public Iterable<Stats> getAllMatchesBetweenTwoPlayerAndTourneyName(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayerAndTourneyName("Rafael", "Nadal", "Roger", "Federer", "Wimbledon");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/between/two/tourney/year")
    public Iterable<Stats> getAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyName(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyName("Rafael", "Nadal", "Roger", "Federer", "Wimbledon", 2008);
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/one/surface")
    public Iterable<Stats> getAllMatchesByPlayerNameAndSurface(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface("Rafael", "Nadal", "Grass");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/one/tourney/surface")
    public Iterable<Stats> getAllMatchesByPlayerNameAndTourneyNameAndSurface(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurface("Rafael", "Nadal", "Wimbledon", "Grass");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/one/tourney/surface/year")
    public Iterable<Stats> getAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear("Rafael", "Nadal", "Wimbledon", "Grass", 2017);
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/between/two/surface")
    public Iterable<Stats> getAllMatchesBetweenTwoPlayerAndSurface(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayerAndSurface("Rafael", "Nadal", "Roger", "Federer", "Grass");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/between/two/tourney/surface")
    public Iterable<Stats> getAllMatchesBetweenTwoPlayerAndTourneyNameAndSurface(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayerAndTourneyNameAndSurface("Rafael", "Nadal", "Roger", "Federer", "Wimbledon", "Grass");
        return statsService.findAllStatsByMatchIds(matches);
    }

    @GetMapping("/player/all/between/two/tourney/surface/year")
    public Iterable<Stats> getAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyNameAndSurface(){
        List<Match> matches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyNameAndSurface("Rafael", "Nadal", "Roger", "Federer", "Wimbledon", 2008, "Grass");
        return statsService.findAllStatsByMatchIds(matches);
    }
}
