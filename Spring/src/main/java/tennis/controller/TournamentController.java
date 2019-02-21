package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tennis.domain.Tournament;
import tennis.service.TournamentService;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/all")
    public Iterable<Tournament> getAllTournaments(){
        return tournamentService.findAll();
    }

    @GetMapping("/test")
    public Iterable<Tournament> getWonTournamentsByPlayerName(){
        return tournamentService.getWonTournamentsByPlayerName("Roger", "Federer");
    }
}
