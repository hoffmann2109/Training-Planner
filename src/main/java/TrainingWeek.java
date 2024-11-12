import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingWeek implements Serializable {
    private List<Exercise> exercises = new ArrayList<>();
    private int week;
    private Map<MuscleGroup, Integer> setsPerWeek = new HashMap<>();
    private Map<MuscleGroup, Integer> targetSetsPerWeek = new HashMap<>();
    int totalPercentage = 0;
    int percentageCount = 0;

    public TrainingWeek(int week) {
        this.week = week;
        setTargetSetsPerWeek();
        Progress.addWeeks(this);
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setTargetSetsPerWeek() {
        this.targetSetsPerWeek.put(MuscleGroup.CALVES, 8);
        this.targetSetsPerWeek.put(MuscleGroup.HAMSTRINGS, 8);
        this.targetSetsPerWeek.put(MuscleGroup.QUADS, 8);
        this.targetSetsPerWeek.put(MuscleGroup.ABS, 0);
        this.targetSetsPerWeek.put(MuscleGroup.CHEST, 10);
        this.targetSetsPerWeek.put(MuscleGroup.BACK, 10);
        this.targetSetsPerWeek.put(MuscleGroup.TRICEPS, 10);
        this.targetSetsPerWeek.put(MuscleGroup.BICEPS, 10);
        this.targetSetsPerWeek.put(MuscleGroup.FOREARMS, 0);
        this.targetSetsPerWeek.put(MuscleGroup.REAR_DELTS, 10);
        this.targetSetsPerWeek.put(MuscleGroup.SIDE_DELTS, 10);
        this.targetSetsPerWeek.put(MuscleGroup.FRONT_DELTS, 0);
        this.targetSetsPerWeek.put(MuscleGroup.TRAPS, 0);
    }

    public StringBuilder getSetsPerWeekString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sets completed in Week " + week + ": " + "\n");
        sb.append("\n");
        for (MuscleGroup muscle : setsPerWeek.keySet()) {
            double percentage = (double) setsPerWeek.get(muscle) / targetSetsPerWeek.get(muscle) * 100.00;
            percentageCount++;
            totalPercentage += percentage;
            sb.append(muscle + ": " + setsPerWeek.get(muscle) + "/" + targetSetsPerWeek.get(muscle) + " sets --> " + (int) percentage + "%" + "\n");
        }
        return sb;
    }

    public int getAveragePercentage() {
        return percentageCount == 0 ? 0 : totalPercentage / percentageCount;
    }

    public Map<MuscleGroup, Integer> getSetsPerWeekMap() {
        return setsPerWeek;
    }

    public Map<MuscleGroup, Integer> getTargetSetsPerWeek() {
        return targetSetsPerWeek;
    }

    public int getWeekNumber() {
        return week;
    }


    public void addExercises(List<List<String>> array) {
        int length = array.size();
        for (int i = 0; i < length; i++) {

            List<String> innerList = array.get(i);
            if (innerList.size() < 6) {
                continue;
            }

            String name = array.get(i).get(0).trim();

            String muscleGroupString = array.get(i).get(1).trim().toUpperCase();
            muscleGroupString = muscleGroupString.replace(' ', '_');
            MuscleGroup muscleGroup = MuscleGroup.valueOf(muscleGroupString);
            int setCount = Integer.parseInt(array.get(i).get(2).trim());
            if (setCount > 0) {


                double weight = Double.parseDouble(array.get(i).get(3).trim());
                List<Set> sets = new ArrayList<>();

                for (int j = 0; j < setCount; j++) {
                    // reps:
                    String sReps = array.get(i).get(4).trim();
                    String[] repArray = sReps.split(",");
                    int reps = Integer.parseInt(repArray[j].trim());

                    //rpe:
                    String sRpe = array.get(i).get(5).trim();
                    String[] rpeArray = sRpe.split(",");
                    double rpes = Double.parseDouble(rpeArray[j].trim());

                    // add to sets:
                    sets.add(new Set(weight, reps, rpes));
                }

                this.exercises.add(new Exercise(name, muscleGroup, sets));
            }
            // Accumulate set counts per muscle group
            this.setsPerWeek.merge(muscleGroup, setCount, Integer::sum);

        }

    }
}
