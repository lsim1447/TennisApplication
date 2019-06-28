package tennis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennis.domain.TrainingDescription;
import tennis.service.TrainingDescriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/training/description")
@CrossOrigin(origins = "http://localhost:3000")
public class TrainingDescriptionController {

    @Autowired private TrainingDescriptionService trainingDescriptionService;

    @GetMapping("/id")
    @ResponseBody
    public TrainingDescription getTrainingDescriptionByTrainingId(@RequestParam String training_id){
        TrainingDescription description = trainingDescriptionService.findById(training_id);
        return description;
    }

    @GetMapping("/all")
    public List<TrainingDescription> getAllTraining(){
        return trainingDescriptionService.findAll();
    }
}
