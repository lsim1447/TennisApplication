package tennis.service;

import org.springframework.stereotype.Service;
import tennis.domain.Player;
import tennis.domain.Stats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredicterService {

    private DecimalFormat df = new DecimalFormat("####0.00");

    public List<Integer> convertSumToOneHundredPercent(double percentage1, double percentage2){
        List<Integer> new_percentages = new ArrayList<>();
        double a, b;
        a = (percentage1 / (percentage1 + percentage2)) * 100;
        b = (percentage2 / (percentage1 + percentage2)) * 100;
        new_percentages.add((int) Math.round(a));
        new_percentages.add((int) Math.round(b));
        return new_percentages;
    }

    public double getServicePointsWonRate(List<Stats> playerStats, Player player){
        playerStats = playerStats.stream()
                .filter(st -> !((double) st.getWinner_service_points_total() < 0.001 || (double) st.getLoser_service_points_total() < 0.001))
                .collect(Collectors.toList());

        double playerServiceRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return stat.getWinner_service_points_won() / (double) stat.getWinner_service_points_total();
                    } else {
                        return stat.getLoser_service_points_won() / (double) stat.getLoser_service_points_total();
                    }
                })
                .reduce(0.00, (a, b) -> a + b));

        if (playerStats.size() == 0) {
            throw new NumberFormatException();
        }
        playerServiceRate = playerServiceRate /(double) playerStats.size();
        return  Double.parseDouble(df.format(playerServiceRate));
    }

    public double getReturnPointsWonRate(List<Stats> playerStats, Player player){
        playerStats = playerStats.stream()
                .filter(st -> !((double)st.getWinner_return_points_total() < 0.0001 || (double) st.getLoser_return_points_total() < 0.0001))
                .collect(Collectors.toList());

        double playerReturnRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return stat.getWinner_return_points_won() / (double) stat.getWinner_return_points_total();
                    } else {
                        return stat.getLoser_return_points_won() / (double) stat.getLoser_return_points_total();
                    }
                })
                .reduce(0.00, (a, b) -> a + b));

        if (playerStats.size() == 0) {
            throw new NumberFormatException();
        }

        playerReturnRate = playerReturnRate / (double) playerStats.size();
        return Double.parseDouble(df.format(playerReturnRate));
    }

    public double getFirstServeInRate(List<Stats> playerStats, Player player){
        playerStats = playerStats.stream()
                .filter(st -> !((double)st.getWinner_first_serve_points_total() < 0.0001 || (double) st.getLoser_first_serve_points_total() < 0.0001))
                .collect(Collectors.toList());

        double playerFirstServeInRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return stat.getWinner_first_serves_in() / (double) stat.getWinner_first_serve_points_total();
                    } else {
                        return stat.getLoser_first_serves_in() / (double) stat.getLoser_first_serve_points_total();
                    }
                })
                .reduce(0.00, (a, b) -> a + b));

        if (playerStats.size() == 0) {
            throw new NumberFormatException();
        }

        playerFirstServeInRate = playerFirstServeInRate / (double) playerStats.size();
        return Double.parseDouble(df.format(playerFirstServeInRate));
    }

    public double getFirstServeWonRate(List<Stats> playerStats, Player player){
        playerStats = playerStats.stream()
                .filter(st -> !((double)st.getWinner_first_serve_points_total() < 0.0001 || (double) st.getLoser_first_serve_points_total() < 0.0001))
                .collect(Collectors.toList());

        double playerFirstServeWonRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return stat.getWinner_first_serve_points_won() / (double) stat.getWinner_first_serve_points_total();
                    } else {
                        return stat.getLoser_first_serve_points_won() / (double) stat.getLoser_first_serve_points_total();
                    }
                })
                .reduce(0.00, (a, b) -> a + b));

        if (playerStats.size() == 0) {
            throw new NumberFormatException();
        }

        playerFirstServeWonRate = playerFirstServeWonRate / (double) playerStats.size();
        return Double.parseDouble(df.format(playerFirstServeWonRate));
    }

    public double getBreakPointsConvertedRate(List<Stats> playerStats, Player player){
        playerStats = playerStats.stream()
                .filter(st -> !((double)st.getWinner_break_points_return_total() < 0.0001 || (double) st.getLoser_break_points_return_total() < 0.0001))
                .collect(Collectors.toList());

        double playerBreakPointsConvertedRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return (stat.getWinner_break_points_return_total() != 0) ? (stat.getWinner_break_points_converted() / (double) stat.getWinner_break_points_return_total()) : 0.0;
                    } else {
                        return stat.getLoser_break_points_converted() / (double) stat.getLoser_break_points_return_total();
                    }
                })
                .reduce(0.00, (a, b) -> a + b));

        if (playerStats.size() == 0) {
            throw new NumberFormatException();
        }

        playerBreakPointsConvertedRate = playerBreakPointsConvertedRate / (double) playerStats.size();
        return Double.parseDouble(df.format(playerBreakPointsConvertedRate));
    }

    public double getBreakPointsSavedRate(List<Stats> playerStats, Player player){
        playerStats = playerStats.stream()
                .filter(st -> !((double)st.getWinner_break_points_serve_total() < 0.0001 || (double) st.getLoser_break_points_serve_total() < 0.0001))
                .collect(Collectors.toList());

        double playerBreakPointsSavedRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return stat.getWinner_break_points_saved() / (double) stat.getWinner_break_points_serve_total();
                    } else {
                        return stat.getLoser_break_points_saved() / (double) stat.getLoser_break_points_serve_total();
                    }
                })
                .reduce(0.00, (a, b) -> a + b));

        if (playerStats.size() == 0) {
            throw new NumberFormatException();
        }

        playerBreakPointsSavedRate = playerBreakPointsSavedRate / (double) playerStats.size();
        return Double.parseDouble(df.format(playerBreakPointsSavedRate));
    }
}
