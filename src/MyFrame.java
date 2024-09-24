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

        // Add a text area to display data
        textArea = new JTextArea(15, 30);
        textArea.setEditable(false); // User can't edit the text
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);

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
        TrainingWeek week = new TrainingWeek(1);
        week.addExercises(array);
        String analysisResults = Analysis.analyze(week).toString();
        textArea.setText(analysisResults);
    }

}
