package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.Match;
import tennis.domain.Player;
import tennis.domain.Stats;
import tennis.domain.Tourney;
import tennis.service.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prediction")
@CrossOrigin(origins = "http://localhost:3000")
public class PredicterController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private StatsService statsService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private TourneyService tourneyService;

    @GetMapping("/calculate")
    @ResponseBody
    public List<Match> calculateProbability(@RequestParam String playerOneSlug, @RequestParam String playerTwoSlug, @RequestParam String surface, @RequestParam String tourneyName, @RequestParam String nrOfCheckedElements){
        Comparator<Match> matchComparator = Comparator.comparing(match -> match.getTournament().getDates(), Comparator.reverseOrder());
        matchComparator.thenComparing(Comparator.comparing(match -> match.getRound_order(), Comparator.reverseOrder()));
        int nr = Integer.parseInt(nrOfCheckedElements);
        int nrCheckedDuel = 5;

        Player player1 = playerService.findBySlug(playerOneSlug);
        Player player2 = playerService.findBySlug(playerTwoSlug);
        Tourney tourneyByName = tourneyService.findByName(tourneyName);
        List<Tourney> tourneysBySurface = (List<Tourney>) tourneyService.findAllBySurface(surface);

        List<Match> firstPlayerMatches  =  matchService.findLastNMatches(player1.getPlayerSlug(), nr);
        List<Match> secondPlayerMatches =  matchService.findLastNMatches(player2.getPlayerSlug(), nr);

        List<Stats> firstPlayerMatchStats  = (List<Stats>) statsService.findAllStatsByMatchIds(firstPlayerMatches);
        List<Stats> secondPlayerMatchStats = (List<Stats>) statsService.findAllStatsByMatchIds(secondPlayerMatches);

        List<Match> firstPlayerMatchesOnSelectedSurface = ((List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(player1.getFirstName(), player1.getLastName(), surface))
                .stream()
                .sorted(matchComparator)
                .limit(nr)
                .collect(Collectors.toList());

        List<Match> secondPlayerMatchesOnSelectedSurface = ((List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(player2.getFirstName(), player2.getLastName(), surface))
                .stream()
                .sorted(matchComparator)
                .limit(nr)
                .collect(Collectors.toList());

        List<Stats> firstPlayerMatchStatsOnSelectedSurface  = (List<Stats>) statsService.findAllStatsByMatchIds(firstPlayerMatches);
        List<Stats> secondPlayerMatchStatsOnSelectedSurface = (List<Stats>) statsService.findAllStatsByMatchIds(secondPlayerMatches);


        List<Match> lastNMatchesBetweenTwoPlayer = ((List<Match>) matchService.findAllMatchesBetweenTwoPlayerAndSurface(player1.getFirstName(), player1.getLastName(), player2.getFirstName(), player2.getLastName(), surface))
                .stream()
                .sorted(matchComparator)
                .limit(nr)
                .collect(Collectors.toList());
        List<Stats> lastNStatsBetweenTwoPlayer = (List<Stats>) statsService.findAllStatsByMatchIds(lastNMatchesBetweenTwoPlayer);

        return firstPlayerMatchesOnSelectedSurface;
    }
}
