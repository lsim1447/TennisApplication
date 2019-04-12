package tennis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennis.domain.Match;
import tennis.domain.Player;
import tennis.domain.Tournament;
import tennis.repository.MatchRepository;
import tennis.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchesRepository;

    @Autowired
    private PlayerRepository playerRepository;

    // REUSABLE FILTERS
    private boolean filterBySelectedYear(Match match, int year){
        try {
            String ID = match.getMatch_id().split("-")[0];
            return (year == Integer.parseInt(ID));
        } catch (Exception e){
            return false;
        }
    }

    private boolean filterByTourneyName(Match match, String tourneyName){
        try {
            return match.getTournament().getTourney().getName().equals(tourneyName);
        } catch (Exception e){
            return false;
        }
    }

    private boolean filterBySurface(Match match, String surface){
        try {
            return match.getTournament().getTourney().getSurface().equals(surface);
        } catch (Exception e){
            return false;
        }
    }

    // DEFAULT METHODS
    public Match findMatchById(String id){ return matchesRepository.findById(id).orElse(new Match());}

    public Iterable<Match> findAll(){ return matchesRepository.findAll(); }

    public Match save(Match match){ return matchesRepository.save(match); }

    public Iterable<Match> saveAll(Iterable<Match> matches) { return matchesRepository.saveAll(matches); }

    public void deleteMatchById(String id) { matchesRepository.deleteById(id); }


    // TWO BASIC CASES
    public Iterable<Match> findAllMatchesByPlayerName(String firstName, String lastName){
        Player player = playerRepository.findByFirstNameAndLastName(firstName, lastName);
        List<Match> matches = (List<Match>) matchesRepository.findAllByWinnerPlayerOrLoserPlayer(player, player);
        return matches;
    }

    public Iterable<Match> findAllMatchesBetweenTwoPlayer(String firstName1, String lastName1, String firstName2, String lastName2){
        Player player1 = playerRepository.findByFirstNameAndLastName(firstName1, lastName1);
        Player player2 = playerRepository.findByFirstNameAndLastName(firstName2, lastName2);

        List<Match> first_round = (List<Match>) matchesRepository.findAllByWinnerPlayerAndLoserPlayer(player1, player2);
        List<Match> second_round = (List<Match>) matchesRepository.findAllByWinnerPlayerAndLoserPlayer(player2, player1);

        first_round.addAll(second_round);
        return first_round.stream()
                .sorted(Comparator.comparing(match -> match.getTournament().getDates(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    // FILTER BY SELECTED YEAR
    public Iterable<Match> findAllMatchesByPlayerNameInSelectedYear(String firstName, String lastName, int year){
        List<Match> matches = (List<Match>) findAllMatchesByPlayerName(firstName, lastName);
        return matches.stream().filter(match -> filterBySelectedYear(match, year)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesBetweenTwoPlayerInSelectedYear(String firstName1, String lastName1, String firstName2, String lastName2, int year){
        List<Match> matches = (List<Match>)findAllMatchesBetweenTwoPlayer(firstName1, lastName1, firstName2, lastName2);
        return matches.stream().filter(match -> filterBySelectedYear(match, year)).collect(Collectors.toList());
    }

    // FILTER BY SELECTED TOURNEY
    public Iterable<Match> findAllMatchesByPlayerNameAndTourneyName(String firstName, String lastName, String tourneyName){
        List<Match> matches = (List<Match>) findAllMatchesByPlayerName(firstName, lastName);
        return matches.stream().filter(match -> filterByTourneyName(match, tourneyName)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesBetweenTwoPlayerAndTourneyName(String firstName1, String lastName1, String firstName2, String lastName2, String tourneyName){
        List<Match> matches = (List<Match>) findAllMatchesBetweenTwoPlayer(firstName1, lastName1, firstName2, lastName2);
        return matches.stream().filter(match -> filterByTourneyName(match, tourneyName)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyName(String firstName1, String lastName1, String firstName2, String lastName2, String tourneyName, int year){
        List<Match> matches = (List<Match>) findAllMatchesBetweenTwoPlayerInSelectedYear(firstName1, lastName1, firstName2, lastName2, year);
        return matches.stream().filter(match -> filterByTourneyName(match, tourneyName)).collect(Collectors.toList());
    }

    // FILTER BY SELECTED SURFACE
    public Iterable<Match> findAllMatchesByPlayerNameAndSurface(String firstName, String lastName, String surface){
        List<Match> matches = (List<Match>) findAllMatchesByPlayerName(firstName, lastName);
        return matches.stream().filter(match -> filterBySurface(match, surface)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesByPlayerNameAndTourneyNameAndSurface(String firstName, String lastName, String tourneyName, String surface){
        List<Match> matches = (List<Match>) findAllMatchesByPlayerNameAndTourneyName(firstName, lastName, tourneyName);
        return matches.stream().filter(match -> filterBySurface(match, surface)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesByPlayerNameAndTourneyNameAndSurfaceInSelectedYear(String firstName, String lastName, String tourneyName, String surface, int year){
        List<Match> matches = (List<Match>) findAllMatchesByPlayerNameAndTourneyNameAndSurface(firstName, lastName, tourneyName, surface);
        return matches.stream().filter(match -> filterBySelectedYear(match, year)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesBetweenTwoPlayerAndSurface(String firstName1, String lastName1, String firstName2, String lastName2, String surface){
        List<Match> matches = (List<Match>) findAllMatchesBetweenTwoPlayer(firstName1, lastName1, firstName2, lastName2);
        return matches.stream().filter(match -> filterBySurface(match, surface)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesBetweenTwoPlayerAndTourneyNameAndSurface(String firstName1, String lastName1, String firstName2, String lastName2, String tourneyName, String surface){
        List<Match> matches = (List<Match>) findAllMatchesBetweenTwoPlayerAndTourneyName(firstName1, lastName1, firstName2, lastName2, tourneyName);
        return matches.stream().filter(match -> filterBySurface(match, surface)).collect(Collectors.toList());
    }

    public Iterable<Match> findAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyNameAndSurface(String firstName1, String lastName1, String firstName2, String lastName2, String tourneyName, int year, String surface){
        List<Match> matches = (List<Match>) findAllMatchesBetweenTwoPlayerInSelectedYearAndTourneyName(firstName1, lastName1, firstName2, lastName2, tourneyName, year);
        return matches.stream().filter(match -> filterBySurface(match, surface)).collect(Collectors.toList());
    }

    public List<Match> findLastNMatches(String slug, int nrVisibleMatches){
        Player player = playerRepository.findByPlayerSlug(slug);
        List<Match> matches = (List<Match>)findAllMatchesByPlayerName(player.getFirstName(), player.getLastName());

        try{
            return matches.stream()
                    .limit(nrVisibleMatches)
                    .collect(Collectors.toList());
        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Match> findAllMatchesByTournament(Tournament tournament){
        return (List<Match>) matchesRepository.findAllByTournament(tournament);
    }
}
