import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLOutput;
import java.util.List;

public class MyFrame extends JFrame implements ActionListener {
    private JButton button;
    private JTextArea textArea;
    private JTextArea textArea2;

    public MyFrame() {
        this.setTitle("Training Planner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); //Center the GUI on the screen
        this.setSize(400, 400);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS)); //Layout-Manager

        // Display a button:
        button = new JButton("Upload CSV");
        this.add(button);
        button.setFocusable(false);
        button.addActionListener(this); //register button as Action-Listener
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button

        // Add a text area to display setsPerWeek
        textArea = new JTextArea(8, 15);
        textArea.setEditable(false); // User can't edit the text
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT); // Center text area
        scrollPane.setMaximumSize(new Dimension(400, 200));
        scrollPane.setPreferredSize(new Dimension(400, 200));

        //Add a second text area to display Volume & RPE
        textArea2 = new JTextArea(8, 15);
        textArea2.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        this.add(scrollPane2);
        scrollPane2.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane2.setMaximumSize(new Dimension(400, 200));
        scrollPane2.setPreferredSize(new Dimension(400, 200));

        // Show:
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == button) { //If button is clicked
            JFileChooser chooser = new JFileChooser();
            int response = chooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) { //If successfully chose file
                File file2 = new File (chooser.getSelectedFile().getAbsolutePath());
                handleFile(file2);
            }
        }
    }

    public void handleFile(File file){
        List<List<String>> array = FileHandler.importData(file);
        int weekCount = findWeek(file);
        TrainingWeek week = new TrainingWeek(weekCount);
        week.addExercises(array);
        String analysisResults = Analysis.setsPerWeek(week).toString();
        String analysisResults2 = Analysis.volumeRpe(week).toString();
        WeekProgress.addWeek(week);
        textArea.setText(analysisResults);
        textArea2.setText(analysisResults2);
    }

    public int findWeek(File file){
        String fileName = file.getName();
        int week = 0;
        for (int i = 0; i < fileName.length(); i++) {
            if (fileName.charAt(i) >= '0' && fileName.charAt(i) <= '9') {
                week = fileName.charAt(i) - '0';
            }
        }
        return week;
    }

}
