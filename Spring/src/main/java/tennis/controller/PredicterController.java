package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tennis.domain.Match;
import tennis.domain.Player;
import tennis.model.*;
import tennis.service.*;
import tennis.utils.python.TrainingDataToJSONConverter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prediction")
@CrossOrigin(origins = "http://localhost:3000")
public class PredicterController {

    private Comparator<Match> matchComparator;
    private HashMap<String, List<Match>> matchMap = new HashMap<>();
    private HashMap<String, List<Match>> matchMapOnSurface = new HashMap<>();
    private HashMap<String, List<Match>> matchMapHeadToHead = new HashMap<>();

    @Autowired
    private PredicterService predicterService;

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


    @Autowired
    public PredicterController(){
        matchComparator = Comparator.comparing(m -> m.getTournament().getDates(), Comparator.reverseOrder());
        matchComparator.thenComparing(Comparator.comparing(m -> m.getRound_order(), Comparator.reverseOrder()));
    }

    private int numberOfLastMatches = 10;
    private int numberOfLastMatchesOnSpecificSurface = 10;
    private int numberOfLastHeadToHeadMatches = 2;
    private DecimalFormat df = new DecimalFormat("####0.00");

    private double convertToPercentage(List<Match> matches, Player player){
        if (matches.size() == 0) return 0.00;
        long counter = matches.stream()
                .filter(match -> match.getWinnerPlayer().getPlayer_id() == player.getPlayer_id())
                .count();

        return Double.parseDouble(df.format((double) counter / (double) matches.size()));
    }

    private List<Match> matchFilter(List<Match> matches, int limit, String date){
        return matches
                .stream()
                .filter(m -> m.getTournament().getDates().compareTo(date) <= 0)
                .sorted(matchComparator)
                .limit(limit)
                .collect(Collectors.toList());

    }

    private List<Integer> getOutputs(Match match){
        Player winnerPlayer = match.getWinnerPlayer();
        Player loserPlayer = match.getLoserPlayer();

        List<Integer> list = new ArrayList<Integer>();

        if (winnerPlayer.getPlayerSlug().compareTo(loserPlayer.getPlayerSlug()) <= 0){
            list.add(1);
            list.add(0);
        } else {
            list.add(0);
            list.add(1);
        }
        return list;
    }

    private List<TrainData> getTrainData(List<Match> matches){
        return matches.stream()
                .filter(match -> getInputs(match) != null)
                .map(match -> new TrainData(getInputs(match), getOutputs(match)))
                .collect(Collectors.toList());
    }

    private List<Double> getInputs(Match match){
        try{
            Player winnerPlayer = match.getWinnerPlayer();
            Player loserPlayer  = match.getLoserPlayer();
            String date = match.getTournament().getDates();
            String surface = match.getTournament().getTourney().getSurface();
            List<Match> winnerPlayerAllMatches;
            List<Match> loserPlayerAllMatches;
            List<Match> winnerPlayerAllMatchesOnSelectedSurface;
            List<Match> loserPlayerAllMatchesOnSelectedSurface;

            if (matchMap.containsKey(winnerPlayer.getPlayerSlug())){
                winnerPlayerAllMatches = matchMap.get(winnerPlayer.getPlayerSlug());
            } else {
                winnerPlayerAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(winnerPlayer.getFirstName(), winnerPlayer.getLastName());
                matchMap.put(winnerPlayer.getPlayerSlug(), winnerPlayerAllMatches);
            }
            if (matchMap.containsKey(loserPlayer.getPlayerSlug())){
                loserPlayerAllMatches = matchMap.get(loserPlayer.getPlayerSlug());
            } else {
                loserPlayerAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(loserPlayer.getFirstName(), loserPlayer.getLastName());
                matchMap.put(loserPlayer.getPlayerSlug(), loserPlayerAllMatches);
            }

            if (matchMapOnSurface.containsKey(winnerPlayer.getPlayerSlug())){
                winnerPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(winnerPlayer.getPlayerSlug());
            } else {
                winnerPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), surface);
                matchMapOnSurface.put(winnerPlayer.getPlayerSlug(), winnerPlayerAllMatchesOnSelectedSurface);
            }
            if (matchMapOnSurface.containsKey(loserPlayer.getPlayerSlug())){
                loserPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(loserPlayer.getPlayerSlug());
            } else {
                loserPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(loserPlayer.getFirstName(), loserPlayer.getLastName(), surface);
                matchMapOnSurface.put(loserPlayer.getPlayerSlug(), loserPlayerAllMatchesOnSelectedSurface);
            }

