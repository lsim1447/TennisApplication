package tennis.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import tennis.domain.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/test")
    public Player test(){
        return playerService.findByFirstNameLastName("Roger", "Federer");
    }

}
