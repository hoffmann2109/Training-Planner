import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MyFrame extends JFrame implements ActionListener {
    private JButton button;

    public MyFrame() {
        this.setTitle("Training Planner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        button = new JButton("Upload CSV");
        this.add(button);
        button.setFocusable(false);
        button.addActionListener(this);
        this.pack();
        this.setLayout(new FlowLayout());
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == button) {
            JFileChooser chooser = new JFileChooser();
            int response = chooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File (chooser.getSelectedFile().getAbsolutePath());

            }
        }
    }
}
