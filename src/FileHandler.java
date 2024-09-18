import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static List<List<String>> importData(String filename){
        String filePath = "/home/thomas/Downloads/" + filename;
        File file = new File(filePath);
        List<List<String>> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(";");

                values.add(new ArrayList<>(List.of(splitLine)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }
}
