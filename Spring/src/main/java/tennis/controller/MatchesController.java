package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.Match;
import tennis.domain.Tournament;
import tennis.service.MatchService;
import tennis.service.TournamentService;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "http://localhost:3000")
public class MatchesController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/player/all/one")
    public Iterable<Match> getAllMatchesByPlayerName(){
        return matchService.findAllMatchesByPlayerName("Rafael", "Nadal");
    }

    @GetMapping("/one")
    @ResponseBody
    public Match getMatchById(@RequestParam String id){
        return matchService.findMatchById(id);
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

    @GetMapping("/player/last")
    @ResponseBody
    public List<Match> getLastNMatches(@RequestParam String slug, @RequestParam int nr){
        System.out.println("slug = " + slug + "  number = " + nr);
        return  matchService.findLastNMatches(slug, nr);
    }

    @GetMapping("/tournament")
    @ResponseBody
    public List<Match> getMatchesByTournament(@RequestParam String id){
        Tournament tournament = tournamentService.findTournamentById(id);
        return  matchService.findAllMatchesByTournament(tournament);
    }
}
