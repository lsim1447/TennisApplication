package tennis.service;

import org.springframework.stereotype.Service;
import tennis.domain.Player;
import tennis.domain.Stats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
        double playerServiceRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return (stat.getWinner_service_points_total() != 0) ? (stat.getWinner_service_points_won() / (double) stat.getWinner_service_points_total()) : 0.0;
                    } else {
                        return (stat.getWinner_service_points_total() != 0) ? (stat.getLoser_service_points_won() / (double) stat.getLoser_service_points_total()) : 0.0;
                    }
                })
                .reduce(0.00, (a, b) -> a + b)) / (double) playerStats.size() ;
        return  Double.parseDouble(df.format(playerServiceRate));
    }

    public double getReturnPointsWonRate(List<Stats> playerStats, Player player){
        double playerReturnRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return (stat.getWinner_return_points_total() != 0) ? (stat.getWinner_return_points_won() / (double) stat.getWinner_return_points_total()) : 0.0;
                    } else {
                        return (stat.getWinner_return_points_total() != 0) ? (stat.getLoser_return_points_won() / (double) stat.getLoser_return_points_total()) : 0.0;
                    }
                })
                .reduce(0.00, (a, b) -> a + b)) / (double) playerStats.size() ;
        return Double.parseDouble(df.format(playerReturnRate));
    }

    public double getFirstServeInRate(List<Stats> playerStats, Player player){
        double playerFirstServeInRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return (stat.getWinner_first_serve_points_total() != 0) ? (stat.getWinner_first_serves_in() / (double) stat.getWinner_first_serve_points_total()) : 0.0;
                    } else {
                        return (stat.getWinner_first_serve_points_total() != 0) ? (stat.getLoser_first_serves_in() / (double) stat.getLoser_first_serve_points_total()) : 0.0;
                    }
                })
                .reduce(0.00, (a, b) -> a + b)) / (double) playerStats.size() ;
        return Double.parseDouble(df.format(playerFirstServeInRate));
    }

    public double getFirstServeWonRate(List<Stats> playerStats, Player player){
        double playerFirstServeWonRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return (stat.getWinner_first_serve_points_total() != 0) ? (stat.getWinner_first_serve_points_won() / (double) stat.getWinner_first_serve_points_total()) : 0.0;
                    } else {
                        return (stat.getLoser_first_serve_points_total() != 0) ? (stat.getLoser_first_serve_points_won() / (double) stat.getLoser_first_serve_points_total()) : 0.0;
                    }
                })
                .reduce(0.00, (a, b) -> a + b)) / (double) playerStats.size() ;
        return Double.parseDouble(df.format(playerFirstServeWonRate));
    }

    public double getBreakPointsConvertedRate(List<Stats> playerStats, Player player){
        double playerBreakPointsConvertedRate = (playerStats.stream()
                .map(stat -> {
                    if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                        return (stat.getWinner_break_points_return_total() != 0) ? (stat.getWinner_break_points_converted() / (double) stat.getWinner_break_points_return_total()) : 0.0;
                    } else {
                        return (stat.getWinner_break_points_return_total() != 0) ? (stat.getLoser_break_points_converted() / (double) stat.getLoser_break_points_return_total()) : 0.0;
                    }
                })
                .reduce(0.00, (a, b) -> a + b)) / (double) playerStats.size() ;
        return Double.parseDouble(df.format(playerBreakPointsConvertedRate));
    }

    public double getBreakPointsSavedRate(List<Stats> playerStats, Player player){
        double playerBreakPointsSavedRate = 0.0;
        try{
            playerBreakPointsSavedRate = (playerStats.stream()
                    .map(stat -> {
                        if (stat.getMatch().getWinnerPlayer().getPlayerSlug().equals(player.getPlayerSlug())){
                            return (stat.getWinner_break_points_serve_total() != 0) ? (stat.getWinner_break_points_saved() / (double) stat.getWinner_break_points_serve_total()) : 0.0;
                        } else {
                            return (stat.getWinner_break_points_serve_total() != 0) ? (stat.getLoser_break_points_saved() / (double) stat.getLoser_break_points_serve_total()) : 0.0;
                        }
                    })
                    .reduce(0.00, (a, b) -> a + b));
            playerBreakPointsSavedRate = playerBreakPointsSavedRate / (double) playerStats.size();
            return Double.parseDouble(df.format(playerBreakPointsSavedRate));
        } catch (Exception e){
            System.out.println("PINA ====> playerStats.size() = " + playerStats.size());
            System.out.println("PINA ====> playerBreakPointsSavedRate = " + playerBreakPointsSavedRate);
            return 0.0;
        }
    }
}
