import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {
    private String name;
    private MuscleGroup muscleGroup;
    private List<Set> sets;

    public Exercise(String name, MuscleGroup muscleGroup, List<Set> sets) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public List<Set> getSets() {
        return sets;
    }

    public String toString(){
        return "Exercise: " + name + " for " + muscleGroup + ": " + sets.size() + " sets";
    }

}
