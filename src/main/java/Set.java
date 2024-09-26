import java.io.Serializable;

public class Set implements Serializable {
    private double weight;
    private int reps;
    private double rpe;

    public Set(double weight, int reps, double rpe) {
        this.weight = weight;
        this.reps = reps;
        this.rpe = rpe;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public double getRpe() {
        return rpe;
    }
}
