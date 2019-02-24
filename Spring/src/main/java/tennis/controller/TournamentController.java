package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.Player;
import tennis.domain.Tournament;
import tennis.domain.Tourney;
import tennis.model.AllTimeChampions;
import tennis.model.Champion;
import tennis.service.TournamentService;

@RestController
@RequestMapping("/api/tournament")
@CrossOrigin(origins = "http://localhost:3000")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/all")
    public Iterable<Tournament> getAllTournaments(){
        return tournamentService.findAll();
    }

    @PostMapping("/name")
    public Iterable<Tournament> getWonTournamentsByPlayer(@RequestBody Player player){
        System.out.println(player);
        return tournamentService.getWonTournamentsByPlayer(player);
    }

    @PostMapping("/tourney")
    public Iterable<Tournament> getAllTournamentByTourney(@RequestBody Tourney tourney){
        return tournamentService.getAllTournamentByTourney(tourney);
    }

    @GetMapping("/all-time-champion")
    @ResponseBody
    public AllTimeChampions getAllTimeChampion(@RequestParam String slug){
        return tournamentService.getAllTimeChampion(slug);
    }

    @GetMapping("/champions")
    @ResponseBody
    public Iterable<Champion> getChampions(@RequestParam String slug){
        return tournamentService.getChampions(slug);
    }
}