            List<Match> winnerPlayerMatches = matchFilter(winnerPlayerAllMatches, numberOfLastMatches, date);
            List<Match> loserPlayerMatches = matchFilter(loserPlayerAllMatches, numberOfLastMatches, date);
            List<Match> winnerPlayerMatchesOnSurface = matchFilter(winnerPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date);
            List<Match> loserPlayerMatchesOnSurface = matchFilter(loserPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date);

            if (winnerPlayerMatches.size() < numberOfLastMatches || loserPlayerMatches.size() < numberOfLastMatches || winnerPlayerMatchesOnSurface.size() < numberOfLastMatchesOnSpecificSurface || loserPlayerMatchesOnSurface.size() < numberOfLastMatchesOnSpecificSurface) return null;

            double winnerPlayeWonPercentageFromAll = convertToPercentage(winnerPlayerMatches, winnerPlayer);
            double loserPlayeWonPercentageFromAll = convertToPercentage(loserPlayerMatches, loserPlayer);
            double winnerPlayeWonPercentageFromSelectedSurface = convertToPercentage(winnerPlayerMatchesOnSurface, winnerPlayer);
            double loserPlayeWonPercentageFromSelectedSurface = convertToPercentage(loserPlayerMatchesOnSurface, loserPlayer);

            // WON SETS PERCENTAGES && WON GAMES PERCENTAGES
            double sum_winner_set_percentages = 0.0;
            double sum_loser_set_percentages = 0.0;

            double sum_winner_games_percentages = 0.0;
            double sum_loser_games_percentages = 0.0;

            for (Match m: winnerPlayerMatches) {
                int all_nr_of_sets = m.getWinner_sets_won() + m.getLoser_sets_won();
                int all_nr_of_games = m.getWinner_games_won() + m.getLoser_games_won();

                if (m.getWinnerPlayer().getPlayerSlug().equals(winnerPlayer.getPlayerSlug())){
                    sum_winner_set_percentages += m.getWinner_sets_won()/(double) all_nr_of_sets;
                    sum_winner_games_percentages += m.getWinner_games_won()/(double) all_nr_of_games;
                } else {
                    sum_winner_set_percentages += m.getLoser_sets_won()/(double) all_nr_of_sets;
                    sum_winner_games_percentages += m.getLoser_games_won()/(double) all_nr_of_games;
                }
            }

            for (Match m: loserPlayerMatches) {
                int all_nr_of_sets = m.getWinner_sets_won() + m.getLoser_sets_won();
                int all_nr_of_games = m.getWinner_games_won() + m.getLoser_games_won();

                if (m.getWinnerPlayer().getPlayerSlug().equals(loserPlayer.getPlayerSlug())){
                    sum_loser_set_percentages += m.getWinner_sets_won()/(double) all_nr_of_sets;
                    sum_loser_games_percentages += m.getWinner_games_won()/(double)all_nr_of_games;
                } else {
                    sum_loser_set_percentages += m.getLoser_sets_won()/(double) all_nr_of_sets;
                    sum_loser_games_percentages += m.getLoser_games_won()/(double) all_nr_of_games;
                }
            }

            double winnerPlayerWonSetsPercentage  = Double.parseDouble(df.format(sum_winner_set_percentages / winnerPlayerMatches.size()));
            double loserPlayerWonSetsPercentage   = Double.parseDouble(df.format(sum_loser_set_percentages / loserPlayerMatches.size()));
            double winnerPlayerWonGamesPercentage = Double.parseDouble(df.format(sum_winner_games_percentages /  winnerPlayerMatches.size()));
            double loserPlayerWonGamesPercentage  = Double.parseDouble(df.format(sum_loser_games_percentages / loserPlayerMatches.size()));


            // HEAD TO HEAD MATCHES
            List<Match> allHeadToHeadMatches;
            String slug_combination = "";
            if (winnerPlayer.getPlayerSlug().equals(loserPlayer.getPlayerSlug())){
                slug_combination = winnerPlayer.getPlayerSlug() + loserPlayer.getPlayerSlug();
            } else {
                slug_combination = loserPlayer.getPlayerSlug() + winnerPlayer.getPlayerSlug();
            }

