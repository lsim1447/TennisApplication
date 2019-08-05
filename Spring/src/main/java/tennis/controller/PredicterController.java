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

        return (matches.size() > 0) ? Double.parseDouble(df.format((double) counter / (double) matches.size())) : 0.0;
    }

    private boolean orderCondition(Match match){
        String date = match.getTournament().getDates();
        int day = Integer.parseInt(date.length() > 2 ? date.substring(date.length() - 2) : "0");
        int p1 = Integer.parseInt(match.getWinnerPlayer().getPlayer_id().length() > 1 ? match.getWinnerPlayer().getPlayer_id().substring(match.getWinnerPlayer().getPlayer_id().length() - 1) : "0");
        int p2 = Integer.parseInt(match.getLoserPlayer().getPlayer_id().length() > 1 ? match.getLoserPlayer().getPlayer_id().substring(match.getLoserPlayer().getPlayer_id().length() - 1) : "0");
        boolean returnValue = (day + p1 + p2) % 2 == 0;
        return returnValue;
    }

    private List<Match> matchFilter(List<Match> matches, int limit, String date){
        return matches
                .stream()
                .filter(m -> m.getTournament().getDates().compareTo(date) < 0)
                .sorted(matchComparator)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<Match> matchFilter(List<Match> matches, int limit, String date, int roundOrder){
        return matches
                .stream()
                .filter(m -> m.getTournament().getDates().compareTo(date) <= 0)
                .filter(m -> m.getTournament().getDates().compareTo(date) != 0 || m.getRound_order() > roundOrder)
                .sorted(matchComparator)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<Integer> getOutputs(Match match){
        Player winnerPlayer = match.getWinnerPlayer();
        Player loserPlayer = match.getLoserPlayer();

        List<Integer> list = new ArrayList<Integer>();

        if (orderCondition(match)){
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

    private List<TrainData> getTestData(List<Match> matches){
        return matches.stream()
                .filter(match -> getInputsToTest(match) != null)
                .map(match -> new TrainData(getInputsToTest(match), getOutputs(match)))
                .collect(Collectors.toList());
    }

    private List<Double> addHeadToHeadMatchesWinningRateToResults(List<Double> results, Player player1, Player player2, String date, boolean order){

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

        double playerOneWonHeadToHeadPercentage = (headToHeadMatches.size() >= 3) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerOne.size() / (double)headToHeadMatches.size())) : 0.0;
        double playerTwoWonHeadToHeadPercentage = (headToHeadMatches.size() >= 3) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerTwo.size() / (double)headToHeadMatches.size())) : 0.0;

        if (order){
            results.add(playerOneWonHeadToHeadPercentage);
            results.add(playerTwoWonHeadToHeadPercentage);
        } else {
            results.add(playerTwoWonHeadToHeadPercentage);
            results.add(playerOneWonHeadToHeadPercentage);
        }

        return results;
    }

    private List<Double> addMatchWinningRateToResults(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean order){

        double player1Percentage    = convertToPercentage(player1Matches, player1);
        double player2Percentage = convertToPercentage(player2Matches, player2);

        if (order){
            results.add(player1Percentage);
            results.add(player2Percentage);
        } else {
            results.add(player2Percentage);
            results.add(player1Percentage);
        }

        return results;
    }

    private List<Double> addGameAndSetWinningRatesToResults(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean order){

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

        double playerOneWonSetsPercentage  = (player1Matches.size() > 0) ? Double.parseDouble(df.format(sum_player_one_set_percentages / player1Matches.size())) : 0.0;
        double playerTwoWonSetsPercentage   = (player2Matches.size() > 0) ? Double.parseDouble(df.format(sum_player_two_set_percentages / player2Matches.size())) : 0.0;
        double playerOneWonGamesPercentage = (player1Matches.size() > 0) ? Double.parseDouble(df.format(sum_player_one_games_percentages /  player1Matches.size())) : 0.0;
        double playerTwoWonGamesPercentage  = (player2Matches.size() > 0) ? Double.parseDouble(df.format(sum_player_two_games_percentages / player2Matches.size())) : 0.0;

        if (order){
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

    private List<Double> addHeadToHeadMatchesOnSurfaceWinningRateToResults(List<Double> results, Player player1, Player player2, String date, String surface, boolean order){

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

        double playerOneWonHeadToHeadPercentage = (headToHeadMatches.size() >= 2) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerOne.size() / (double) headToHeadMatches.size())) : 0.0;
        double playerTwoWonHeadToHeadPercentage = (headToHeadMatches.size() >= 2) ? Double.parseDouble(df.format(headToHeadMatchesWonByPlayerTwo.size() / (double) headToHeadMatches.size())) : 0.0;

        if (order){
            results.add(playerOneWonHeadToHeadPercentage);
            results.add(playerTwoWonHeadToHeadPercentage);
        } else {
            results.add(playerTwoWonHeadToHeadPercentage);
            results.add(playerOneWonHeadToHeadPercentage);
        }

        return results;
    }

    private List<Double> addTourneyWinningRate(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean order){
        return this.addMatchWinningRateToResults(results, player1, player1Matches, player2, player2Matches, order);
    }

    private List<Double> addServiceAndReturnStatistics(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, boolean order){
        List<Stats> player1Stats = ((List<Stats>) statsService.findAllStatsByMatchIds(player1Matches))
                .stream()
                .filter(st -> st != null)
                .collect(Collectors.toList());

        List<Stats> player2Stats = ((List<Stats>) statsService.findAllStatsByMatchIds(player2Matches))
                .stream()
                .filter(st -> st != null)
                .collect(Collectors.toList());

        double player1ServicePercentage = 0.0;
        double player2ServicePercentage = 0.0;
        double player1ReturnPercentage = 0.0;
        double player2ReturnPercentage = 0.0;
        double player1FirstServeInPercentage = 0.0;
        double player2FirstServeInPercentage = 0.0;
        double player1FirstServeWonRate = 0.0;
        double player2FirstServeWonRate = 0.0;
        double player1BreakPointsConvertedRate = 0.0;
        double player2BreakPointsConvertedRate = 0.0;
        double player1BreakPointsSavedRate = 0.0;
        double player2BreakPointsSavedRate = 0.0;
        double player1SecondServePointsWonRate = 0.0;
        double player2SecondServePointsWonRate = 0.0;
        double player1SecondServeReturnWonRate = 0.0;
        double player2SecondServeReturnWonRate = 0.0;

        if (player1Stats.size() != 0){
            player1ServicePercentage = predicterService.getServicePointsWonRate(player1Stats, player1);
            player1ReturnPercentage = predicterService.getReturnPointsWonRate(player1Stats, player1);
            player1FirstServeInPercentage = predicterService.getFirstServeInRate(player1Stats, player1);
            player1FirstServeWonRate = predicterService.getFirstServeWonRate(player1Stats, player1);
            player1BreakPointsConvertedRate = predicterService.getBreakPointsConvertedRate(player1Stats, player1);
            player1BreakPointsSavedRate = predicterService.getBreakPointsSavedRate(player1Stats, player1);
            player1SecondServePointsWonRate = predicterService.getSecondServePointsWonRate(player1Stats, player1);
            player1SecondServeReturnWonRate = predicterService.getSecondServeReturnWonRate(player1Stats, player1);
        }

        if (player2Stats.size() != 0){
            player2ServicePercentage = predicterService.getServicePointsWonRate(player2Stats, player2);
            player2ReturnPercentage = predicterService.getReturnPointsWonRate(player2Stats, player2);
            player2FirstServeInPercentage = predicterService.getFirstServeInRate(player2Stats, player2);
            player2FirstServeWonRate = predicterService.getFirstServeWonRate(player2Stats, player2);
            player2BreakPointsConvertedRate = predicterService.getBreakPointsConvertedRate(player2Stats, player2);
            player2BreakPointsSavedRate = predicterService.getBreakPointsSavedRate(player2Stats, player2);
            player2SecondServePointsWonRate = predicterService.getSecondServePointsWonRate(player2Stats, player2);
            player2SecondServeReturnWonRate = predicterService.getSecondServeReturnWonRate(player1Stats, player2);
        }

        if (order){
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
            results.add(player1SecondServePointsWonRate);
            results.add(player2SecondServePointsWonRate);
            results.add(player1SecondServeReturnWonRate);
            results.add(player2SecondServeReturnWonRate);
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
            results.add(player2SecondServePointsWonRate);
            results.add(player1SecondServePointsWonRate);
            results.add(player2SecondServeReturnWonRate);
            results.add(player1SecondServeReturnWonRate);
        }

        return results;
    }

    private List<Double> addRoundExperienceAndWinningRate(List<Double> results, Player player1, List<Match> player1Matches, Player player2, List<Match> player2Matches, int round_order, boolean order){
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

        if (order){
            results.add(player1WinningRate);
            results.add(player2WinningRate);
        } else {
            results.add(player2WinningRate);
            results.add(player1WinningRate);
        }
        return results;
    }

    private List<Double> addMatchDurationAvg(List<Double> results, Player player1, Player player2, Tournament tournament, int roundOrder, boolean order){
        List<Match> playerOneMatchesOnTournament =
                ((List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear(player1.getFirstName(), player1.getLastName(), tournament.getTourney().getName(), tournament.getTourney().getSurface(), tournament.getYear()))
                .stream()
                .filter(match -> match.getRound_order() > roundOrder)
                .collect(Collectors.toList());
        List<Match> playerTwoMatchesOnTournament =
                ((List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear(player2.getFirstName(), player2.getLastName(), tournament.getTourney().getName(), tournament.getTourney().getSurface(), tournament.getYear()))
                        .stream()
                        .filter(match -> match.getRound_order() > roundOrder)
                        .collect(Collectors.toList());

        List<Stats> playerOneStats = ((List<Stats>) statsService.findAllStatsByMatchIds(playerOneMatchesOnTournament))
                .stream()
                .filter(stats -> stats != null)
                .collect(Collectors.toList());
        List<Stats> playerTwoStats = ((List<Stats>) statsService.findAllStatsByMatchIds(playerTwoMatchesOnTournament))
                .stream()
                .filter(stats -> stats != null)
                .collect(Collectors.toList());

        double playerOnePercentage = 0.0;
        double playerTwoPercentage = 0.0;

        if (playerOneStats.size() != 0 && playerTwoStats.size() != 0) {
            double sum1 = playerOneStats
                    .stream()
                    .map(stats -> (double) Integer.parseInt(stats.getMatch_duration()))
                    .reduce(0.00, (a, b) -> a + b);
            double sum2 = playerTwoStats
                    .stream()
                    .map(stats -> (double) Integer.parseInt(stats.getMatch_duration()))
                    .reduce(0.00, (a, b) -> a + b);
            playerOnePercentage = Double.parseDouble(df.format(sum2 / (sum1 + sum2)));
            playerTwoPercentage = Double.parseDouble(df.format(sum1 / (sum1 + sum2)));
        }

        if (order){
            results.add(playerOnePercentage);
            results.add(playerTwoPercentage);
        } else {
            results.add(playerTwoPercentage);
            results.add(playerOnePercentage);
        }

        return results;
    }

    private List<Double> addServiceAndReturnStatisticsSoFarOnTheTournament(List<Double> results, Player player1, Player player2, Tournament tournament, int roundOrder, boolean order){
        List<Match> playerOneMatchesOnTournament =
                ((List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear(player1.getFirstName(), player1.getLastName(), tournament.getTourney().getName(), tournament.getTourney().getSurface(), tournament.getYear()))
                        .stream()
                        .filter(match -> match.getRound_order() > roundOrder)
                        .collect(Collectors.toList());
        List<Match> playerTwoMatchesOnTournament =
                ((List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear(player2.getFirstName(), player2.getLastName(), tournament.getTourney().getName(), tournament.getTourney().getSurface(), tournament.getYear()))
                        .stream()
                        .filter(match -> match.getRound_order() > roundOrder)
                        .collect(Collectors.toList());

        return addServiceAndReturnStatistics(results, player1, playerOneMatchesOnTournament, player2, playerTwoMatchesOnTournament, order);
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
            List<Match> winnerPlayerMatches = matchFilter(winnerPlayerAllMatches, numberOfLastMatches, date, match.getRound_order());
            if (winnerPlayerMatches.size() < LIMIT_NR_OF_ALL_MATCHES) return null;

            if (matchMap.containsKey(loserPlayer.getPlayerSlug())) {
                loserPlayerAllMatches = matchMap.get(loserPlayer.getPlayerSlug());
            } else {
                loserPlayerAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(loserPlayer.getFirstName(), loserPlayer.getLastName());
                matchMap.put(loserPlayer.getPlayerSlug(), loserPlayerAllMatches);
            }
            List<Match> loserPlayerMatches = matchFilter(loserPlayerAllMatches, numberOfLastMatches, date, match.getRound_order());
            if (loserPlayerMatches.size() < LIMIT_NR_OF_ALL_MATCHES) return  null;

            if (matchMapOnSurface.containsKey(winnerPlayer.getPlayerSlug() + surface)) {
                winnerPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(winnerPlayer.getPlayerSlug() + surface);
            } else {
                winnerPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), surface);
                matchMapOnSurface.put(winnerPlayer.getPlayerSlug() + surface, winnerPlayerAllMatchesOnSelectedSurface);
            }
            List<Match> winnerPlayerMatchesOnSurface = matchFilter(winnerPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date, match.getRound_order());
            if (winnerPlayerMatchesOnSurface.size() < LIMIT_NR_OF_ALL_SURFACE_MATCHES) return null;

            if (matchMapOnSurface.containsKey(loserPlayer.getPlayerSlug() + surface)) {
                loserPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(loserPlayer.getPlayerSlug() + surface);
            } else {
                loserPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(loserPlayer.getFirstName(), loserPlayer.getLastName(), surface);
                matchMapOnSurface.put(loserPlayer.getPlayerSlug() + surface, loserPlayerAllMatchesOnSelectedSurface);
            }
            List<Match> loserPlayerMatchesOnSurface = matchFilter(loserPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date, match.getRound_order());
            if (loserPlayerMatchesOnSurface.size() < LIMIT_NR_OF_ALL_SURFACE_MATCHES) return null;

            if (matchMapOnTournament.containsKey(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug())) {
                winnerPlayerAllMatchesOnTournament = matchMapOnTournament.get(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug());
            } else {
                winnerPlayerAllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), match.getTournament().getTourney().getName());
                matchMapOnTournament.put(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug(), winnerPlayerAllMatchesOnTournament);
            }
            List<Match> winnerPlayerMatchesOnTournament = matchFilter(winnerPlayerAllMatchesOnTournament, numberOfLastMatchesOnTournament, date, match.getRound_order());
            if (winnerPlayerMatchesOnTournament.size() < LIMIT_NR_OF_ALL_TOURNAMENT_MATCHES) return null;

            if (matchMapOnTournament.containsKey(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug())) {
                loserPlayerAllMatchesOnTournament = matchMapOnTournament.get(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug());
            } else {
                loserPlayerAllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(loserPlayer.getFirstName(), loserPlayer.getLastName(), match.getTournament().getTourney().getName());
                matchMapOnTournament.put(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug(), loserPlayerAllMatchesOnTournament);
            }
            List<Match> loserPlayerMatchesOnTournament = matchFilter(loserPlayerAllMatchesOnTournament, numberOfLastMatchesOnTournament, date, match.getRound_order());
            if (loserPlayerMatchesOnTournament.size() < LIMIT_NR_OF_ALL_TOURNAMENT_MATCHES) return null;

            List<Double> list = new ArrayList<Double>();

            boolean order = orderCondition(match);

            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, order);
            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, order);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, order);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, order);  // wip
            list = addHeadToHeadMatchesWinningRateToResults(list, winnerPlayer, loserPlayer, date, order);
            list = addHeadToHeadMatchesOnSurfaceWinningRateToResults(list, winnerPlayer, loserPlayer, date, surface, order);
            list = addTourneyWinningRate(list, winnerPlayer, winnerPlayerMatchesOnTournament, loserPlayer, loserPlayerMatchesOnTournament, order);

            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, order);
            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, order);
            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerAllMatchesOnTournament, loserPlayer, loserPlayerAllMatchesOnTournament, order);

            list = addRoundExperienceAndWinningRate(list, winnerPlayer, winnerPlayerAllMatches, loserPlayer, loserPlayerAllMatches, match.getRound_order(), order);
            list = addRoundExperienceAndWinningRate(list, winnerPlayer, winnerPlayerAllMatchesOnSelectedSurface, loserPlayer, loserPlayerAllMatchesOnSelectedSurface, match.getRound_order(), order);
            list = addRoundExperienceAndWinningRate(list, winnerPlayer,winnerPlayerAllMatchesOnTournament, loserPlayer, loserPlayerAllMatchesOnTournament, match.getRound_order(), order);

            list = addMatchDurationAvg(list, winnerPlayer, loserPlayer, match.getTournament(), match.getRound_order(), order);
            list = addServiceAndReturnStatisticsSoFarOnTheTournament(list, winnerPlayer, loserPlayer, match.getTournament(), match.getRound_order(), order);

            return  list;

        } catch (Exception e){
            System.out.println("Something went wrong  during the convertion to input data... " + e.getMessage());
            return null;
        }

    }

    private List<Double> getInputsToPredict(Player playerOne, Player playerTwo, String surface, String tourneyName){
            int round_order = 1;
            Tourney tourney = tourneyService.findByName(tourneyName);
            List<Tournament> tournaments = (List<Tournament>)tournamentService.getAllTournamentByTourney(tourney);
            Tournament tournament = tournaments.get(tournaments.size() - 1);

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

            boolean order = false;

            list = addMatchWinningRateToResults(list, playerOne, playerOneMatches, playerTwo, playerTwoMatches, order);
            list = addMatchWinningRateToResults(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, order);
            list = addGameAndSetWinningRatesToResults(list, playerOne, playerOneMatches, playerTwo, playerTwoMatches, order);
            list = addGameAndSetWinningRatesToResults(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, order); // wip
            list = addHeadToHeadMatchesWinningRateToResults(list, playerOne, playerTwo, "2099.01.01", order);
            list = addHeadToHeadMatchesOnSurfaceWinningRateToResults(list, playerOne, playerTwo, "2099.01.01", surface, order);
            list = addTourneyWinningRate(list, playerOne, player1AllMatchesOnTournament, playerTwo, player2AllMatchesOnTournament, order);

            list = addServiceAndReturnStatistics(list, playerOne, playerOneMatches, playerTwo, playerOneMatches, order);
            list = addServiceAndReturnStatistics(list, playerOne, playerOneMatchesOnSurface, playerTwo, playerTwoMatchesOnSurface, order);
            list = addServiceAndReturnStatistics(list, playerOne, player1AllMatchesOnTournament, playerTwo, player2AllMatchesOnTournament, order);

            list = addRoundExperienceAndWinningRate(list, playerOne, playerOneAllMatches, playerTwo, playerTwoAllMatches, round_order, order);
            list = addRoundExperienceAndWinningRate(list, playerOne, playerOneAllMatchesOnSelectedSurface, playerTwo, playerTwoAllMatchesOnSelectedSurface, round_order, order);
            list = addRoundExperienceAndWinningRate(list, playerOne, player1AllMatchesOnTournament, playerTwo, player2AllMatchesOnTournament, round_order, order);

            // MATCH DURATION - there is no rhyme or reason to put this into the input values just if we choose the round of the tournament too. - and the date
            list = addMatchDurationAvg(list, playerOne, playerTwo, tournament, 999, order);
            // STATISTICS on the tournament
            list = addServiceAndReturnStatisticsSoFarOnTheTournament(list, playerOne, playerTwo, tournament, 999, order);

        return  list;
    }

    private List<Double> getInputsToTest(Match match){
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
            List<Match> winnerPlayerMatches = matchFilter(winnerPlayerAllMatches, numberOfLastMatches, date, match.getRound_order());

            if (matchMap.containsKey(loserPlayer.getPlayerSlug())) {
                loserPlayerAllMatches = matchMap.get(loserPlayer.getPlayerSlug());
            } else {
                loserPlayerAllMatches = (List<Match>) matchService.findAllMatchesByPlayerName(loserPlayer.getFirstName(), loserPlayer.getLastName());
                matchMap.put(loserPlayer.getPlayerSlug(), loserPlayerAllMatches);
            }
            List<Match> loserPlayerMatches = matchFilter(loserPlayerAllMatches, numberOfLastMatches, date, match.getRound_order());

            if (matchMapOnSurface.containsKey(winnerPlayer.getPlayerSlug() + surface)) {
                winnerPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(winnerPlayer.getPlayerSlug() + surface);
            } else {
                winnerPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), surface);
                matchMapOnSurface.put(winnerPlayer.getPlayerSlug() + surface, winnerPlayerAllMatchesOnSelectedSurface);
            }
            List<Match> winnerPlayerMatchesOnSurface = matchFilter(winnerPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date, match.getRound_order());

            if (matchMapOnSurface.containsKey(loserPlayer.getPlayerSlug() + surface)) {
                loserPlayerAllMatchesOnSelectedSurface = matchMapOnSurface.get(loserPlayer.getPlayerSlug() + surface);
            } else {
                loserPlayerAllMatchesOnSelectedSurface = (List<Match>) matchService.findAllMatchesByPlayerNameAndSurface(loserPlayer.getFirstName(), loserPlayer.getLastName(), surface);
                matchMapOnSurface.put(loserPlayer.getPlayerSlug() + surface, loserPlayerAllMatchesOnSelectedSurface);
            }
            List<Match> loserPlayerMatchesOnSurface = matchFilter(loserPlayerAllMatchesOnSelectedSurface, numberOfLastMatchesOnSpecificSurface, date, match.getRound_order());

            if (matchMapOnTournament.containsKey(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug())) {
                winnerPlayerAllMatchesOnTournament = matchMapOnTournament.get(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug());
            } else {
                winnerPlayerAllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(winnerPlayer.getFirstName(), winnerPlayer.getLastName(), match.getTournament().getTourney().getName());
                matchMapOnTournament.put(winnerPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug(), winnerPlayerAllMatchesOnTournament);
            }
            List<Match> winnerPlayerMatchesOnTournament = matchFilter(winnerPlayerAllMatchesOnTournament, numberOfLastMatchesOnTournament, date, match.getRound_order());

            if (matchMapOnTournament.containsKey(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug())) {
                loserPlayerAllMatchesOnTournament = matchMapOnTournament.get(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug());
            } else {
                loserPlayerAllMatchesOnTournament = (List<Match>) matchService.findAllMatchesByPlayerNameAndTourneyName(loserPlayer.getFirstName(), loserPlayer.getLastName(), match.getTournament().getTourney().getName());
                matchMapOnTournament.put(loserPlayer.getPlayerSlug() + match.getTournament().getTourney().getSlug(), loserPlayerAllMatchesOnTournament);
            }
            List<Match> loserPlayerMatchesOnTournament = matchFilter(loserPlayerAllMatchesOnTournament, numberOfLastMatchesOnTournament, date, match.getRound_order());

            boolean order = orderCondition(match);

            List<Double> list = new ArrayList<Double>();
            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, order);
            list = addMatchWinningRateToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, order);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, order);
            list = addGameAndSetWinningRatesToResults(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, order);  // wip
            list = addHeadToHeadMatchesWinningRateToResults(list, winnerPlayer, loserPlayer, date, order);
            list = addHeadToHeadMatchesOnSurfaceWinningRateToResults(list, winnerPlayer, loserPlayer, date, surface, order);
            list = addTourneyWinningRate(list, winnerPlayer, winnerPlayerMatchesOnTournament, loserPlayer, loserPlayerMatchesOnTournament, order);


            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerMatches, loserPlayer, loserPlayerMatches, order);
            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerMatchesOnSurface, loserPlayer, loserPlayerMatchesOnSurface, order);
            list = addServiceAndReturnStatistics(list, winnerPlayer, winnerPlayerAllMatchesOnTournament, loserPlayer, loserPlayerAllMatchesOnTournament, order);

            list = addRoundExperienceAndWinningRate(list, winnerPlayer, winnerPlayerAllMatches, loserPlayer, loserPlayerAllMatches, match.getRound_order(), order);
            list = addRoundExperienceAndWinningRate(list, winnerPlayer, winnerPlayerAllMatchesOnSelectedSurface, loserPlayer, loserPlayerAllMatchesOnSelectedSurface, match.getRound_order(), order);
            list = addRoundExperienceAndWinningRate(list, winnerPlayer,winnerPlayerAllMatchesOnTournament, loserPlayer, loserPlayerAllMatchesOnTournament, match.getRound_order(), order);

            list = addMatchDurationAvg(list, winnerPlayer, loserPlayer, match.getTournament(), match.getRound_order(), order);
            list = addServiceAndReturnStatisticsSoFarOnTheTournament(list, winnerPlayer, loserPlayer, match.getTournament(), match.getRound_order(), order);

            return  list;

        } catch (Exception e){
            System.out.println("Something went wrong  during the convertion to input data... " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public TestResponseDTO test(){
        List<Match> allMatchesForTest =
                ((List<Match>) matchService.findAll())
                .stream()
                .filter(match -> match.getTournament().getYear() == 2017)
                .collect(Collectors.toList());

        List<TrainData> testData;
        String training_url = "http://localhost:5000/test";
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("All number of data = " + allMatchesForTest.size());
            testData = getTestData(allMatchesForTest);
        System.out.println("Number of test data = " + testData.size());
        System.out.println("Number of input data = " + testData.size());
        String test_data_filename = "test-data-v2-with-all-90-inputs-2017.txt";
        String weights_filename = "weights-version-all-included-actual-tournaments-statistics.txt";
        String biases_filename = "biases-version-all-included-actual-tournaments-statistics.txt";

        TrainingDataToJSONConverter.writeToJSONFile(testData, test_data_filename);

        int nrOfTestInputs = testData.get(0).getInputs().size();
        System.out.println("Nr of test inputs = " + nrOfTestInputs);

        TestRequestDTO requestDTO = new TestRequestDTO(test_data_filename, weights_filename, biases_filename, nrOfTestInputs);
        HttpEntity<TestRequestDTO> request = new HttpEntity<>(requestDTO);
        ResponseEntity<TestResponseDTO> response = restTemplate.postForEntity(training_url, request, TestResponseDTO.class);
        TestResponseDTO responseDTO  = (TestResponseDTO) response.getBody();

        System.out.println("Message = " + responseDTO.getMessage());
        return responseDTO;
    }

    @GetMapping("/training")
    @ResponseBody
    public void train(@RequestParam String version, @RequestParam String description){
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
        String weight_filename = "weights-version-all-included-actual-tournaments-statistics.txt";
        String biases_filename = "biases-version-all-included-actual-tournaments-statistics.txt";

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
