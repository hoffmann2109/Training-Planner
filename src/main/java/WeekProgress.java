import java.util.ArrayList;

public class WeekProgress {
    private static ArrayList<TrainingWeek> weeks = new ArrayList<TrainingWeek>();

    public static void addWeek(TrainingWeek week) {
        weeks.add(week);
    }
}
