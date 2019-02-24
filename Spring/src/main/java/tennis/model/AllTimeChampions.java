package tennis.model;

import tennis.domain.Player;

import java.util.List;

public class AllTimeChampions {
    private List<Player> players;
    private int times;

    public AllTimeChampions(){

    }

    public AllTimeChampions(List<Player> players, int times) {
        this.players = players;
        this.times = times;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
