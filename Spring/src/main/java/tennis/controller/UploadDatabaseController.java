package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tennis.domain.*;
import tennis.service.*;
import tennis.utils.CSVReader;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/database/upload")
@CrossOrigin(origins = "http://localhost:3000")
public class UploadDatabaseController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TourneyService tourneyService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private StatsService statsService;

    @GetMapping("/players/men")
    public List<Player> uploadDatabaseWithMenPlayers(){
        List<List<String>> data = CSVReader.getData("players_men").stream().collect(Collectors.toList());
        List<Player> players = data.stream()
                .map((line) -> {
                    Player player = new Player();

                    player.setPlayer_id(line.get(0));
                    try {
                        player.setPlayerSlug(line.get(1));
                    } catch (Exception e){
                        player.setPlayerSlug("");
                    }
                    try {
                        player.setFirstName(line.get(2));
                    } catch (Exception e){
                        player.setFirstName("");
                    }
                    try {
                        player.setLastName(line.get(3));
                    } catch (Exception e){
                        player.setLastName("");
                    }
                    try {
                        player.setPlayerUrl(line.get(4));
                    } catch (Exception e){
                        player.setPlayerUrl("");
                    }
                    try {
                        player.setFlagCode(line.get(5));
                    } catch (Exception e){
                        player.setFlagCode("");
                    }
                    try {
                        player.setBirthdate(line.get(6));
                    } catch (Exception e) {
                        player.setBirthdate("");
                    }
                    try{
                        player.setBirthYear(new Integer(line.get(7)));
                    } catch (Exception e){
                        player.setBirthYear(0);
                    }
                    try{
                        player.setBirthMonth(new Integer(line.get(8)));
                    } catch (Exception e){
                        player.setBirthYear(0);
                    }
                    try{
                        player.setBirthDay(new Integer(line.get(9)));
                    } catch (Exception e){
                        player.setBirthYear(0);
                    }
                    try{
                        player.setTurnedPro(new Integer(line.get(10)));
                    } catch (Exception e){
                        player.setBirthYear(0);
                    }
                    try{
                        player.setWeightLbs(new Integer(line.get(11)));
                    } catch (Exception e){
                        player.setBirthYear(0);
                    }
                    try{
                        player.setWeightKg(new Integer(line.get(12)));
                    } catch (Exception e){
                        player.setBirthYear(0);
                    }
                    try{
                        player.setHeightFt(line.get(13));
                    } catch (Exception e){
                        player.setHeightFt("");
                    }
                    try{
                        player.setHeightInches(line.get(14));
                    } catch (Exception e){
                        player.setHeightInches("");
                    }
                    try{
                        player.setHeightCm(line.get(15));
                    } catch (Exception e){
                        player.setHeightCm("");
                    }
                    try{
                        player.setHeightCm(line.get(15));
                    } catch (Exception e){
                        player.setHeightCm("");
                    }
                    try{
                        player.setHandedness(line.get(16));
                    } catch (Exception e){
                        player.setHandedness("");
                    }
                    try{
                        player.setBackhand(line.get(17));
                    } catch (Exception e){
                        player.setBackhand("");
                    }
                    return  player;
                })
                .collect(Collectors.toList());

        System.out.println("All players = " + players.size());
        playerService.saveAll(players);
        return players;
    }

    @GetMapping("/tourneys")
    public List<Tourney> uploadDatabaseWithTourneys(){
        List<List<String>> data = CSVReader.getData("tournaments").stream().collect(Collectors.toList());

        List<Tourney> tourneys = data. stream()
                .map(line -> new Tourney(line.get(2), line.get(1), line.get(3), line.get(4), line.get(7), line.get(8)))
                .collect(Collectors.toList());

        HashMap<String, Tourney> map = new HashMap<>();
        int i = tourneys.size();
        String key = "";
        Tourney value;

        while (i > 0) {
            i--;
            key = tourneys.get(i).getName();
            value = tourneys.get(i);
            if (!map.containsKey(key)){
                map.put(key, value);
            }
        }

        map.forEach((k, v) -> {
            System.out.println("Saving the following tourney to the database: " + v.getName());
            tourneyService.save(v);
        });

        return tourneys;
    }

    @GetMapping("/tournaments")
    public List<Tournament> uploadDatabaseWithTournaments(){
        List<List<String>> data = CSVReader.getData("tournaments").stream().collect(Collectors.toList());
        List<Tournament> tournaments = data.stream()
                .map((line) -> {
                    //System.out.println("LINE = " + line);
                    Tournament tournament = new Tournament();
                    tournament.setTournament_year_id(line.get(14));
                    tournament.setDates(line.get(5));
                    tournament.setYear(Integer.parseInt(line.get(0)));
                    tournament.setSinglesDraw(Integer.parseInt(line.get(6)));
                    tournament.setUrlSuffix(line.get(9));

                    Tourney tourney = tourneyService.findTourneyById(line.get(2));
                    tournament.setTourney(tourney);

                    Player player = playerService.findPlayerById(line.get(13));
                    tournament.setPlayer(player);
                    return  tournament;
                })
                .collect(Collectors.toList());

        tournaments.forEach(tournament -> {
            try{
                System.out.println("Saving the following tournament to the database: " + tournament.getTournament_year_id());
                tournamentService.save(tournament);
            } catch (Exception e){
                System.out.println("Sorry! We are not able to save this tournament to the database.");
            }
        });

        return tournaments;
    }

    @GetMapping("/matches")
    public String uploadDatabaseWithMatches(){
        List<List<String>> data = CSVReader.getData("match_scores_1991-2016").stream().collect(Collectors.toList());
        int counter = 0;
        for (List<String> line: data) {
            counter ++;
            System.out.println("nr = " + counter);
            System.out.println("LINE = " + line);

            Match match = new Match();
            match.setMatch_id(line.get(17));
            match.setRound_name(line.get(1));
            match.setRound_order(Integer.parseInt(line.get(2)));
            match.setMatch_order(Integer.parseInt(line.get(3)));

            Tournament tournament = tournamentService.findTournamentById(line.get(0));
            match.setTournament(tournament);

            Player winnerPlayer = playerService.findPlayerById(line.get(5));
            match.setWinnerPlayer(winnerPlayer);

            Player loserPlayer = playerService.findPlayerById(line.get(8));
            match.setLoserPlayer(loserPlayer);

            match.setMatch_score_tiebreaks(line.get(10));
            match.setWinner_sets_won(Integer.parseInt(line.get(11)));
            match.setLoser_sets_won(Integer.parseInt(line.get(12)));
            match.setWinner_games_won(Integer.parseInt(line.get(13)));
            match.setLoser_games_won(Integer.parseInt(line.get(14)));
            match.setWinner_tiebreaks_won(Integer.parseInt(line.get(15)));
            match.setLoser_tiebreaks_won(Integer.parseInt(line.get(16)));

            matchService.save(match);
        }

        return "DONE!!!";
    }

    @GetMapping("/stats")
    public String uploadDatabaseWithStats(){
        List<List<String>> data = CSVReader.getData("match_stats_2007-2016").stream().collect(Collectors.toList());
        int counter = 0;

        for (List<String> line: data) {
            counter++;
            Stats stat = new Stats();
            //System.out.println("LINE = " + line);
            //System.out.println("NR = " + counter);

            try {
                Match match = matchService.findMatchById(line.get(0));
                stat.setMatch(match);
                stat.setMatch_time(line.get(1));
                stat.setMatch_duration(line.get(2));
                stat.setWinner_aces(Integer.parseInt(line.get(3)));
                stat.setWinner_double_faults(Integer.parseInt(line.get(4)));
                stat.setWinner_first_serves_in(Integer.parseInt(line.get(5)));
                stat.setWinner_first_serves_total(Integer.parseInt(line.get(6)));
                stat.setWinner_first_serve_points_won(Integer.parseInt(line.get(7)));
                stat.setWinner_first_serve_points_total(Integer.parseInt(line.get(8)));
                stat.setWinner_second_serve_points_won(Integer.parseInt(line.get(9)));
                stat.setWinner_second_serve_points_total(Integer.parseInt(line.get(10)));
                stat.setWinner_break_points_saved(Integer.parseInt(line.get(11)));
                stat.setWinner_break_points_serve_total(Integer.parseInt(line.get(12)));
                stat.setWinner_service_points_won(Integer.parseInt(line.get(13)));
                stat.setWinner_service_points_total(Integer.parseInt(line.get(14)));
                stat.setWinner_first_serve_return_won(Integer.parseInt(line.get(15)));
                stat.setWinner_first_serve_return_total(Integer.parseInt(line.get(16)));
                stat.setWinner_second_serve_return_won(Integer.parseInt(line.get(17)));
                stat.setWinner_second_serve_return_total(Integer.parseInt(line.get(18)));
                stat.setWinner_break_points_converted(Integer.parseInt(line.get(19)));
                stat.setWinner_break_points_return_total(Integer.parseInt(line.get(20)));
                stat.setWinner_service_games_played(Integer.parseInt(line.get(21)));
                stat.setWinner_return_games_played(Integer.parseInt(line.get(22)));
                stat.setWinner_return_points_won(Integer.parseInt(line.get(23)));
                stat.setWinner_return_points_total(Integer.parseInt(line.get(24)));
                stat.setWinner_total_points_won(Integer.parseInt(line.get(25)));
                stat.setWinner_total_points_total(Integer.parseInt(line.get(26)));
                //loser
                stat.setLoser_aces(Integer.parseInt(line.get(27)));
                stat.setLoser_double_faults(Integer.parseInt(line.get(28)));
                stat.setLoser_first_serves_in(Integer.parseInt(line.get(29)));
                stat.setLoser_first_serves_total(Integer.parseInt(line.get(30)));
                stat.setLoser_first_serve_points_won(Integer.parseInt(line.get(31)));
                stat.setLoser_first_serve_points_total(Integer.parseInt(line.get(32)));
                stat.setLoser_second_serve_points_won(Integer.parseInt(line.get(33)));
                stat.setLoser_second_serve_points_total(Integer.parseInt(line.get(34)));
                stat.setLoser_break_points_saved(Integer.parseInt(line.get(35)));
                stat.setLoser_break_points_serve_total(Integer.parseInt(line.get(36)));
                stat.setLoser_service_points_won(Integer.parseInt(line.get(37)));
                stat.setLoser_service_points_total(Integer.parseInt(line.get(38)));
                stat.setLoser_first_serve_return_won(Integer.parseInt(line.get(39)));
                stat.setLoser_first_serve_return_total(Integer.parseInt(line.get(40)));
                stat.setLoser_second_serve_return_won(Integer.parseInt(line.get(41)));
                stat.setLoser_second_serve_return_total(Integer.parseInt(line.get(42)));
                stat.setLoser_break_points_converted(Integer.parseInt(line.get(43)));
                stat.setLoser_break_points_return_total(Integer.parseInt(line.get(44)));
                stat.setLoser_service_games_played(Integer.parseInt(line.get(45)));
                stat.setLoser_return_games_played(Integer.parseInt(line.get(46)));
                stat.setLoser_return_points_won(Integer.parseInt(line.get(47)));
                stat.setLoser_return_points_total(Integer.parseInt(line.get(48)));
                stat.setLoser_total_points_won(Integer.parseInt(line.get(49)));
                stat.setLoser_total_points_total(Integer.parseInt(line.get(50)));

                statsService.save(stat);
            } catch (Exception e){
                System.out.println("ERRROR = " + e.getMessage());
            }
        }

        return "DONE!!!";
    }

}
