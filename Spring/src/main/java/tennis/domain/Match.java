package tennis.domain;
import javax.persistence.*;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    private String match_id;

    private String round_name;
    private int round_order;
    private int match_order;

    @ManyToOne
    @JoinColumn(name="tournament_year_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name="winner_player_id")
    private Player winnerPlayer;

    @ManyToOne
    @JoinColumn(name="loser_player_id")
    private Player loserPlayer;

    private String winner_seed;
    private String loser_seed;
    private String match_score_tiebreaks;
    private int winner_sets_won;
    private int loser_sets_won;
    private int winner_games_won;
    private int loser_games_won;
    private int winner_tiebreaks_won;
    private int loser_tiebreaks_won;
    private String match_stats_url_suffix;

    public Match(){

    }

    public Match(String match_id, String round_name, int round_order, int match_order, Tournament tournament, Player winner_player, Player loser_player, String winner_seed, String loser_seed, String match_score_tiebreaks, int winner_sets_won, int loser_sets_won, int winner_games_won, int loser_games_won, int winner_tiebreaks_won, int loser_tiebreaks_won, String match_stats_url_suffix) {
        this.match_id = match_id;
        this.round_name = round_name;
        this.round_order = round_order;
        this.match_order = match_order;
        this.tournament = tournament;
        this.winnerPlayer = winner_player;
        this.loserPlayer = loser_player;
        this.winner_seed = winner_seed;
        this.loser_seed = loser_seed;
        this.match_score_tiebreaks = match_score_tiebreaks;
        this.winner_sets_won = winner_sets_won;
        this.loser_sets_won = loser_sets_won;
        this.winner_games_won = winner_games_won;
        this.loser_games_won = loser_games_won;
        this.winner_tiebreaks_won = winner_tiebreaks_won;
        this.loser_tiebreaks_won = loser_tiebreaks_won;
        this.match_stats_url_suffix = match_stats_url_suffix;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getRound_name() {
        return round_name;
    }

    public void setRound_name(String round_name) {
        this.round_name = round_name;
    }

    public int getRound_order() {
        return round_order;
    }

    public void setRound_order(int round_order) {
        this.round_order = round_order;
    }

    public int getMatch_order() {
        return match_order;
    }

    public void setMatch_order(int match_order) {
        this.match_order = match_order;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setWinnerPlayer(Player winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public Player getLoserPlayer() {
        return loserPlayer;
    }

    public void setLoserPlayer(Player loserPlayer) {
        this.loserPlayer = loserPlayer;
    }

    public String getWinner_seed() {
        return winner_seed;
    }

    public void setWinner_seed(String winner_seed) {
        this.winner_seed = winner_seed;
    }

    public String getLoser_seed() {
        return loser_seed;
    }

    public void setLoser_seed(String loser_seed) {
        this.loser_seed = loser_seed;
    }

    public String getMatch_score_tiebreaks() {
        return match_score_tiebreaks;
    }

    public void setMatch_score_tiebreaks(String match_score_tiebreaks) {
        this.match_score_tiebreaks = match_score_tiebreaks;
    }

    public int getWinner_sets_won() {
        return winner_sets_won;
    }

    public void setWinner_sets_won(int winner_sets_won) {
        this.winner_sets_won = winner_sets_won;
    }

    public int getLoser_sets_won() {
        return loser_sets_won;
    }

    public void setLoser_sets_won(int loser_sets_won) {
        this.loser_sets_won = loser_sets_won;
    }

    public int getWinner_games_won() {
        return winner_games_won;
    }

    public void setWinner_games_won(int winner_games_won) {
        this.winner_games_won = winner_games_won;
    }

    public int getLoser_games_won() {
        return loser_games_won;
    }

    public void setLoser_games_won(int loser_games_won) {
        this.loser_games_won = loser_games_won;
    }

    public int getWinner_tiebreaks_won() {
        return winner_tiebreaks_won;
    }

    public void setWinner_tiebreaks_won(int winner_tiebreaks_won) {
        this.winner_tiebreaks_won = winner_tiebreaks_won;
    }

    public int getLoser_tiebreaks_won() {
        return loser_tiebreaks_won;
    }

    public void setLoser_tiebreaks_won(int loser_tiebreaks_won) {
        this.loser_tiebreaks_won = loser_tiebreaks_won;
    }

    public String getMatch_stats_url_suffix() {
        return match_stats_url_suffix;
    }

    public void setMatch_stats_url_suffix(String match_stats_url_suffix) {
        this.match_stats_url_suffix = match_stats_url_suffix;
    }
}
