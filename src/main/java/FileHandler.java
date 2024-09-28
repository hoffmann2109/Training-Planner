import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static List<List<String>> importData(File file){
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

    public static void serializeObject(ArrayList<TrainingWeek> weeks){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream("training_week.ser");
            oos = new ObjectOutputStream(fos);

            oos.writeObject(weeks);
            fos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<TrainingWeek> deserializeObject() {
        try (FileInputStream fis = new FileInputStream("training_week.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            ArrayList<TrainingWeek> weeks = (ArrayList<TrainingWeek>) ois.readObject();
            return weeks;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
