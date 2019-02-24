package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Player;
import tennis.domain.Tournament;
import tennis.domain.Tourney;
import tennis.model.AllTimeChampions;
import tennis.model.Champion;
import tennis.repository.PlayerRepository;
import tennis.repository.TournamentRepository;
import tennis.repository.TourneyRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TourneyRepository tourneyRepository;

    public Tournament findTournamentById(String id){
        return tournamentRepository.findById(id).orElse(null);
    }

    public Iterable<Tournament> findAll(){
        return tournamentRepository.findAll();
    }

    public Tournament save(Tournament tournament){
        return tournamentRepository.save(tournament);
    }

    public Iterable<Tournament> saveAll(Iterable<Tournament> tournaments){ return tournamentRepository.saveAll(tournaments); }

    public void deleteTournamentById(String id){ tournamentRepository.deleteById(id); }

    public Iterable<Tournament> getWonTournamentsByPlayer(Player player){
        return tournamentRepository.findAllByPlayer(player);
    }

    public Iterable<Tournament> getWonTournamentsByPlayerName(String firstName, String lastName){
        Player player = playerRepository.findByFirstNameAndLastName(firstName, lastName);
        return tournamentRepository.findAllByPlayer(player);
    }

    public Iterable<Tournament> getAllTournamentByTourney(Tourney tourney){
        return tournamentRepository.findAllByTourney(tourney);
    }

    public AllTimeChampions getAllTimeChampion(String tourney_slug){
        Tourney tourney = tourneyRepository.findBySlug(tourney_slug);
        List<Tournament> tournaments = (List<Tournament>) getAllTournamentByTourney(tourney);

        HashMap<String, Integer> map = new HashMap<>();
        tournaments.forEach(tournament -> {
            if (map.containsKey(tournament.getPlayer().getPlayerSlug())){
                map.put(tournament.getPlayer().getPlayerSlug(), map.get(tournament.getPlayer().getPlayerSlug()) + 1);
            } else {
                map.put(tournament.getPlayer().getPlayerSlug(), 1);
            }
        });

        List<String> player_slugs = new ArrayList<>();
        int times = map.get(Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey());

        map.forEach((key, value) -> {
            if (value == times) player_slugs.add(key);
        });

        List<Player> players = player_slugs.stream()
                .map(s -> playerRepository.findByPlayerSlug(s))
                .collect(Collectors.toList());

        AllTimeChampions allTimeChampion = new AllTimeChampions(players, times);
        return allTimeChampion;
    }
    
    public Iterable<Champion> getChampions(String tourney_slug){
        Tourney tourney = tourneyRepository.findBySlug(tourney_slug);
        List<Tournament> tournaments = (List<Tournament>) getAllTournamentByTourney(tourney);

        HashMap<String, Integer> myMmap = new HashMap<>();
        tournaments.forEach(tournament -> {
            if (myMmap.containsKey(tournament.getPlayer().getPlayerSlug())){
                myMmap.put(tournament.getPlayer().getPlayerSlug(), myMmap.get(tournament.getPlayer().getPlayerSlug()) + 1);
            } else {
                myMmap.put(tournament.getPlayer().getPlayerSlug(), 1);
            }
        });

        List<Champion> champions = new ArrayList<>();
        myMmap.forEach((key, value) -> {
           Champion champion = new Champion();
           champion.setPlayer(playerRepository.findByPlayerSlug(key));
           champion.setTimes(value);
           champions.add(champion);
        });
        return  champions;
    }
}
