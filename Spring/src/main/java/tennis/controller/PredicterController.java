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

    public List<Double> addHeadToHeadMatchesWinningRateToResults(List<Double> results, Player player1, Player player2, String date, boolean to_train){

        List<Match> allHeadToHeadMatches;
        String slug_combination = "";

        if (player1.getPlayerSlug().equals(player2.getPlayerSlug())){
            slug_combination = player1.getPlayerSlug() + player2.getPlayerSlug();
        } else {
            slug_combination = player2.getPlayerSlug() + player1.getPlayerSlug();
        }

        if (matchMapHeadToHead.containsKey(slug_combination)){
            allHeadToHeadMatches = matchMapHeadToHead.get(slug_combination);
        } else {
            allHeadToHeadMatches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayer(player1.getFirstName(), player1.getLastName(), player2.getFirstName(), player2.getLastName());
            matchMapHeadToHead.put(slug_combination, allHeadToHeadMatches);
        }

        List<Match> headToHeadMatches = matchFilter(
                allHeadToHeadMatches,
                numberOfLastHeadToHeadMatches,
                date);
        List<Match> headToHeadMatchesWonByPlayerOne = headToHeadMatches
                .stream()
                .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(player1.getPlayerSlug()))
                .collect(Collectors.toList());
        List<Match> headToHeadMatchesWonByPlayerTwo = headToHeadMatches
                .stream()
                .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(player2.getPlayerSlug()))
                .collect(Collectors.toList());

        double playerOneWonHeadToHeadPercentage = Double.parseDouble(df.format(headToHeadMatchesWonByPlayerOne.size() / headToHeadMatches.size()));
        double playerTwoWonHeadToHeadPercentage = Double.parseDouble(df.format(headToHeadMatchesWonByPlayerTwo.size() / headToHeadMatches.size()));

        playerOneWonHeadToHeadPercentage = 0.5;  //temporary values because of deficient database
        playerTwoWonHeadToHeadPercentage = 0.5;

        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false){
            results.add(playerOneWonHeadToHeadPercentage);
            results.add(playerTwoWonHeadToHeadPercentage);
        } else {
            results.add(playerTwoWonHeadToHeadPercentage);
            results.add(playerOneWonHeadToHeadPercentage);
        }

        return results;
    }

    public List<Double> addMatchWinningRateToResults(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean to_train){

        double player1Percentage    = convertToPercentage(player1Matches, player1);
        double player2WonPercentage = convertToPercentage(player2Matches, player2);

        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false){
            results.add(player1Percentage);
            results.add(player2WonPercentage);
        } else {
            results.add(player2WonPercentage);
            results.add(player1Percentage);
        }

        return results;
    }

    public List<Double> addGameAndSetWinningRatesToResults(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean to_train){

        double sum_player_one_set_percentages = 0;
        double sum_player_two_set_percentages = 0;
        double sum_player_one_games_percentages = 0;
        double sum_player_two_games_percentages = 0;

        for (Match m: player1Matches) {
            int all_nr_of_sets = m.getWinner_sets_won() + m.getLoser_sets_won();
            int all_nr_of_games = m.getWinner_games_won() + m.getLoser_games_won();

            if (all_nr_of_games != 0 && all_nr_of_sets != 0){
                if (m.getWinnerPlayer().getPlayerSlug().equals(player1.getPlayerSlug())){
                    sum_player_one_set_percentages += m.getWinner_sets_won()/(double) all_nr_of_sets;
                    sum_player_one_games_percentages += m.getWinner_games_won()/(double) all_nr_of_games;
                } else {
                    sum_player_one_set_percentages += m.getLoser_sets_won()/(double) all_nr_of_sets;
                    sum_player_one_games_percentages += m.getLoser_games_won()/(double) all_nr_of_games;
                }
            }
        }

        for (Match m: player2Matches) {
            int all_nr_of_sets = m.getWinner_sets_won() + m.getLoser_sets_won();
            int all_nr_of_games = m.getWinner_games_won() + m.getLoser_games_won();

            if (all_nr_of_games != 0 && all_nr_of_sets != 0){
                if (m.getWinnerPlayer().getPlayerSlug().equals(player2.getPlayerSlug())){
                    sum_player_two_set_percentages += m.getWinner_sets_won()/(double) all_nr_of_sets;
                    sum_player_two_games_percentages += m.getWinner_games_won()/(double)all_nr_of_games;
                } else {
                    sum_player_two_set_percentages += m.getLoser_sets_won()/(double) all_nr_of_sets;
                    sum_player_two_games_percentages += m.getLoser_games_won()/(double) all_nr_of_games;
                }
            }
        }

        double playerOneWonSetsPercentage  = Double.parseDouble(df.format(sum_player_one_set_percentages / player1Matches.size()));
        double playerTwoWonSetsPercentage   = Double.parseDouble(df.format(sum_player_two_set_percentages / player2Matches.size()));
        double playerOneWonGamesPercentage = Double.parseDouble(df.format(sum_player_one_games_percentages /  player1Matches.size()));
        double playerTwoWonGamesPercentage  = Double.parseDouble(df.format(sum_player_two_games_percentages / player2Matches.size()));

        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false){
            results.add(playerOneWonSetsPercentage);
            results.add(playerTwoWonSetsPercentage);
            results.add(playerOneWonGamesPercentage);
            results.add(playerTwoWonGamesPercentage);
        } else {
            results.add(playerTwoWonSetsPercentage);
            results.add(playerOneWonSetsPercentage);
            results.add(playerTwoWonGamesPercentage);
            results.add(playerOneWonGamesPercentage);
        }

        return results;
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

            List<Double> list = new ArrayList<Double>();

            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, true);
            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, true);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, true);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, true);  // wip
            list = addHeadToHeadMatchesWinningRateToResults(list, winnerPlayer, loserPlayer, date, true);

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


            List<Double> list = new ArrayList<Double>();

            list = addMatchWinningRateToResults(list, playerOne, playerOneMatches, playerTwo, playerTwoMatches, false);
            list = addMatchWinningRateToResults(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, false);
            list = addGameAndSetWinningRatesToResults(list, playerOne, playerOneMatches, playerTwo, playerTwoMatches, false);
            list = addGameAndSetWinningRatesToResults(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, false); // wip
            list = addHeadToHeadMatchesWinningRateToResults(list, playerOne, playerTwo, "2099.01.01", false);

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

        System.out.println("Duration of the training  = " + duration);
        System.out.println("Training data length = " + trainData.size());

        String training_data_filename = "training-data-extended-test3.txt";
        String weight_filename = "weights-extended-test3.txt";
        String biases_filename = "biases-extended-test3.txt";

            TrainingDataToJSONConverter.writeToJSONFile(trainData, training_data_filename);

        TrainingRequestDTO requestDTO = new TrainingRequestDTO(training_data_filename, weight_filename, biases_filename, true);
        HttpEntity<TrainingRequestDTO> request = new HttpEntity<>(requestDTO);
        ResponseEntity<TrainingResponseDTO> response = restTemplate.postForEntity(training_url, request, TrainingResponseDTO.class);
        System.out.println(response.getBody().toString());
    }

    @GetMapping("/calculate")
    @ResponseBody
    public List<Integer> calculateProbability(@RequestParam String playerOneSlug, @RequestParam String playerTwoSlug, @RequestParam String surface, @RequestParam String tourneyName, @RequestParam String nrOfAllCheckedMatches, @RequestParam String nrOfCheckedMatchesOnSelectedSurface, @RequestParam String nrOfHeadToHeadMatches){
        System.out.println("I've got the request to PREDICT...");
       RestTemplate restTemplate = new RestTemplate();
       String URL = "http://localhost:5000/prediction";

       Player playerOne = playerService.findBySlug(playerOneSlug);
       Player playerTwo = playerService.findBySlug(playerTwoSlug);

       // Later we can receive these file names from the user => new webpage
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
