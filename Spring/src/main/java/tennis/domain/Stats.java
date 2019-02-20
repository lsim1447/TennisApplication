package tennis.domain;

import javax.persistence.*;

@Table
@Entity
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name="match_id")
    private Match match;

    private String match_time;
    private String match_duration;
    private int winner_aces;
    private int winner_double_faults;
    private int winner_first_serves_in;
    private int winner_first_serves_total;
    private int winner_first_serve_points_won;
    private int winner_first_serve_points_total;
    private int winner_second_serve_points_won;
    private int winner_second_serve_points_total;
    private int winner_break_points_saved;
    private int winner_break_points_serve_total;
    private int winner_service_points_won;
    private int winner_service_points_total;
    private int winner_first_serve_return_won;
    private int winner_first_serve_return_total;
    private int winner_second_serve_return_won;
    private int winner_second_serve_return_total;
    private int winner_break_points_converted;
    private int winner_break_points_return_total;
    private int winner_service_games_played;
    private int winner_return_games_played;
    private int winner_return_points_won;
    private int winner_return_points_total;
    private int winner_total_points_won;
    private int winner_total_points_total;
    private int loser_aces;
    private int loser_double_faults;
    private int loser_first_serves_in;
    private int loser_first_serves_total;
    private int loser_first_serve_points_won;
    private int loser_first_serve_points_total;
    private int loser_second_serve_points_won;
    private int loser_second_serve_points_total;
    private int loser_break_points_saved;
    private int loser_break_points_serve_total;
    private int loser_service_points_won;
    private int loser_service_points_total;
    private int loser_first_serve_return_won;
    private int loser_first_serve_return_total;
    private int loser_second_serve_return_won;
    private int loser_second_serve_return_total;
    private int loser_break_points_converted;
    private int loser_break_points_return_total;
    private int loser_service_games_played;
    private int loser_return_games_played;
    private int loser_return_points_won;
    private int loser_return_points_total;
    private int loser_total_points_won;
    private int loser_total_points_total;

    public Stats(){

    }

    public Stats(Match match, String match_time, String match_duration, int winner_aces, int winner_double_faults, int winner_first_serves_in, int winner_first_serves_total, int winner_first_serve_points_won, int winner_first_serve_points_total, int winner_second_serve_points_won, int winner_second_serve_points_total, int winner_break_points_saved, int winner_break_points_serve_total, int winner_service_points_won, int winner_service_points_total, int winner_first_serve_return_won, int winner_first_serve_return_total, int winner_second_serve_return_won, int winner_second_serve_return_total, int winner_break_points_converted, int winner_break_points_return_total, int winner_service_games_played, int winner_return_games_played, int winner_return_points_won, int winner_return_points_total, int winner_total_points_won, int winner_total_points_total, int loser_aces, int loser_double_faults, int loser_first_serves_in, int loser_first_serves_total, int loser_first_serve_points_won, int loser_first_serve_points_total, int loser_second_serve_points_won, int loser_second_serve_points_total, int loser_break_points_saved, int loser_break_points_serve_total, int loser_service_points_won, int loser_service_points_total, int loser_first_serve_return_won, int loser_first_serve_return_total, int loser_second_serve_return_won, int loser_second_serve_return_total, int loser_break_points_converted, int loser_break_points_return_total, int loser_service_games_played, int loser_return_games_played, int loser_return_points_won, int loser_return_points_total, int loser_total_points_won, int loser_total_points_total) {
        this.match = match;
        this.match_time = match_time;
        this.match_duration = match_duration;
        this.winner_aces = winner_aces;
        this.winner_double_faults = winner_double_faults;
        this.winner_first_serves_in = winner_first_serves_in;
        this.winner_first_serves_total = winner_first_serves_total;
        this.winner_first_serve_points_won = winner_first_serve_points_won;
        this.winner_first_serve_points_total = winner_first_serve_points_total;
        this.winner_second_serve_points_won = winner_second_serve_points_won;
        this.winner_second_serve_points_total = winner_second_serve_points_total;
        this.winner_break_points_saved = winner_break_points_saved;
        this.winner_break_points_serve_total = winner_break_points_serve_total;
        this.winner_service_points_won = winner_service_points_won;
        this.winner_service_points_total = winner_service_points_total;
        this.winner_first_serve_return_won = winner_first_serve_return_won;
        this.winner_first_serve_return_total = winner_first_serve_return_total;
        this.winner_second_serve_return_won = winner_second_serve_return_won;
        this.winner_second_serve_return_total = winner_second_serve_return_total;
        this.winner_break_points_converted = winner_break_points_converted;
        this.winner_break_points_return_total = winner_break_points_return_total;
        this.winner_service_games_played = winner_service_games_played;
        this.winner_return_games_played = winner_return_games_played;
        this.winner_return_points_won = winner_return_points_won;
        this.winner_return_points_total = winner_return_points_total;
        this.winner_total_points_won = winner_total_points_won;
        this.winner_total_points_total = winner_total_points_total;
        this.loser_aces = loser_aces;
        this.loser_double_faults = loser_double_faults;
        this.loser_first_serves_in = loser_first_serves_in;
        this.loser_first_serves_total = loser_first_serves_total;
        this.loser_first_serve_points_won = loser_first_serve_points_won;
        this.loser_first_serve_points_total = loser_first_serve_points_total;
        this.loser_second_serve_points_won = loser_second_serve_points_won;
        this.loser_second_serve_points_total = loser_second_serve_points_total;
        this.loser_break_points_saved = loser_break_points_saved;
        this.loser_break_points_serve_total = loser_break_points_serve_total;
        this.loser_service_points_won = loser_service_points_won;
        this.loser_service_points_total = loser_service_points_total;
        this.loser_first_serve_return_won = loser_first_serve_return_won;
        this.loser_first_serve_return_total = loser_first_serve_return_total;
        this.loser_second_serve_return_won = loser_second_serve_return_won;
        this.loser_second_serve_return_total = loser_second_serve_return_total;
        this.loser_break_points_converted = loser_break_points_converted;
        this.loser_break_points_return_total = loser_break_points_return_total;
        this.loser_service_games_played = loser_service_games_played;
        this.loser_return_games_played = loser_return_games_played;
        this.loser_return_points_won = loser_return_points_won;
        this.loser_return_points_total = loser_return_points_total;
        this.loser_total_points_won = loser_total_points_won;
        this.loser_total_points_total = loser_total_points_total;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getMatch_duration() {
        return match_duration;
    }

    public void setMatch_duration(String match_duration) {
        this.match_duration = match_duration;
    }

    public int getWinner_aces() {
        return winner_aces;
    }

    public void setWinner_aces(int winner_aces) {
        this.winner_aces = winner_aces;
    }

    public int getWinner_double_faults() {
        return winner_double_faults;
    }

    public void setWinner_double_faults(int winner_double_faults) {
        this.winner_double_faults = winner_double_faults;
    }

    public int getWinner_first_serves_in() {
        return winner_first_serves_in;
    }

    public void setWinner_first_serves_in(int winner_first_serves_in) {
        this.winner_first_serves_in = winner_first_serves_in;
    }

    public int getWinner_first_serves_total() {
        return winner_first_serves_total;
    }

    public void setWinner_first_serves_total(int winner_first_serves_total) {
        this.winner_first_serves_total = winner_first_serves_total;
    }

    public int getWinner_first_serve_points_won() {
        return winner_first_serve_points_won;
    }

    public void setWinner_first_serve_points_won(int winner_first_serve_points_won) {
        this.winner_first_serve_points_won = winner_first_serve_points_won;
    }

    public int getWinner_first_serve_points_total() {
        return winner_first_serve_points_total;
    }

    public void setWinner_first_serve_points_total(int winner_first_serve_points_total) {
        this.winner_first_serve_points_total = winner_first_serve_points_total;
    }

    public int getWinner_second_serve_points_won() {
        return winner_second_serve_points_won;
    }

    public void setWinner_second_serve_points_won(int winner_second_serve_points_won) {
        this.winner_second_serve_points_won = winner_second_serve_points_won;
    }

    public int getWinner_second_serve_points_total() {
        return winner_second_serve_points_total;
    }

    public void setWinner_second_serve_points_total(int winner_second_serve_points_total) {
        this.winner_second_serve_points_total = winner_second_serve_points_total;
    }

    public int getWinner_break_points_saved() {
        return winner_break_points_saved;
    }

    public void setWinner_break_points_saved(int winner_break_points_saved) {
        this.winner_break_points_saved = winner_break_points_saved;
    }

    public int getWinner_break_points_serve_total() {
        return winner_break_points_serve_total;
    }

    public void setWinner_break_points_serve_total(int winner_break_points_serve_total) {
        this.winner_break_points_serve_total = winner_break_points_serve_total;
    }

    public int getWinner_service_points_won() {
        return winner_service_points_won;
    }

    public void setWinner_service_points_won(int winner_service_points_won) {
        this.winner_service_points_won = winner_service_points_won;
    }

    public int getWinner_service_points_total() {
        return winner_service_points_total;
    }

    public void setWinner_service_points_total(int winner_service_points_total) {
        this.winner_service_points_total = winner_service_points_total;
    }

    public int getWinner_first_serve_return_won() {
        return winner_first_serve_return_won;
    }

    public void setWinner_first_serve_return_won(int winner_first_serve_return_won) {
        this.winner_first_serve_return_won = winner_first_serve_return_won;
    }

    public int getWinner_first_serve_return_total() {
        return winner_first_serve_return_total;
    }

    public void setWinner_first_serve_return_total(int winner_first_serve_return_total) {
        this.winner_first_serve_return_total = winner_first_serve_return_total;
    }

    public int getWinner_second_serve_return_won() {
        return winner_second_serve_return_won;
    }

    public void setWinner_second_serve_return_won(int winner_second_serve_return_won) {
        this.winner_second_serve_return_won = winner_second_serve_return_won;
    }

    public int getWinner_second_serve_return_total() {
        return winner_second_serve_return_total;
    }

    public void setWinner_second_serve_return_total(int winner_second_serve_return_total) {
        this.winner_second_serve_return_total = winner_second_serve_return_total;
    }

    public int getWinner_break_points_converted() {
        return winner_break_points_converted;
    }

    public void setWinner_break_points_converted(int winner_break_points_converted) {
        this.winner_break_points_converted = winner_break_points_converted;
    }

    public int getWinner_break_points_return_total() {
        return winner_break_points_return_total;
    }

    public void setWinner_break_points_return_total(int winner_break_points_return_total) {
        this.winner_break_points_return_total = winner_break_points_return_total;
    }

    public int getWinner_service_games_played() {
        return winner_service_games_played;
    }

    public void setWinner_service_games_played(int winner_service_games_played) {
        this.winner_service_games_played = winner_service_games_played;
    }

    public int getWinner_return_games_played() {
        return winner_return_games_played;
    }

    public void setWinner_return_games_played(int winner_return_games_played) {
        this.winner_return_games_played = winner_return_games_played;
    }

    public int getWinner_return_points_won() {
        return winner_return_points_won;
    }

    public void setWinner_return_points_won(int winner_return_points_won) {
        this.winner_return_points_won = winner_return_points_won;
    }

    public int getWinner_return_points_total() {
        return winner_return_points_total;
    }

    public void setWinner_return_points_total(int winner_return_points_total) {
        this.winner_return_points_total = winner_return_points_total;
    }

    public int getWinner_total_points_won() {
        return winner_total_points_won;
    }

    public void setWinner_total_points_won(int winner_total_points_won) {
        this.winner_total_points_won = winner_total_points_won;
    }

    public int getWinner_total_points_total() {
        return winner_total_points_total;
    }

    public void setWinner_total_points_total(int winner_total_points_total) {
        this.winner_total_points_total = winner_total_points_total;
    }

    public int getLoser_aces() {
        return loser_aces;
    }

    public void setLoser_aces(int loser_aces) {
        this.loser_aces = loser_aces;
    }

    public int getLoser_double_faults() {
        return loser_double_faults;
    }

    public void setLoser_double_faults(int loser_double_faults) {
        this.loser_double_faults = loser_double_faults;
    }

    public int getLoser_first_serves_in() {
        return loser_first_serves_in;
    }

    public void setLoser_first_serves_in(int loser_first_serves_in) {
        this.loser_first_serves_in = loser_first_serves_in;
    }

    public int getLoser_first_serves_total() {
        return loser_first_serves_total;
    }

    public void setLoser_first_serves_total(int loser_first_serves_total) {
        this.loser_first_serves_total = loser_first_serves_total;
    }

    public int getLoser_first_serve_points_won() {
        return loser_first_serve_points_won;
    }

    public void setLoser_first_serve_points_won(int loser_first_serve_points_won) {
        this.loser_first_serve_points_won = loser_first_serve_points_won;
    }

    public int getLoser_first_serve_points_total() {
        return loser_first_serve_points_total;
    }

    public void setLoser_first_serve_points_total(int loser_first_serve_points_total) {
        this.loser_first_serve_points_total = loser_first_serve_points_total;
    }

    public int getLoser_second_serve_points_won() {
        return loser_second_serve_points_won;
    }

    public void setLoser_second_serve_points_won(int loser_second_serve_points_won) {
        this.loser_second_serve_points_won = loser_second_serve_points_won;
    }

    public int getLoser_second_serve_points_total() {
        return loser_second_serve_points_total;
    }

    public void setLoser_second_serve_points_total(int loser_second_serve_points_total) {
        this.loser_second_serve_points_total = loser_second_serve_points_total;
    }

    public int getLoser_break_points_saved() {
        return loser_break_points_saved;
    }

    public void setLoser_break_points_saved(int loser_break_points_saved) {
        this.loser_break_points_saved = loser_break_points_saved;
    }

    public int getLoser_break_points_serve_total() {
        return loser_break_points_serve_total;
    }

    public void setLoser_break_points_serve_total(int loser_break_points_serve_total) {
        this.loser_break_points_serve_total = loser_break_points_serve_total;
    }

    public int getLoser_service_points_won() {
        return loser_service_points_won;
    }

    public void setLoser_service_points_won(int loser_service_points_won) {
        this.loser_service_points_won = loser_service_points_won;
    }

    public int getLoser_service_points_total() {
        return loser_service_points_total;
    }

    public void setLoser_service_points_total(int loser_service_points_total) {
        this.loser_service_points_total = loser_service_points_total;
    }

    public int getLoser_first_serve_return_won() {
        return loser_first_serve_return_won;
    }

    public void setLoser_first_serve_return_won(int loser_first_serve_return_won) {
        this.loser_first_serve_return_won = loser_first_serve_return_won;
    }

    public int getLoser_first_serve_return_total() {
        return loser_first_serve_return_total;
    }

    public void setLoser_first_serve_return_total(int loser_first_serve_return_total) {
        this.loser_first_serve_return_total = loser_first_serve_return_total;
    }

    public int getLoser_second_serve_return_won() {
        return loser_second_serve_return_won;
    }

    public void setLoser_second_serve_return_won(int loser_second_serve_return_won) {
        this.loser_second_serve_return_won = loser_second_serve_return_won;
    }

    public int getLoser_second_serve_return_total() {
        return loser_second_serve_return_total;
    }

    public void setLoser_second_serve_return_total(int loser_second_serve_return_total) {
        this.loser_second_serve_return_total = loser_second_serve_return_total;
    }

    public int getLoser_break_points_converted() {
        return loser_break_points_converted;
    }

    public void setLoser_break_points_converted(int loser_break_points_converted) {
        this.loser_break_points_converted = loser_break_points_converted;
    }

    public int getLoser_break_points_return_total() {
        return loser_break_points_return_total;
    }

    public void setLoser_break_points_return_total(int loser_break_points_return_total) {
        this.loser_break_points_return_total = loser_break_points_return_total;
    }

    public int getLoser_service_games_played() {
        return loser_service_games_played;
    }

    public void setLoser_service_games_played(int loser_service_games_played) {
        this.loser_service_games_played = loser_service_games_played;
    }

    public int getLoser_return_games_played() {
        return loser_return_games_played;
    }

    public void setLoser_return_games_played(int loser_return_games_played) {
        this.loser_return_games_played = loser_return_games_played;
    }

    public int getLoser_return_points_won() {
        return loser_return_points_won;
    }

    public void setLoser_return_points_won(int loser_return_points_won) {
        this.loser_return_points_won = loser_return_points_won;
    }

    public int getLoser_return_points_total() {
        return loser_return_points_total;
    }

    public void setLoser_return_points_total(int loser_return_points_total) {
        this.loser_return_points_total = loser_return_points_total;
    }

    public int getLoser_total_points_won() {
        return loser_total_points_won;
    }

    public void setLoser_total_points_won(int loser_total_points_won) {
        this.loser_total_points_won = loser_total_points_won;
    }

    public int getLoser_total_points_total() {
        return loser_total_points_total;
    }

    public void setLoser_total_points_total(int loser_total_points_total) {
        this.loser_total_points_total = loser_total_points_total;
    }
}
