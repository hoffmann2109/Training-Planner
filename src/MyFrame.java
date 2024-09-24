import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class MyFrame extends JFrame implements ActionListener {
    private JButton button;
    private String fileName;

    public MyFrame() {
        this.setTitle("Training Planner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(400, 400);
        this.setLayout(new FlowLayout());

        // Display a button:
        button = new JButton("Upload CSV");
        this.add(button);
        button.setFocusable(false);
        button.addActionListener(this);

        // Show:
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == button) {
            JFileChooser chooser = new JFileChooser();
            int response = chooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file2 = new File (chooser.getSelectedFile().getAbsolutePath());
                List<List<String>> array = FileHandler.importData(file2);
                TrainingWeek week = new TrainingWeek(1);
                week.addExercises(array);
                Analysis.analyze(week);
            }
        }
    }

}
