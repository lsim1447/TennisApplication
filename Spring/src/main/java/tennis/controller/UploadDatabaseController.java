package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tennis.domain.*;
import tennis.service.*;
import tennis.utils.CSVReader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/database/upload")
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
                        player.setPlayer_slug(line.get(1));
                    } catch (Exception e){
                        player.setPlayer_slug("");
                    }
                    try {
                        player.setFirst_name(line.get(2));
                    } catch (Exception e){
                        player.setFirst_name("");
                    }
                    try {
                        player.setLast_name(line.get(3));
                    } catch (Exception e){
                        player.setLast_name("");
                    }
                    try {
                        player.setPlayer_url(line.get(4));
                    } catch (Exception e){
                        player.setPlayer_url("");
                    }
                    try {
                        player.setFlag_code(line.get(5));
                    } catch (Exception e){
                        player.setFlag_code("");
                    }
                    try {
                        player.setBirthdate(line.get(6));
                    } catch (Exception e) {
                        player.setBirthdate("");
                    }
                    try{
                        player.setBirth_year(new Integer(line.get(7)));
                    } catch (Exception e){
                        player.setBirth_year(0);
                    }
                    try{
                        player.setBirth_month(new Integer(line.get(8)));
                    } catch (Exception e){
                        player.setBirth_year(0);
                    }
                    try{
                        player.setBirth_day(new Integer(line.get(9)));
                    } catch (Exception e){
                        player.setBirth_year(0);
                    }
                    try{
                        player.setTurned_pro(new Integer(line.get(10)));
                    } catch (Exception e){
                        player.setBirth_year(0);
                    }
                    try{
                        player.setWeight_lbs(new Integer(line.get(11)));
                    } catch (Exception e){
                        player.setBirth_year(0);
                    }
                    try{
                        player.setWeight_kg(new Integer(line.get(12)));
                    } catch (Exception e){
                        player.setBirth_year(0);
                    }
                    try{
                        player.setHeight_ft(line.get(13));
                    } catch (Exception e){
                        player.setHeight_ft("");
                    }
                    try{
                        player.setHeight_inches(line.get(14));
                    } catch (Exception e){
                        player.setHeight_inches("");
                    }
                    try{
                        player.setHeight_cm(line.get(15));
                    } catch (Exception e){
                        player.setHeight_cm("");
                    }
                    try{
                        player.setHeight_cm(line.get(15));
                    } catch (Exception e){
                        player.setHeight_cm("");
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

        playerService.saveAll(players);
        return players;
    }

    @GetMapping("/tourneys")
    public List<Tourney> uploadDatabaseWithTourneys(){
        List<List<String>> data = CSVReader.getData("tournaments").stream().collect(Collectors.toList());

        List<Tourney> tourneys = data.stream()
                .map(line -> new Tourney(line.get(2), line.get(1), line.get(3), line.get(4)))
                .distinct()
                .collect(Collectors.toList());

        tourneyService.saveAll(tourneys);
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
                    tournament.setYear(new Long(line.get(0)));
                    tournament.setSingles_draw(new Long(line.get(6)));
                    tournament.setConditions(line.get(7));
                    tournament.setSurface(line.get(8));
                    tournament.setUrl_suffix(line.get(9));

                    Tourney tourney = new Tourney();
                    tourney.setTourney_id(line.get(2));
                    tournament.setTourney(tourney);

                    Player player = playerService.findPlayerById(line.get(13));
                    //player.setPlayer_id(line.get(13));
                    tournament.setPlayer(player);
                    return  tournament;
                })
                .collect(Collectors.toList());

        System.out.println("ALL = " + tournaments.size());

        tournaments.forEach(tournament -> {
            try{
                tournamentService.save(tournament);
            } catch (Exception e){
                System.out.println("Player not found!");
            }
        });

        return tournaments;
    }

    @GetMapping("/matches")
    public List<Match> uploadDatabaseWithMatches(){
        List<List<String>> data = CSVReader.getData("match_scores_2017").stream().collect(Collectors.toList());
        List<Match> matches = data.stream()
                .map((line) -> {
                    Match match = new Match();
                    System.out.println("LINE = " + line);

                    match.setMatch_id(line.get(19));
                    match.setRound_name(line.get(1));
                    match.setRound_order(new Long(line.get(2)));
                    match.setMatch_order(new Long(line.get(3)));

                    Tournament tournament = tournamentService.findTournamentById(line.get(0));
                    match.setTournament(tournament);

                    Player winnerPlayer = playerService.findPlayerById(line.get(5));
                    match.setWinner_player(winnerPlayer);

                    Player loserPlayer = playerService.findPlayerById(line.get(8));
                    match.setLoser_player(loserPlayer);

                    match.setWinner_seed(line.get(10));
                    match.setLoser_seed(line.get(11));
                    match.setMatch_score_tiebreaks(line.get(12));
                    match.setWinner_sets_won(new Long(line.get(13)));
                    match.setLoser_sets_won(new Long(line.get(14)));
                    match.setWinner_games_won(new Long(line.get(15)));
                    match.setLoser_games_won(new Long(line.get(16)));
                    match.setWinner_tiebreaks_won(new Long(line.get(17)));
                    match.setLoser_tiebreaks_won(new Long(line.get(18)));
                    try{
                        match.setMatch_stats_url_suffix(line.get(20));
                    } catch (Exception e){

                    }
                    return  match;
                })
                .collect(Collectors.toList());

        matches.forEach(match -> {
            try{
                matchService.save(match);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        });

        return matches;
    }

    @GetMapping("/stats")
    public List<Stats> uploadDatabaseWithStats(){
        List<List<String>> data = CSVReader.getData("match_stats_2017").stream().collect(Collectors.toList());
        List<Stats> stats = data.stream()
                .map((line) -> {
                    Stats stat = new Stats();
                    System.out.println("LINE = " + line);

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

                    return  stat;
                })
                .collect(Collectors.toList());

        stats.forEach(stat -> {
            try{
                statsService.save(stat);
            } catch (Exception ex){
                System.out.println("ERROR STATS");
            }
        });

        return stats;
    }

}
