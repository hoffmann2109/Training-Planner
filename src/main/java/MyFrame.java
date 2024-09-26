import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import com.formdev.flatlaf.*;

public class MyFrame extends JFrame {
    private JButton button;
    private JTextArea upperTextArea;
    private JTextArea lowerTextArea;
    private JProgressBar progressBar;
    private JComboBox<MuscleGroup> comboBox;
    private ChartPanel chartPanel; // Added for the chart
    private TrainingWeek week; // Store the TrainingWeek instance

    public MyFrame() {
        // Create the Main frame
        this.setTitle("Training Planner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Center the GUI on the screen
        this.setSize(800, 600); // Increased size to accommodate the chart
        this.setLayout(new BorderLayout(10, 10)); // Add gaps for better spacing

        // Create top panel for the button and progress bar
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create the "Upload CSV" button
        button = new JButton("Upload CSV");
        button.setFocusable(false);
        topPanel.add(button);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add space between button and progress bar

        // Add progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Show progress percentage
        topPanel.add(progressBar);

        // Add topPanel to the main frame
        this.add(topPanel, BorderLayout.NORTH);

        // Create center panel for the rest of the components
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Add text area for displaying analysis results
        upperTextArea = new JTextArea(10, 30);
        upperTextArea.setEditable(false); // User can't edit the text
        upperTextArea.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(upperTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 150)); // Set preferred size
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between components

        // Initialize the chart with empty data
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Sets per Muscle Group", // Chart Title
                "Muscle Group", // Category axis (X)
                "Sets", // Value axis (Y)
                dataset
        );
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(chartPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between components

        // Create a JComboBox and populate it with the enum values
        comboBox = new JComboBox<>(MuscleGroup.values());
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        comboBox.setRenderer(listRenderer);
        centerPanel.add(comboBox); // Add the JComboBox to the center panel
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between components

        // Add a second text area after the combo box
        lowerTextArea = new JTextArea(10, 30);
        lowerTextArea.setEditable(false);
        lowerTextArea.setFocusable(false);
        JScrollPane scrollPane2 = new JScrollPane(lowerTextArea);
        scrollPane2.setPreferredSize(new Dimension(400, 150)); // Set preferred size
        centerPanel.add(scrollPane2);

        // Add centerPanel to the main frame
        this.add(centerPanel, BorderLayout.CENTER);

        // Show the frame
        this.setVisible(true);

        // Add ActionListener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                int response = chooser.showOpenDialog(null); // Show the open file dialog
                if (response == JFileChooser.APPROVE_OPTION) { // If a file was selected
                    File file = chooser.getSelectedFile();
                    handleFile(file); // Process the file
                }
            }
        });

        // Add ActionListener to the JComboBox to display the selected item
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MuscleGroup selectedMuscleGroup = (MuscleGroup) comboBox.getSelectedItem();
                // Handle the selected muscle group
            }
        });
    }

    public void handleFile(File file) {
        // Example of handling file and populating text areas and progress bar
        List<List<String>> array = FileHandler.importData(file); // Assuming FileHandler works correctly
        int weekCount = findWeek(file);
        week = new TrainingWeek(weekCount); // Store the week instance in the class variable
        week.addExercises(array);

        String analysisResults = Analysis.setsPerWeek(week).toString();
        String analysisResults2 = Analysis.volumeRpe(week).toString();

        WeekProgress.addWeek(week);
        WeekProgress.serializeWeek(); //Serialize Training weeks
        upperTextArea.setText(analysisResults);  // Update first text area
        lowerTextArea.setText(analysisResults2);
        progressBar.setValue(week.getAveragePercentage()); // Update progress bar

        // Update the chart with the new data
        updateChart();

    }

    public void updateChart() {
        // Create a dataset from the TrainingWeek data
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Get data from the TrainingWeek instance
        Map<MuscleGroup, Integer> setsPerWeek = week.getSetsPerWeekMap();
        Map<MuscleGroup, Integer> targetSetsPerWeek = week.getTargetSetsPerWeek();

        for (MuscleGroup muscle : setsPerWeek.keySet()) {
            int setsCompleted = setsPerWeek.get(muscle);
            int targetSets = targetSetsPerWeek.get(muscle);

            // Add data to the dataset
            dataset.addValue(setsCompleted, "Completed Sets", muscle.toString());
            dataset.addValue(targetSets, "Target Sets", muscle.toString());
        }

        // Create a new chart with the updated dataset
        JFreeChart chart = ChartFactory.createBarChart(
                "Sets per Muscle Group - Week " + week.getWeekNumber(), // Chart Title
                "Muscle Group", // Category axis (X)
                "Sets", // Value axis (Y)
                dataset
        );

        // Update the chart in the chartPanel
        chartPanel.setChart(chart);
    }

    public int findWeek(File file) {
        String fileName = file.getName();
        int week = 0;
        for (int i = 0; i < fileName.length(); i++) {
            if (Character.isDigit(fileName.charAt(i))) {
                week = fileName.charAt(i) - '0';
            }
        }
        return week;
    }

    public static void main(String[] args) {
        FlatLightLaf.install();
        // Run the GUI
        new MyFrame();
    }
}
