import java.io.FileWriter;
import java.util.ArrayList;

public class WeekProgress {
    private static ArrayList<TrainingWeek> weeks = new ArrayList<TrainingWeek>();

    public static void addWeek(TrainingWeek week) {
        weeks.add(week);
    }

    public static void serializeWeek(){
        FileHandler.serializeObject(weeks);
    }

    public static void setWeeks(ArrayList<TrainingWeek> newWeeks) {
        weeks = newWeeks;
    }

    public static ArrayList<TrainingWeek> getWeeks() {
        return weeks;
    }


}
