import java.util.ArrayList;
import java.util.List;

public class Progress {
    private static List<TrainingWeek> trainingWeeks = new ArrayList<TrainingWeek>();

    public static void addWeeks(TrainingWeek trainingWeek) {
        trainingWeeks.add(trainingWeek);
    }

    public static List<TrainingWeek> getWeeks() {
        return trainingWeeks;
    }
}
