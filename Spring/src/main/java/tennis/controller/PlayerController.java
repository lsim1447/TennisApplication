package tennis.controller;

import org.springframework.web.bind.annotation.*;
import tennis.domain.Player;
import org.springframework.beans.factory.annotation.Autowired;
import tennis.service.PlayerService;


@RestController
@RequestMapping("/api/player")
@CrossOrigin(origins = "http://localhost:3000")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/all")
    public Iterable<Player> getAllPlayers(){
        return playerService.findAll();
    }

    @GetMapping("/one")
    @ResponseBody
    public Player getPlayerBySlug(@RequestParam String slug){
        return playerService.findBySlug(slug);
    }

}
