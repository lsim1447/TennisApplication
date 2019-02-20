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

    private Long year;

    private Long singles_draw;

    private String conditions;

    private String surface;

    private String url_suffix;

    @ManyToOne
    @JoinColumn(name="tourney_id")
    private Tourney tourney;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    public Tournament(){

    }

    public Tournament(String tournament_year_id, String dates, Long year, Long singles_draw, String conditions, String surface, String url_suffix, Tourney tourney, Player player) {
        this.tournament_year_id = tournament_year_id;
        this.dates = dates;
        this.year = year;
        this.singles_draw = singles_draw;
        this.conditions = conditions;
        this.surface = surface;
        this.url_suffix = url_suffix;
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

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getSingles_draw() {
        return singles_draw;
    }

    public void setSingles_draw(Long singles_draw) {
        this.singles_draw = singles_draw;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getUrl_suffix() {
        return url_suffix;
    }

    public void setUrl_suffix(String url_suffix) {
        this.url_suffix = url_suffix;
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
