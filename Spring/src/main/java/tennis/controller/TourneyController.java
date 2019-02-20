package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tennis.domain.Tourney;
import tennis.service.TourneyService;

@RestController
@RequestMapping("/api/tourney")
public class TourneyController {

    @Autowired
    private TourneyService tourneyService;

    @GetMapping("/all")
    public Iterable<Tourney> getAllTourneys(){
        return tourneyService.findAll();
    }
}
