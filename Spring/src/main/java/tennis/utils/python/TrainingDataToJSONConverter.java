package tennis.utils.python;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tennis.model.TrainData;

public class TrainingDataToJSONConverter {

    public static void writeToJSONFile(List<TrainData> data, String filename){
        try {
            JSONObject obj = new JSONObject();
            JSONArray dataJSONArray = new JSONArray();

            for (TrainData element: data) {
                JSONObject traindata_obj = new JSONObject();
                JSONArray inputJSONArray = new JSONArray();
                JSONArray outputJSONArray = new JSONArray();

                inputJSONArray.addAll(element.getInputs());
                outputJSONArray.addAll(element.getOutputs());

                traindata_obj.put("inputs", inputJSONArray);
                traindata_obj.put("outputs", outputJSONArray);

                dataJSONArray.add(traindata_obj);
            }
            obj.put("data", dataJSONArray);

            FileWriter file = new FileWriter("../Python/" + filename);
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            file.close();
        } catch (IOException io){
            System.out.println("IO Exception during the json convertion or during the writing to file.");
        }
    }
}
