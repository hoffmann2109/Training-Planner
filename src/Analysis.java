import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analysis {

    public static StringBuilder setsPerWeek(TrainingWeek week){
        return week.getSetsPerWeek();
    }

    public static StringBuilder volumeRpe(TrainingWeek week) {
        ArrayList<Exercise> exercises = (ArrayList) week.getExercises();
        StringBuilder result = new StringBuilder();
        result.append("Volume and average RPE per muscle group: " + "\n");
        result.append("\n");
        Map<MuscleGroup, AccumulatedData> muscleDataMap = new HashMap<>();

        for (Exercise e : exercises) {
            MuscleGroup muscle = e.getMuscleGroup();
            List<Set> sets = e.getSets();

            // Initialize or retrieve the accumulated data for the muscle group
            AccumulatedData data = muscleDataMap.getOrDefault(muscle, new AccumulatedData());
            for (Set s : sets) {
                double weight = s.getWeight();
                int reps = s.getReps();
                double rpe = s.getRpe();
                data.totalVolume += weight * reps;
                data.sumRpe += rpe;
                data.rpeCount++;
            }
            muscleDataMap.put(muscle, data);
        }

        // Compile the results for each muscle group
        for (Map.Entry<MuscleGroup, AccumulatedData> entry : muscleDataMap.entrySet()) {
            MuscleGroup muscle = entry.getKey();
            AccumulatedData data = entry.getValue();
            double averageRpe = Math.round((data.sumRpe / data.rpeCount) * 10.0) / 10.0;
            result.append(muscle + ": " + data.totalVolume + "@" + averageRpe + "\n");
        }

        return result;
    }

    // Helper class to accumulate data
    static class AccumulatedData {
        double totalVolume = 0;
        double sumRpe = 0;
        int rpeCount = 0;
    }
}