            if (matchMapHeadToHead.containsKey(slug_combination)){
                allHeadToHeadMatches = matchMapHeadToHead.get(slug_combination);
            } else {
                allHeadToHeadMatches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayer(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), loserPlayer.getFirstName(), loserPlayer.getLastName());
                matchMapHeadToHead.put(slug_combination, allHeadToHeadMatches);
            }

            List<Match> headToHeadMatches = matchFilter(
                    allHeadToHeadMatches,
                    numberOfLastHeadToHeadMatches,
                    date);
            List<Match> headToHeadMatchesWonByWinnerPlayer = headToHeadMatches
                    .stream()
                    .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(winnerPlayer.getPlayerSlug()))
                    .collect(Collectors.toList());
            List<Match> headToHeadMatchesWonByLoserPlayer = headToHeadMatches
                    .stream()
                    .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(loserPlayer.getPlayerSlug()))
                    .collect(Collectors.toList());

            double winnerPlayerWonHeadToHeadPercentage = Double.parseDouble(df.format(headToHeadMatchesWonByWinnerPlayer.size() / headToHeadMatches.size()));
            double loserPlayerWonHeadToHeadPercentage = Double.parseDouble(df.format(headToHeadMatchesWonByLoserPlayer.size() / headToHeadMatches.size()));

            List<Double> list = new ArrayList<Double>();
            if (winnerPlayer.getPlayerSlug().compareTo(loserPlayer.getPlayerSlug()) <= 0){
                list.add(winnerPlayeWonPercentageFromAll);
                list.add(loserPlayeWonPercentageFromAll);
                list.add(winnerPlayeWonPercentageFromSelectedSurface);
                list.add(loserPlayeWonPercentageFromSelectedSurface);
                list.add(winnerPlayerWonSetsPercentage);
                list.add(loserPlayerWonSetsPercentage);
                list.add(winnerPlayerWonGamesPercentage);
                list.add(loserPlayerWonGamesPercentage);
                list.add(winnerPlayerWonHeadToHeadPercentage);
                list.add(loserPlayerWonHeadToHeadPercentage);
            } else {
                list.add(loserPlayeWonPercentageFromAll);
                list.add(winnerPlayeWonPercentageFromAll);
                list.add(loserPlayeWonPercentageFromSelectedSurface);
                list.add(winnerPlayeWonPercentageFromSelectedSurface);
                list.add(loserPlayerWonSetsPercentage);
                list.add(winnerPlayerWonSetsPercentage);
                list.add(loserPlayerWonGamesPercentage);
                list.add(winnerPlayerWonGamesPercentage);
                list.add(loserPlayerWonHeadToHeadPercentage);
                list.add(winnerPlayerWonHeadToHeadPercentage);
            }

            return  list;

        } catch (Exception e){
            return null;
        }

    }

    private List<Double> getInputsToPredict(Player playerOne, Player playerTwo, String surface){
            List<Match> playerOneAllMatches;
            List<Match> playerTwoAllMatches;

            List<Match> playerOneAllMatchesOnSelectedSurface;
            List<Match> playerTwoAllMatchesOnSelectedSurface;

            playerOneAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(playerOne.getFirstName(), playerOne.getLastName());
            playerTwoAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(playerTwo.getFirstName(), playerTwo.getLastName());

            playerOneAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(playerOne.getFirstName(), playerOne.getLastName(), surface);
            playerTwoAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(playerTwo.getFirstName(), playerTwo.getLastName(), surface);

            List<Match> playerOneMatches = playerOneAllMatches
                    .stream()
                    .sorted(matchComparator)
                    .limit(numberOfLastMatches)
                    .collect(Collectors.toList());

            List<Match> playerTwoMatches = playerTwoAllMatches
                    .stream()
                    .sorted(matchComparator)
                    .limit(numberOfLastMatches)
                    .collect(Collectors.toList());


            List<Match> playerOneMatchesOnSurface = playerOneAllMatchesOnSelectedSurface
                    .stream()
                    .sorted(matchComparator)
                    .limit(numberOfLastMatchesOnSpecificSurface)
                    .collect(Collectors.toList());

            List<Match> playerTwoMatchesOnSurface = playerTwoAllMatchesOnSelectedSurface
                    .stream()
                    .sorted(matchComparator)
                    .limit(numberOfLastMatchesOnSpecificSurface)
                    .collect(Collectors.toList());

            double playerOneWonPercentageFromAll = convertToPercentage(playerOneMatches, playerOne);
            double playerTwoWonPercentageFromAll = convertToPercentage(playerTwoMatches, playerTwo);
            double playerOneWonPercentageFromSelectedSurface = convertToPercentage(playerOneMatchesOnSurface, playerOne);
            double playerTwoWonPercentageFromSelectedSurface = convertToPercentage(playerTwoMatchesOnSurface, playerTwo);

            // WON SETS PERCENTAGES && WON GAMES PERCENTAGES
            double sum_player_one_set_percentages = 0;
            double sum_player_two_set_percentages = 0;
            double sum_player_one_games_percentages = 0;
            double sum_player_two_games_percentages = 0;

            for (Match m: playerOneMatches) {
                int all_nr_of_sets = m.getWinner_sets_won() + m.getLoser_sets_won();
                int all_nr_of_games = m.getWinner_games_won() + m.getLoser_games_won();

                if (all_nr_of_games != 0 && all_nr_of_sets != 0){
                    if (m.getWinnerPlayer().getPlayerSlug().equals(playerOne.getPlayerSlug())){
                        sum_player_one_set_percentages += m.getWinner_sets_won()/(double) all_nr_of_sets;
                        sum_player_one_games_percentages += m.getWinner_games_won()/(double) all_nr_of_games;
                    } else {
                        sum_player_one_set_percentages += m.getLoser_sets_won()/(double) all_nr_of_sets;
                        sum_player_one_games_percentages += m.getLoser_games_won()/(double) all_nr_of_games;
                    }
                }
            }

            for (Match m: playerTwoMatches) {
                int all_nr_of_sets = m.getWinner_sets_won() + m.getLoser_sets_won();
                int all_nr_of_games = m.getWinner_games_won() + m.getLoser_games_won();

                if (all_nr_of_games != 0 && all_nr_of_sets != 0){
                    if (m.getWinnerPlayer().getPlayerSlug().equals(playerTwo.getPlayerSlug())){
                        sum_player_two_set_percentages += m.getWinner_sets_won()/(double) all_nr_of_sets;
                        sum_player_two_games_percentages += m.getWinner_games_won()/(double)all_nr_of_games;
                    } else {
                        sum_player_two_set_percentages += m.getLoser_sets_won()/(double) all_nr_of_sets;
                        sum_player_two_games_percentages += m.getLoser_games_won()/(double) all_nr_of_games;
                    }
                }
            }

            double playerOneWonSetsPercentage  = Double.parseDouble(df.format(sum_player_one_set_percentages / playerOneMatches.size()));
            double playerTwoWonSetsPercentage   = Double.parseDouble(df.format(sum_player_two_set_percentages / playerTwoMatches.size()));
            double playerOneWonGamesPercentage = Double.parseDouble(df.format(sum_player_one_games_percentages /  playerOneMatches.size()));
            double playerTwoWonGamesPercentage  = Double.parseDouble(df.format(sum_player_two_games_percentages / playerTwoMatches.size()));

            // HEAD TO HEAD MATCHES
            List<Match> allHeadToHeadMatches;
            String slug_combination = "";
            if (playerOne.getPlayerSlug().equals(playerTwo.getPlayerSlug())){
                slug_combination = playerOne.getPlayerSlug() + playerTwo.getPlayerSlug();
            } else {
                slug_combination = playerTwo.getPlayerSlug() + playerOne.getPlayerSlug();
            }

            if (matchMapHeadToHead.containsKey(slug_combination)){
                allHeadToHeadMatches = matchMapHeadToHead.get(slug_combination);
            } else {
                allHeadToHeadMatches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayer(playerOne.getFirstName(), playerOne.getLastName(), playerTwo.getFirstName(), playerTwo.getLastName());
                matchMapHeadToHead.put(slug_combination, allHeadToHeadMatches);
            }

            List<Match> headToHeadMatches = matchFilter(
                    allHeadToHeadMatches,
                    numberOfLastHeadToHeadMatches,
                    "2099.01.01");
            List<Match> headToHeadMatchesWonByPlayerOne = headToHeadMatches
                    .stream()
                    .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(playerOne.getPlayerSlug()))
                    .collect(Collectors.toList());
            List<Match> headToHeadMatchesWonByPlayerTwo = headToHeadMatches
                    .stream()
                    .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(playerTwo.getPlayerSlug()))
                    .collect(Collectors.toList());

            double winnerPlayerWonHeadToHeadPercentage = Double.parseDouble(df.format(headToHeadMatchesWonByPlayerOne.size() / headToHeadMatches.size()));
            double loserPlayerWonHeadToHeadPercentage = Double.parseDouble(df.format(headToHeadMatchesWonByPlayerTwo.size() / headToHeadMatches.size()));

        List<Double> list = new ArrayList<Double>();
        if (playerOne.getPlayerSlug().compareTo(playerTwo.getPlayerSlug()) <= 0){
            list.add(playerOneWonPercentageFromAll);
            list.add(playerTwoWonPercentageFromAll);
            list.add(playerOneWonPercentageFromSelectedSurface);
            list.add(playerTwoWonPercentageFromSelectedSurface);
            list.add(playerOneWonSetsPercentage);
            list.add(playerTwoWonSetsPercentage);
            list.add(playerOneWonGamesPercentage);
            list.add(playerTwoWonGamesPercentage);
            list.add(winnerPlayerWonHeadToHeadPercentage);
            list.add(loserPlayerWonHeadToHeadPercentage);
        } else {
            list.add(playerTwoWonPercentageFromAll);
            list.add(playerOneWonPercentageFromAll);
            list.add(playerTwoWonPercentageFromSelectedSurface);
            list.add(playerOneWonPercentageFromSelectedSurface);
            list.add(playerTwoWonSetsPercentage);
            list.add(playerOneWonSetsPercentage);
            list.add(playerTwoWonGamesPercentage);
            list.add(playerOneWonGamesPercentage);
            list.add(loserPlayerWonHeadToHeadPercentage);
            list.add(winnerPlayerWonHeadToHeadPercentage);
        }

        return  list;

    }

    @GetMapping("/training")
    @ResponseBody
    public void training(){
        List<Match> allMatches = (List<Match>) matchService.findAll();
        List<TrainData> trainData;
        String training_url = "http://localhost:5000/training";
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("Starting training ...");
        long startTime = System.nanoTime();
            trainData = getTrainData(allMatches);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        System.out.println("Duration of creating training data = " + duration);
        System.out.println("Training data length = " + trainData.size());

        String training_data_filename = "training-data-extended-test3.txt";
        String weight_filename = "weights-extended-test3.txt";
        String biases_filename = "biases-extended-test3.txt";

            TrainingDataToJSONConverter.writeToJSONFile(trainData, training_data_filename);

        TrainingRequestDTO requestDTO = new TrainingRequestDTO(training_data_filename, weight_filename, biases_filename);
        HttpEntity<TrainingRequestDTO> request = new HttpEntity<>(requestDTO);
        ResponseEntity<TrainingResponseDTO> response = restTemplate.postForEntity(training_url, request, TrainingResponseDTO.class);
        System.out.println(response.getBody().toString());
    }

    @GetMapping("/calculate")
    @ResponseBody
    public List<Integer> calculateProbability(@RequestParam String playerOneSlug, @RequestParam String playerTwoSlug, @RequestParam String surface, @RequestParam String tourneyName, @RequestParam String nrOfAllCheckedMatches, @RequestParam String nrOfCheckedMatchesOnSelectedSurface, @RequestParam String nrOfHeadToHeadMatches){

       RestTemplate restTemplate = new RestTemplate();
       String URL = "http://localhost:5000/prediction";

       Player playerOne = playerService.findBySlug(playerOneSlug);
       Player playerTwo = playerService.findBySlug(playerTwoSlug);

       List<Double> inputs = getInputsToPredict(playerOne, playerTwo, surface);
       String weight_filename = "weights-extended-test3.txt";
       String biases_filename = "biases-extended-test3.txt";

       PredictionRequestDTO requestDTO = new PredictionRequestDTO(playerOne.getPlayerSlug(), playerTwo.getPlayerSlug(), weight_filename, biases_filename, getInputsToPredict(playerOne, playerTwo, surface));
       HttpEntity<PredictionRequestDTO> request = new HttpEntity<>(requestDTO);
       ResponseEntity<PredictionResponseDTO> response;

       try{
           response = restTemplate.postForEntity(URL, request, PredictionResponseDTO.class);
           PredictionResponseDTO responseDTO = response.getBody();
           return predicterService.convertSumToOneHundredPercent(responseDTO.getFirst_percentage(), responseDTO.getSecond_percentage());
       } catch (Exception e){
           System.out.println(e);
       }


       return Arrays.asList(50, 50);
    }
}
