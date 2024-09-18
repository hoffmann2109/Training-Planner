import java.util.List;

public class Main {
    public static void main(String[] args) {
        handleFile(1);

    }

    public static void handleFile(int week){
        String fileName = "Week" + 1 +".csv";
        List<List<String>> array = FileHandler.importData(fileName);
        TrainingWeek week1 = new TrainingWeek(week);
        week1.addExercises(array);
        Analysis.analyze(week1);
    }


}