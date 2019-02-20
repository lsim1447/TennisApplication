package tennis.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVReader {

    private static List<List<String>> records = new ArrayList<List<String>>();

    private static void readDataFromFile(String filename){
        try (Scanner scanner = new Scanner(new File("./src/main/java/tennis/utils/csv/" + filename + ".csv"))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException fnf){
            System.out.println("File Not Found Exception");
        }
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public static List<List<String>> getData(String filename){
        readDataFromFile(filename);
        return  records;
    }
}
