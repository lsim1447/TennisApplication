package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.Tourney;
import tennis.service.TourneyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tourney")
@CrossOrigin(origins = "http://localhost:3000")
public class TourneyController {

    @Autowired
    private TourneyService tourneyService;

    @GetMapping("/all")
    public Iterable<Tourney> getAllTourneys(){
        return tourneyService.findAll();
    }

    @GetMapping("/grand-slams")
    public Iterable<Tourney> getGrandSlams(){
        List<Tourney> tourneys = (List<Tourney>)tourneyService.findAll();

        return tourneys.stream()
                .filter(tourney ->
                     tourney.getSlug().equals("us-open") || tourney.getSlug().equals("wimbledon")
                            || tourney.getSlug().equals("australian-open") || tourney.getSlug().equals("roland-garros")
                )
                .collect(Collectors.toList());
    }

    @GetMapping("/one")
    @ResponseBody
    public Tourney getTourneyBlSlug(@RequestParam String slug){
        return tourneyService.findBySlug(slug);
    }
}
