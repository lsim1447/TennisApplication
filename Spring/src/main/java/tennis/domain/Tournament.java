package tennis.domain;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Tournament {
    @Id
    private String tournament_year_id;

    private String dates;

    private int year;

    private int singlesDraw;

    private String urlSuffix;

    @ManyToOne
    @JoinColumn(name="tourney_id")
    private Tourney tourney;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    public Tournament(){

    }

    public Tournament(String tournament_year_id, String dates, int year, int singles_draw, String url_suffix, Tourney tourney, Player player) {
        this.tournament_year_id = tournament_year_id;
        this.dates = dates;
        this.year = year;
        this.singlesDraw = singles_draw;
        this.urlSuffix = url_suffix;
        this.tourney = tourney;
        this.player = player;
    }

    public String getTournament_year_id() {
        return tournament_year_id;
    }

    public void setTournament_year_id(String tournament_year_id) {
        this.tournament_year_id = tournament_year_id;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSinglesDraw() {
        return singlesDraw;
    }

    public void setSinglesDraw(int singlesDraw) {
        this.singlesDraw = singlesDraw;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public Tourney getTourney() {
        return tourney;
    }

    public void setTourney(Tourney tourney) {
        this.tourney = tourney;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
