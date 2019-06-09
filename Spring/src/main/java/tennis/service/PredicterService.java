package tennis.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PredicterService {

    public List<Integer> convertSumToOneHundredPercent(double percentage1, double percentage2){
        List<Integer> new_percentages = new ArrayList<>();
        double a, b;
        a = (percentage1 / (percentage1 + percentage2)) * 100;
        b = (percentage2 / (percentage1 + percentage2)) * 100;
        new_percentages.add((int) Math.round(a));
        new_percentages.add((int) Math.round(b));
        return new_percentages;
    }
}
