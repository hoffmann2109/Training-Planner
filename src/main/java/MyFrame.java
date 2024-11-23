import com.formdev.flatlaf.FlatLightLaf;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFrame extends JFrame {
    private JButton button;
    private JButton importButton;
    private JButton exportButton;  // Add export button
    private JTextArea upperTextArea;
    private JTextArea lowerTextArea;
    private JProgressBar progressBar;
    private JComboBox<String> comboBox;  // Change to String to include "All" option
    private ChartPanel chartPanel;
    private TrainingWeek week;
    private JButton clearDatabaseButton; // New button

    public MyFrame() {
        this.setTitle("Training Planner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout(10, 10));

        // Create top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create buttons and add them to top panel
        button = new JButton("Upload CSV");
        importButton = new JButton("Import");
        exportButton = new JButton("Export");

        topPanel.add(button);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(importButton);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(exportButton);  // Added export button

        // Add progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        topPanel.add(progressBar);

        this.add(topPanel, BorderLayout.NORTH);

        // Center panel with text areas and chart
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        upperTextArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(upperTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        centerPanel.add(scrollPane);

        lowerTextArea = new JTextArea(10, 30);
        JScrollPane scrollPane2 = new JScrollPane(lowerTextArea);
        scrollPane2.setPreferredSize(new Dimension(400, 150));
        centerPanel.add(scrollPane2);

        // Initialize the chart with empty data
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Sets per Muscle Group", "Muscle Group", "Sets", dataset
        );
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(chartPanel);

        // Add comboBox for muscle groups, including an "All" option
        comboBox = new JComboBox<>();
        comboBox.addItem("All");
        for (MuscleGroup mg : MuscleGroup.values()) {
            comboBox.addItem(mg.toString());
        }
        centerPanel.add(comboBox);
        this.add(centerPanel, BorderLayout.CENTER);

        this.setVisible(true);

        // ActionListener for Export Button
        exportButton.addActionListener(evt -> {
            WeekProgress.serializeWeek();  // Serialize only on export button click
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.exportTrainingWeek(week);
            JOptionPane.showMessageDialog(MyFrame.this, "Data exported to PostgreSQL successfully.", "Export", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(MyFrame.this, "Data exported successfully.", "Export", JOptionPane.INFORMATION_MESSAGE);
        });

        button.addActionListener(evt -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                List<List<String>> data = FileHandler.importData(file);

                int weekCount = findNextWeekNumber();
                week = new TrainingWeek(weekCount);
                week.addExercises(data);

                // Add the week to progress tracking
                WeekProgress.addWeek(week);

                // Update the display with the new data
                updateTextAreas();
                updateChart();
                progressBar.setValue(week.getAveragePercentage());
            }
        });


        // ActionListener for Import Button
        importButton.addActionListener(evt -> {
            ArrayList<TrainingWeek> weeks = FileHandler.deserializeObject();
            if (weeks != null && !weeks.isEmpty()) {
                week = weeks.get(weeks.size() - 1);  // Load most recent week
                WeekProgress.setWeeks(weeks);

                updateTextAreas();
                updateChart();
                progressBar.setValue(week.getAveragePercentage());
            } else {
                JOptionPane.showMessageDialog(this, "No data found in the deserialized file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ActionListener for comboBox to filter by muscle group or show all
        comboBox.addActionListener(evt -> {
            String selected = (String) comboBox.getSelectedItem();
            if (selected.equals("All")) {
                updateTextAreas();  // Show all data
            } else {
                MuscleGroup selectedMuscleGroup = MuscleGroup.valueOf(selected);
                showFilteredData(selectedMuscleGroup);
            }
        });

        clearDatabaseButton = new JButton("Clear Database");
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(clearDatabaseButton);

        clearDatabaseButton.addActionListener(evt -> {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to clear the database? This action cannot be undone.",
                    "Confirm Clear Database",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.clearDatabase();
                JOptionPane.showMessageDialog(this, "Database cleared successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void updateTextAreas() {
        upperTextArea.setText(Analysis.setsPerWeek(week).toString());
        lowerTextArea.setText(Analysis.volumeRpe(week).toString());
    }

    private void showFilteredData(MuscleGroup muscleGroup) {
        StringBuilder exerciseData = new StringBuilder("Exercises for " + muscleGroup + ":\n\n");
        StringBuilder volumeData = new StringBuilder("Volume and average RPE for " + muscleGroup + ":\n\n");

        double totalVolume = 0; // Variable to accumulate the total volume

        for (Exercise e : week.getExercises()) {
            if (e.getMuscleGroup() == muscleGroup) {
                exerciseData.append(e.toString()).append("\n");
                for (Set s : e.getSets()) {
                    double volume = s.getWeight() * s.getReps();
                    totalVolume += volume; // Add to total volume
                    volumeData.append("Volume: ").append(volume).append(", RPE: ").append(s.getRpe()).append("\n");
                }
            }
        }

        // Add total volume summary at the top
        volumeData.insert(0, "Total Volume for " + muscleGroup + ": " + totalVolume + "\n\n");

        upperTextArea.setText(exerciseData.toString());
        lowerTextArea.setText(volumeData.toString());
    }


    private void updateChart() {
        if (week == null) return;  // Ensure week is not null

        // Create a dataset from the TrainingWeek data
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Get data from the TrainingWeek instance
        Map<MuscleGroup, Integer> setsPerWeek = week.getSetsPerWeekMap();
        Map<MuscleGroup, Integer> targetSetsPerWeek = week.getTargetSetsPerWeek();

        for (MuscleGroup muscle : setsPerWeek.keySet()) {
            int setsCompleted = setsPerWeek.get(muscle);
            int targetSets = targetSetsPerWeek.getOrDefault(muscle, 0);

            // Add data to the dataset
            dataset.addValue(setsCompleted, "Completed Sets", muscle.toString());
            dataset.addValue(targetSets, "Target Sets", muscle.toString());
        }

        // Create a new chart with the updated dataset
        JFreeChart chart = ChartFactory.createBarChart(
                "Sets per Muscle Group - Week " + week.getWeekNumber(),
                "Muscle Group",
                "Sets",
                dataset
        );

        // Update the chart in the chartPanel
        chartPanel.setChart(chart);
    }

    private int findNextWeekNumber() {
        List<TrainingWeek> weeks = WeekProgress.getWeeks();
        return weeks.isEmpty() ? 1 : weeks.get(weeks.size() - 1).getWeekNumber() + 1;
    }


    public static void main(String[] args) {
        // Set the look and feel (optional, depends on your setup)
        FlatLightLaf.install();

        // Run the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MyFrame());
    }

}
