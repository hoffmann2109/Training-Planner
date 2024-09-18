import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingWeek {
    private List<Exercise> exercises = new ArrayList<>();
    private int week;
    private Map<MuscleGroup, Integer> setsPerWeek = new HashMap<>();

    public TrainingWeek(int week) {
        this.week = week;
    }

    public void getSetsPerWeek(){
        System.out.println("-----------------------------");
        System.out.println("Sets completed in Week "+ week + ": " + "\n") ;
        for (MuscleGroup muscle: setsPerWeek.keySet()) {
            System.out.println(muscle + ": " + setsPerWeek.get(muscle) + " sets");
        }
        System.out.println("-----------------------------");
    }

    public void addExercises(List<List<String>> array) {
        int length = array.size();
        for (int i = 0; i < length; i++){

            List<String> innerList = array.get(i);
            if (innerList.size() < 6) {
                continue;
            }

            String name = array.get(i).get(0).trim();

            String muscleGroupString = array.get(i).get(1).trim().toUpperCase();
            muscleGroupString = muscleGroupString.replace(' ', '_');
            MuscleGroup muscleGroup = MuscleGroup.valueOf(muscleGroupString);
            int setCount = Integer.parseInt(array.get(i).get(2).trim());
            double weight = Double.parseDouble(array.get(i).get(3).trim());
            List<Set> sets = new ArrayList<>();

            for (int j = 0; j < setCount; j++){
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
            // Accumulate set counts per muscle group
            this.setsPerWeek.merge(muscleGroup, setCount, Integer::sum);

        }
    }

    public void printExercises() {
        for (Exercise exercise : exercises) {
            System.out.println(exercise);
        }
    }



}
