package tennis.model;

import tennis.domain.Player;

public class Champion {
    private Player player;
    private int times;

    public Champion(){}

    public Champion(Player player, int times) {
        this.player = player;
        this.times = times;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
