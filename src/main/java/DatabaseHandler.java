import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseHandler {
    private static final Dotenv dotenv = Dotenv.load(); // Initialize dotenv
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/TrainingData";
    private static final String USER = "postgres";
    private static final String PASSWORD = dotenv.get("DB_PASSWORD"); // Read password from environment variable

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void exportTrainingWeek(TrainingWeek week) {
        String sql = "INSERT INTO training_data (week, exercise, muscle_group, sets, reps, rpe) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Exercise exercise : week.getExercises()) {
                for (Set set : exercise.getSets()) {
                    pstmt.setInt(1, week.getWeekNumber());
                    pstmt.setString(2, exercise.getName());
                    pstmt.setString(3, exercise.getMuscleGroup().toString());
                    pstmt.setInt(4, set.getReps());
                    pstmt.setDouble(5, set.getWeight());
                    pstmt.setDouble(6, set.getRpe());
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
            System.out.println("Training data exported successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
