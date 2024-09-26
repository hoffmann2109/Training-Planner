import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MyFrame extends JFrame {
    private JButton button;
    private JTextArea textArea;
    private JTextArea textArea2;
    private JProgressBar progressBar;
    private JComboBox<MuscleGroup> comboBox;

    public MyFrame() {
        // Create the Main frame
        this.setTitle("Training Planner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Center the GUI on the screen
        this.setSize(600, 400);
        this.setLayout(new BorderLayout(10, 10)); // Add gaps for better spacing

        // Create two panels: one for the data, one for charts
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Set background colors for visual distinction
        leftPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBackground(Color.WHITE);

        // Add the panels to the main frame
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.CENTER);

        // Display an "Upload CSV" button
        button = new JButton("Upload CSV");
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        button.setFocusable(false);
        leftPanel.add(button);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add space between components

        // Add a progress bar at the bottom of the left panel
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Show progress percentage
        leftPanel.add(progressBar);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add space

        // Add text areas for displaying the analysis results
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false); // User can't edit the text
        textArea.setFocusable(false);

        // Scrollable view of the textArea:
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the text area
        scrollPane.setPreferredSize(new Dimension(400, 150)); // Set preferred size
        rightPanel.add(scrollPane);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add space between components

        textArea2 = new JTextArea(10, 30);
        textArea2.setEditable(false);
        textArea2.setFocusable(false);

        // Scrollable view of the textArea:
        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setPreferredSize(new Dimension(400, 150)); // Set preferred size
        scrollPane2.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(scrollPane2);

        // Create a JComboBox and populate it with the enum values
        comboBox = new JComboBox<>(MuscleGroup.values());
        rightPanel.add(comboBox); // Add the JComboBox to the frame

        // Show the frame
        this.setVisible(true);

        // Add ActionListener to the button using an anonymous inner class
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
                // Display the selected item in textArea2 or use as needed
                textArea2.setText("Selected Muscle Group: " + selectedMuscleGroup);
            }
        });
    }

    public void handleFile(File file) {
        // Example of handling file and populating text areas and progress bar
        List<List<String>> array = FileHandler.importData(file); // Assuming FileHandler works correctly
        int weekCount = findWeek(file);
        TrainingWeek week = new TrainingWeek(weekCount);
        week.addExercises(array);

        String analysisResults = Analysis.setsPerWeek(week).toString();
        String analysisResults2 = Analysis.volumeRpe(week).toString();

        WeekProgress.addWeek(week);
        textArea.setText(analysisResults + "\n" + "------------------------------------------------------------------" + "\n" + analysisResults2);  // Update first text area
        progressBar.setValue(week.getAveragePercentage()); // Update progress bar
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
        // Run the GUI
        new MyFrame();
    }
}
