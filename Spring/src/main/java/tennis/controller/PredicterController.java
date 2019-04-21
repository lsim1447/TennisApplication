package tennis.controller;

import jep.Jep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.Match;
import tennis.domain.Player;
import tennis.model.TrainData;
import tennis.service.*;
import tennis.utils.python.MyPythonInterpreter;
import tennis.utils.python.TrainingDataToJSONConverter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prediction")
@CrossOrigin(origins = "http://localhost:3000")
public class PredicterController {

    private Comparator<Match> matchComparator;
    private HashMap<String, List<Match>> matchMap = new HashMap<>();
    private HashMap<String, List<Match>> matchMapOnSurface = new HashMap<>();

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
    private DecimalFormat df = new DecimalFormat("####0.00");

    private double convertToPercentage(List<Match> matches, Player player){
        long counter = matches.stream()
                .filter(match -> match.getWinnerPlayer().getPlayer_id() == player.getPlayer_id())
                .count();

        return (double) counter / (double) matches.size();
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

            List<Match> winnerPlayerMatches = winnerPlayerAllMatches
                    .stream()
                    .filter(m -> m.getTournament().getDates().compareTo(date) <= 0)
                    .sorted(matchComparator)
                    .limit(numberOfLastMatches)
                    .collect(Collectors.toList());

            List<Match> loserPlayerMatches = loserPlayerAllMatches
                    .stream()
                    .filter(m -> m.getTournament().getDates().compareTo(date) <= 0)
                    .sorted(matchComparator)
                    .limit(numberOfLastMatches)
                    .collect(Collectors.toList());


            List<Match> winnerPlayerMatchesOnSurface = winnerPlayerAllMatchesOnSelectedSurface
                    .stream()
                    .filter(m -> m.getTournament().getDates().compareTo(date) <= 0)
                    .sorted(matchComparator)
                    .limit(numberOfLastMatchesOnSpecificSurface)
                    .collect(Collectors.toList());

            List<Match> loserPlayerMatchesOnSurface = loserPlayerAllMatchesOnSelectedSurface
                    .stream()
                    .filter(m -> m.getTournament().getDates().compareTo(date) <= 0)
                    .sorted(matchComparator)
                    .limit(numberOfLastMatchesOnSpecificSurface)
                    .collect(Collectors.toList());

            if (winnerPlayerMatches.size() < numberOfLastMatches || loserPlayerMatches.size() < numberOfLastMatches || winnerPlayerMatchesOnSurface.size() < numberOfLastMatchesOnSpecificSurface || loserPlayerMatchesOnSurface.size() < numberOfLastMatchesOnSpecificSurface) return null;

            double winnerPlayeWonPercentageFromAll = convertToPercentage(winnerPlayerMatches, winnerPlayer);
            double loserPlayeWonPercentageFromAll = convertToPercentage(loserPlayerMatches, loserPlayer);
            double winnerPlayeWonPercentageFromSelectedSurface = convertToPercentage(winnerPlayerMatchesOnSurface, winnerPlayer);
            double loserPlayeWonPercentageFromSelectedSurface = convertToPercentage(loserPlayerMatchesOnSurface, loserPlayer);

            // WON SETS PERCENTAGES
            double nrOfSets = (double) (match.getWinner_sets_won() + match.getLoser_sets_won());
            double winnerPlayerWonSetsPercentage = Double.parseDouble(df.format(match.getWinner_sets_won() / nrOfSets));
            double loserPlayerWonSetsPercentage  = Double.parseDouble(df.format(match.getLoser_sets_won() / nrOfSets));

            // WON GAMES PERCENTAGES
            double nrOfGames = (double) (match.getWinner_games_won() + match.getLoser_games_won());
            double winnerPlayerWonGamesPercentage = Double.parseDouble(df.format(match.getWinner_games_won() / nrOfGames));
            double loserPlayerWonGamesPercentage  = Double.parseDouble(df.format(match.getLoser_games_won() / nrOfGames));

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
            } else {
                list.add(loserPlayeWonPercentageFromAll);
                list.add(winnerPlayeWonPercentageFromAll);
                list.add(loserPlayeWonPercentageFromSelectedSurface);
                list.add(winnerPlayeWonPercentageFromSelectedSurface);
                list.add(loserPlayerWonSetsPercentage);
                list.add(winnerPlayerWonSetsPercentage);
                list.add(loserPlayerWonGamesPercentage);
                list.add(winnerPlayerWonGamesPercentage);
            }

            return  list;

        } catch (Exception e){
            return null;
        }

    }

    @GetMapping("/training")
    @ResponseBody
    public void training(){
        List<Match> allMatches = (List<Match>) matchService.findAll();
        List<TrainData> trainData;

        long startTime = System.nanoTime();
            trainData = getTrainData(allMatches);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Duration of creating training data = " + duration);
        System.out.println("Training data length = " + trainData.size());

        TrainingDataToJSONConverter.writeToJSONFile(trainData, "training-data-extended.txt");
    }

    @GetMapping("/calculate")
    @ResponseBody
    public List<TrainData> calculateProbability(@RequestParam String playerOneSlug, @RequestParam String playerTwoSlug, @RequestParam String surface, @RequestParam String tourneyName, @RequestParam String nrOfCheckedElements){
        int nr = Integer.parseInt(nrOfCheckedElements);
        int nrCheckedDuel = 5;

        Player player1 = playerService.findBySlug(playerOneSlug);
        Player player2 = playerService.findBySlug(playerTwoSlug);

        List<Match> firstPlayerMatches  =  matchService.findLastNMatches(player1.getPlayerSlug(), nr);
        List<Match> secondPlayerMatches =  matchService.findLastNMatches(player2.getPlayerSlug(), nr);

        List<Match> allMatches = (List<Match>) matchService.findAll();

        List<TrainData> trainData = new ArrayList<>();
        long startTime = System.nanoTime();
        trainData = getTrainData(allMatches);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Duration of creating training data = " + duration);
        System.out.println("Training data length = " + trainData.size());

        TrainingDataToJSONConverter.writeToJSONFile(trainData, "training-data-extended.txt");

        //MyPythonInterpreter pythonInterpreter = new MyPythonInterpreter("Training", "learn", "training.py");
        //pythonInterpreter.executeScript();

        return trainData;
    }
}
