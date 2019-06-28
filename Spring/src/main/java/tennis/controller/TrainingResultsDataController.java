package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.TrainingDescription;
import tennis.domain.TrainingResultsData;
import tennis.model.TrainingDetails;
import tennis.service.TrainingDescriptionService;
import tennis.service.TrainingResultsDataService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/training/results/data")
@CrossOrigin(origins = "http://localhost:3000")
public class TrainingResultsDataController {

    @Autowired private TrainingDescriptionService trainingDescriptionService;
    @Autowired private TrainingResultsDataService trainingResultsDataService;

    @GetMapping("/training_id")
    @ResponseBody
    public TrainingDetails getTrainingResultsData(@RequestParam String training_id){
        TrainingDescription description = trainingDescriptionService.findById(training_id);
        List<TrainingResultsData> trainingResultsData = new ArrayList<>();

        if (description != null){
            trainingResultsData = trainingResultsDataService.findAllByTrainingDescription(description);
        }

        TrainingDetails trainingDetails = new TrainingDetails(trainingResultsData, description);

        return trainingDetails;
    }
}
