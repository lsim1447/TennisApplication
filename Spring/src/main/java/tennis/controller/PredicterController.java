package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tennis.domain.*;
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

    private int LIMIT_NR_OF_ALL_MATCHES = 20;
    private int LIMIT_NR_OF_ALL_SURFACE_MATCHES = 10;
    private int LIMIT_NR_OF_ALL_TOURNAMENT_MATCHES = 2;

    private Comparator<Match> matchComparator;
    private HashMap<String, List<Match>> matchMap = new HashMap<>();
    private HashMap<String, List<Match>> matchMapOnSurface = new HashMap<>();
    private HashMap<String, List<Match>> matchMapHeadToHead = new HashMap<>();
    private HashMap<String, List<Match>> matchMapHeadToHeadOnSurface = new HashMap<>();
    private HashMap<String, List<Match>> matchMapOnTournament = new HashMap<>();

    @Autowired private PredicterService predicterService;

    @Autowired private MatchService matchService;

    @Autowired private StatsService statsService;

    @Autowired private PlayerService playerService;

    @Autowired private TournamentService tournamentService;

    @Autowired private TourneyService tourneyService;

    @Autowired private TrainingDescriptionService trainingDescriptionService;

    @Autowired private TrainingResultsDataService trainingResultsDataService;

    @Autowired
    public PredicterController(){
        matchComparator = Comparator.comparing(m -> m.getTournament().getDates(), Comparator.reverseOrder());
        matchComparator.thenComparing(Comparator.comparing(m -> m.getRound_order(), Comparator.reverseOrder()));
    }

    private int numberOfLastMatches = 160;
    private int numberOfLastMatchesOnSpecificSurface = 100;
    private int numberOfLastHeadToHeadMatches = 12;
    private int numberOfLastHeadToHeadMatchesOnSpecificSurface = 8;
    private int numberOfLastMatchesOnTournament = 60;

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
                .filter(m -> m.getTournament().getDates().compareTo(date) < 0)
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

    private List<Double> addHeadToHeadMatchesWinningRateToResults(List<Double> results, Player player1, Player player2, String date, boolean to_train){

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

        double playerOneWonHeadToHeadPercentage = (headToHeadMatches.size() >= 3) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerOne.size() / (double)headToHeadMatches.size())) : 0.5;
        double playerTwoWonHeadToHeadPercentage = (headToHeadMatches.size() >= 3) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerTwo.size() / (double)headToHeadMatches.size())) : 0.5;

        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false){
            results.add(playerOneWonHeadToHeadPercentage);
            results.add(playerTwoWonHeadToHeadPercentage);
        } else {
            results.add(playerTwoWonHeadToHeadPercentage);
            results.add(playerOneWonHeadToHeadPercentage);
        }

        return results;
    }

    private List<Double> addMatchWinningRateToResults(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean to_train){

        double player1Percentage    = convertToPercentage(player1Matches, player1);
        double player2Percentage = convertToPercentage(player2Matches, player2);

        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false){
            results.add(player1Percentage);
            results.add(player2Percentage);
        } else {
            results.add(player2Percentage);
            results.add(player1Percentage);
        }

        return results;
    }

    private List<Double> addGameAndSetWinningRatesToResults(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean to_train){

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

    private List<Double> addHeadToHeadMatchesOnSurfaceWinningRateToResults(List<Double> results, Player player1, Player player2, String date, String surface, boolean to_train){

        List<Match> allHeadToHeadMatches;
        String slug_combination = "";

        if (player1.getPlayerSlug().equals(player2.getPlayerSlug())){
            slug_combination = player1.getPlayerSlug() + player2.getPlayerSlug() + surface;
        } else {
            slug_combination = player2.getPlayerSlug() + player1.getPlayerSlug() + surface;
        }

        if (matchMapHeadToHeadOnSurface.containsKey(slug_combination)){
            allHeadToHeadMatches = matchMapHeadToHeadOnSurface.get(slug_combination);
        } else {
            allHeadToHeadMatches = (List<Match>) matchService.findAllMatchesBetweenTwoPlayerAndSurface(player1.getFirstName(), player1.getLastName(), player2.getFirstName(), player2.getLastName(), surface);
            matchMapHeadToHeadOnSurface.put(slug_combination, allHeadToHeadMatches);
        }

        List<Match> headToHeadMatches = matchFilter(
                allHeadToHeadMatches,
                numberOfLastHeadToHeadMatchesOnSpecificSurface,
                date);

        List<Match> headToHeadMatchesWonByPlayerOne = headToHeadMatches
                .stream()
                .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(player1.getPlayerSlug()))
                .collect(Collectors.toList());

        List<Match> headToHeadMatchesWonByPlayerTwo = headToHeadMatches
                .stream()
                .filter(m -> m.getWinnerPlayer().getPlayerSlug().equals(player2.getPlayerSlug()))
                .collect(Collectors.toList());

        double playerOneWonHeadToHeadPercentage = (headToHeadMatches.size() >= 2) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerOne.size() / (double) headToHeadMatches.size())) : 0.5;
        double playerTwoWonHeadToHeadPercentage = (headToHeadMatches.size() >= 2) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerTwo.size() / (double) headToHeadMatches.size())) : 0.5;

        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false){
            results.add(playerOneWonHeadToHeadPercentage);
            results.add(playerTwoWonHeadToHeadPercentage);
        } else {
            results.add(playerTwoWonHeadToHeadPercentage);
            results.add(playerOneWonHeadToHeadPercentage);
        }

        return results;
    }

    private List<Double> addTourneyWinningRate(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean to_train){
        return this.addMatchWinningRateToResults(results, player1, player1Matches, player2, player2Matches, to_train);
    }

    private List<Double> addServiceAndReturnStatistics(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean to_train){
        List<Stats> player1Stats = ((List<Stats>) statsService.findAllStatsByMatchIds(player1Matches))
                .stream()
                .filter(st -> st != null)
                .collect(Collectors.toList());

        List<Stats> player2Stats = ((List<Stats>) statsService.findAllStatsByMatchIds(player2Matches))
                .stream()
                .filter(st -> st != null)
                .collect(Collectors.toList());

        // ------------------------------------ PLAYER SERVICE PERCENTAGE ----------------------------------------------//
        double player1ServicePercentage = predicterService.getServicePointsWonRate(player1Stats, player1);

        double player2ServicePercentage = predicterService.getServicePointsWonRate(player2Stats, player2);

        // ------------------------------------ PLAYER RETRURN PERCENTAGE ---------------------------------------------//

        double player1ReturnPercentage = predicterService.getReturnPointsWonRate(player1Stats, player1);

        double player2ReturnPercentage = predicterService.getReturnPointsWonRate(player2Stats, player2);

        // ------------------------------------ PLAYER FIRST SERVE IN PERCENTAGE ---------------------------------------//

        double player1FirstServeInPercentage = predicterService.getFirstServeInRate(player1Stats, player1);

        double player2FirstServeInPercentage = predicterService.getFirstServeInRate(player2Stats, player2);

        // ------------------------------------ PLAYER FIRST SERVE WON PERCENTAGE ---------------------------------------//

        double player1FirstServeWonRate = predicterService.getFirstServeWonRate(player1Stats, player1);

        double player2FirstServeWonRate = predicterService.getFirstServeWonRate(player2Stats, player2);

        // ------------------------------------ PLAYER BREAK POINTS CONVERTED ------------------------------------------//

        double player1BreakPointsConvertedRate = predicterService.getBreakPointsConvertedRate(player1Stats, player1);

        double player2BreakPointsConvertedRate = predicterService.getBreakPointsConvertedRate(player2Stats, player2);

        // ------------------------------------ PLAYER BREAK POINTS SAVED ----------------------------------------------//

        double player1BreakPointsSavedRate = predicterService.getBreakPointsSavedRate(player1Stats, player1);

        double player2BreakPointsSavedRate = predicterService.getBreakPointsSavedRate(player2Stats, player2);

        // ------------------------------------ ADD TO INPUTS ----------------------------------------------------------//
        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false){
            results.add(player1ServicePercentage);
            results.add(player2ServicePercentage);
            results.add(player1ReturnPercentage);
            results.add(player2ReturnPercentage);
            results.add(player1FirstServeInPercentage);
            results.add(player2FirstServeInPercentage);
            results.add(player1FirstServeWonRate);
            results.add(player2FirstServeWonRate);
            results.add(player1BreakPointsConvertedRate);
            results.add(player2BreakPointsConvertedRate);
            results.add(player1BreakPointsSavedRate);
            results.add(player2BreakPointsSavedRate);
        } else {
            results.add(player2ServicePercentage);
            results.add(player1ServicePercentage);
            results.add(player2ReturnPercentage);
            results.add(player1ReturnPercentage);
            results.add(player2FirstServeInPercentage);
            results.add(player1FirstServeInPercentage);
            results.add(player2FirstServeWonRate);
            results.add(player1FirstServeWonRate);
            results.add(player2BreakPointsConvertedRate);
            results.add(player1BreakPointsConvertedRate);
            results.add(player2BreakPointsSavedRate);
            results.add(player1BreakPointsSavedRate);
        }

        return results;
    }

    private List<Double> addRoundExperienceAndWinningRate(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, int round_order, boolean to_train){
        player1Matches = player1Matches.stream()
                .filter(m -> m.getRound_order() == round_order)
                .collect(Collectors.toList());
        player2Matches = player2Matches.stream()
                .filter(m -> m.getRound_order() == round_order)
                .collect(Collectors.toList());

        double player1WinningRate = 0.0;
        double player2WinningRate = 0.0;

        if (player1Matches.size() > 0){
            player1WinningRate = convertToPercentage(player1Matches, player1);
        }

        if (player2Matches.size() > 0){
            player2WinningRate = convertToPercentage(player2Matches, player2);
        }

        if (player1.getPlayerSlug().compareTo(player2.getPlayerSlug()) <= 0 || to_train == false) {
            results.add(player1WinningRate);
            results.add(player2WinningRate);
        } else {
            results.add(player2WinningRate);
            results.add(player1WinningRate);
        }
        return results;
    }

    private List<Double> getInputs(Match match){
        try {
            Player winnerPlayer = match.getWinnerPlayer();
            Player loserPlayer = match.getLoserPlayer();
            String date = match.getTournament().getDates();
            String surface = match.getTournament().getTourney().getSurface();
            List<Match> winnerPlayerAllMatches;
            List<Match> loserPlayerAllMatches;
            List<Match> winnerPlayerAllMatchesOnSelectedSurface;
            List<Match> loserPlayerAllMatchesOnSelectedSurface;
            List<Match> winnerPlayerAllMatchesOnTournament;
            List<Match> loserPlayerAllMatchesOnTournament;

            if (matchMap.containsKey(winnerPlayer.getPlayerSlug())) {
                winnerPlayerAllMatches = matchMap.get(winnerPlayer.getPlayerSlug());
            } else {
                winnerPlayerAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(winnerPlayer.getFirstName(), winnerPlayer.getLastName());
                matchMap.put(winnerPlayer.getPlayerSlug(), winnerPlayerAllMatches);
            }
            List<Match> winnerPlayerMatches = matchFilter(winnerPlayerAllMatches, numberOfLastMatches, date);
            if (winnerPlayerMatches.size() < LIMIT_NR_OF_ALL_MATCHES) return null;

            if (matchMap.containsKey(loserPlayer.getPlayerSlug())) {
                loserPlayerAllMatches = matchMap.get(loserPlayer.getPlayerSlug());
            } else {
                loserPlayerAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(loserPlayer.getFirstName(), loserPlayer.getLastName());
                matchMap.put(loserPlayer.getPlayerSlug(), loserPlayerAllMatches);
            }
            List<Match> loserPlayerMatches = matchFilter(loserPlayerAllMatches, numberOfLastMatches, date);
            if (loserPlayerMatches.size() < LIMIT_NR_OF_ALL_MATCHES) return  null;

            if (matchMapOnSurface.containsKey(winnerPlayer.getPlayerSlug() + surface)) {
                winnerPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(winnerPlayer.getPlayerSlug() + surface);
            } else {
                winnerPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), surface);
                matchMapOnSurface.put(winnerPlayer.getPlayerSlug() + surface, winnerPlayerAllMatchesOnSelectedSurface);
            }
            List<Match> winnerPlayerMatchesOnSurface = matchFilter(winnerPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date);
            if (winnerPlayerMatchesOnSurface.size() < LIMIT_NR_OF_ALL_SURFACE_MATCHES) return null;

            if (matchMapOnSurface.containsKey(loserPlayer.getPlayerSlug() + surface)) {
                loserPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(loserPlayer.getPlayerSlug() + surface);
            } else {
                loserPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(loserPlayer.getFirstName(), loserPlayer.getLastName(), surface);
                matchMapOnSurface.put(loserPlayer.getPlayerSlug() + surface, loserPlayerAllMatchesOnSelectedSurface);
            }
            List<Match> loserPlayerMatchesOnSurface = matchFilter(loserPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date);
            if (loserPlayerMatchesOnSurface.size() < LIMIT_NR_OF_ALL_SURFACE_MATCHES) return null;

            if (matchMapOnTournament.containsKey(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug())) {
                winnerPlayerAllMatchesOnTournament = matchMapOnTournament.get(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug());
            } else {
                winnerPlayerAllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), match.getTournament().getTourney().getName());
                matchMapOnTournament.put(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug(), winnerPlayerAllMatchesOnTournament);
            }
            List<Match> winnerPlayerMatchesOnTournament = matchFilter(winnerPlayerAllMatchesOnTournament, numberOfLastMatchesOnTournament, date);
            if (winnerPlayerMatchesOnTournament.size() < LIMIT_NR_OF_ALL_TOURNAMENT_MATCHES) return null;

            if (matchMapOnTournament.containsKey(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug())) {
                loserPlayerAllMatchesOnTournament = matchMapOnTournament.get(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug());
            } else {
                loserPlayerAllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(loserPlayer.getFirstName(), loserPlayer.getLastName(), match.getTournament().getTourney().getName());
                matchMapOnTournament.put(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug(), loserPlayerAllMatchesOnTournament);
            }
            List<Match> loserPlayerMatchesOnTournament = matchFilter(loserPlayerAllMatchesOnTournament, numberOfLastMatchesOnTournament, date);
            if (loserPlayerMatchesOnTournament.size() < LIMIT_NR_OF_ALL_TOURNAMENT_MATCHES) return null;

            List<Double> list = new ArrayList<Double>();

            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, true);
            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, true);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, true);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, true);  // wip
            list = addHeadToHeadMatchesWinningRateToResults(list, winnerPlayer, loserPlayer, date, true);
            list = addHeadToHeadMatchesOnSurfaceWinningRateToResults(list, winnerPlayer, loserPlayer, date, surface, true);
            list = addTourneyWinningRate(list, winnerPlayer, winnerPlayerMatchesOnTournament, loserPlayer, loserPlayerMatchesOnTournament, true);
            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, true);
            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, true);
            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerAllMatchesOnTournament, loserPlayer, loserPlayerAllMatchesOnTournament, true);

            list = addRoundExperienceAndWinningRate(list, winnerPlayer, winnerPlayerAllMatches, loserPlayer, loserPlayerAllMatches, match.getRound_order(), true);
            list = addRoundExperienceAndWinningRate(list, winnerPlayer, winnerPlayerAllMatchesOnSelectedSurface, loserPlayer, loserPlayerAllMatchesOnSelectedSurface, match.getRound_order(), true);
            list = addRoundExperienceAndWinningRate(list, winnerPlayer,winnerPlayerAllMatchesOnTournament, loserPlayer, loserPlayerAllMatchesOnTournament, match.getRound_order(), true);

            return  list;

        } catch (Exception e){
            System.out.println("Something went wrong  during the convertion to input data... " + e.getMessage());
            return null;
        }

    }

    private List<Double> getInputsToPredict(Player playerOne, Player playerTwo, String surface, String tourneyName){
            int round_order = 1;

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

            List<Match> player1AllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(playerOne.getFirstName(), playerOne.getLastName(), tourneyName);
            List<Match> player2AllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(playerTwo.getFirstName(), playerTwo.getLastName(), tourneyName);

            List<Double> list = new ArrayList<Double>();

            list = addMatchWinningRateToResults(list, playerOne, playerOneMatches, playerTwo, playerTwoMatches, false);
            list = addMatchWinningRateToResults(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, false);
            list = addGameAndSetWinningRatesToResults(list, playerOne, playerOneMatches, playerTwo, playerTwoMatches, false);
            list = addGameAndSetWinningRatesToResults(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, false); // wip
            list = addHeadToHeadMatchesWinningRateToResults(list, playerOne, playerTwo, "2099.01.01", false);
            list = addHeadToHeadMatchesOnSurfaceWinningRateToResults(list, playerOne, playerTwo, "2099.01.01", surface, false);
            list = addTourneyWinningRate(list, playerOne, player1AllMatchesOnTournament, playerTwo, player2AllMatchesOnTournament, false);
            list = addServiceAndReturnStatistics(list, playerOne, playerOneMatches, playerTwo, playerOneMatches, false);
            list = addServiceAndReturnStatistics(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, false);
            list = addServiceAndReturnStatistics(list, playerOne, player1AllMatchesOnTournament, playerTwo, player2AllMatchesOnTournament, false);

            list = addRoundExperienceAndWinningRate(list, playerOne, playerOneAllMatches, playerTwo, playerTwoAllMatches, round_order, true);
            list = addRoundExperienceAndWinningRate(list, playerOne, playerOneAllMatchesOnSelectedSurface, playerTwo, playerTwoAllMatchesOnSelectedSurface, round_order, true);
            list = addRoundExperienceAndWinningRate(list, playerOne, player1AllMatchesOnTournament, playerTwo, player2AllMatchesOnTournament, round_order, true);

        return  list;
    }

    @GetMapping("/training")
    @ResponseBody
    public void training(@RequestParam String version, @RequestParam String description){
        List<Match> allMatches = (List<Match>) matchService.findAll();
        List<TrainData> trainData;
        String training_url = "http://localhost:5000/training";
        RestTemplate restTemplate = new RestTemplate();

        System.out.print("Starting training ...");
        System.out.println(predicterService.getCurrentDate(new Date()));
            trainData = getTrainData(allMatches);
        System.out.print("The training ended = ");
        System.out.println(predicterService.getCurrentDate(new Date()));

        System.out.println("Number of training data = " + trainData.size());

        String training_data_filename = "training-data-" + version.replaceAll("/\\s\\s+/g", "-") + ".txt";
        String weight_filename = "weights-" + version + ".txt";
        String biases_filename = "biases-" + version + ".txt";

            TrainingDataToJSONConverter.writeToJSONFile(trainData, training_data_filename);

        int nrOfInputs = trainData.get(0).getInputs().size();
        System.out.println("Nr of inputs = " + nrOfInputs);
        TrainingRequestDTO requestDTO = new TrainingRequestDTO(training_data_filename, weight_filename, biases_filename, true, nrOfInputs);
        HttpEntity<TrainingRequestDTO> request = new HttpEntity<>(requestDTO);
        ResponseEntity<TrainingResponseDTO> response = restTemplate.postForEntity(training_url, request, TrainingResponseDTO.class);
        TrainingResponseDTO responseDTO  = (TrainingResponseDTO) response.getBody();
        TrainingDescription trainingDescription = new TrainingDescription(version, description, predicterService.getCurrentDate(new Date()), responseDTO.getHighest_percentage());
        trainingDescriptionService.save(trainingDescription);

        for (double percentage: responseDTO.getHistory()) {
            TrainingResultsData resultsData = new TrainingResultsData();
            resultsData.setPercentage(percentage);
            resultsData.setTrainingDescription(trainingDescription);
            trainingResultsDataService.save(resultsData);
        }
    }


    @GetMapping("/calculate")
    @ResponseBody
    public List<Integer> calculateProbability(@RequestParam String playerOneSlug, @RequestParam String playerTwoSlug, @RequestParam String surface, @RequestParam String tourneyName, @RequestParam String nrOfAllCheckedMatches, @RequestParam String nrOfCheckedMatchesOnSelectedSurface, @RequestParam String nrOfHeadToHeadMatches){
        System.out.println("I've got the request to PREDICT... " + playerOneSlug + " vs " + playerTwoSlug + " on: " + tourneyName + ", " + surface);
        RestTemplate restTemplate = new RestTemplate();
        String URL = "http://localhost:5000/prediction";

        Player playerOne = playerService.findBySlug(playerOneSlug);
        Player playerTwo = playerService.findBySlug(playerTwoSlug);

        // Later we can receive these file names from the user => new webpage
        List<Double> inputs = getInputsToPredict(playerOne, playerTwo, surface, tourneyName);
        String weight_filename = "weights-1991-2016-stats-all-surface-tournament-mental-matches-2.txt";
        String biases_filename = "biases-1991-2016-stats-all-surface-tournament-mental-matches-2.txt";

        PredictionRequestDTO requestDTO = new PredictionRequestDTO(playerOne.getPlayerSlug(), playerTwo.getPlayerSlug(), weight_filename, biases_filename, getInputsToPredict(playerOne, playerTwo, surface, tourneyName));
        HttpEntity<PredictionRequestDTO> request = new HttpEntity<>(requestDTO);
        ResponseEntity<PredictionResponseDTO> response;

       try{
           response = restTemplate.postForEntity(URL, request, PredictionResponseDTO.class);
           PredictionResponseDTO responseDTO = response.getBody();
           return predicterService.convertSumToOneHundredPercent(responseDTO.getFirst_percentage(), responseDTO.getSecond_percentage());
       } catch (Exception e){
           System.out.println(e);
           return Arrays.asList(50, 50);
       }
    }
}
