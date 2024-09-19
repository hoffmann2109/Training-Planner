import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.util.List;

public class Gui {

    public static void initialize() {
        JFrame jframe = new JFrame("Training Planner");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(500, 500);

        // Create menubar with FILE and HELP menus
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("FILE");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        // Add options to FILE menu
        JMenuItem fileMenu1 = new JMenuItem("New File");
        JMenuItem fileMenu2 = new JMenuItem("Save As");
        fileMenu.add(fileMenu1);
        fileMenu.add(fileMenu2);

        // Text area at the center
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.PLAIN, 16));

        // Panel at the bottom with label, text field, and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel label = new JLabel("Please Enter Text:");
        JTextField textField = new JTextField(15);
        JButton btn_send = new JButton("Send");
        JButton btn_reset = new JButton("Reset");

        // Styling components
        label.setFont(new Font("Arial", Font.BOLD, 14));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        btn_send.setBackground(Color.GREEN);
        btn_send.setForeground(Color.WHITE);
        btn_reset.setBackground(Color.RED);
        btn_reset.setForeground(Color.WHITE);

        // Add Choose File button
        JButton btn_chooseFile = new JButton("Choose File");
        btn_chooseFile.setBackground(Color.BLUE);
        btn_chooseFile.setForeground(Color.WHITE);

        // Action listener to open File Chooser
        btn_chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(jframe);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    textField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        // Drag and Drop field
        JTextField dragDropField = new JTextField("Drag and drop a file here");
        dragDropField.setPreferredSize(new Dimension(200, 50));
        dragDropField.setHorizontalAlignment(JTextField.CENTER);
        dragDropField.setFont(new Font("Arial", Font.ITALIC, 12));
        dragDropField.setBackground(Color.LIGHT_GRAY);

        // Enable drag and drop on the text field
        dragDropField.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        // Display the file path in the text field
                        textField.setText(file.getAbsolutePath());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Field to select the number of weeks
        JLabel weeksLabel = new JLabel("Number of Weeks:");
        weeksLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JSpinner weeksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 52, 1)); // Min 1, Max 52
        weeksSpinner.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add components to the panel
        panel.add(label);
        panel.add(textField);
        panel.add(btn_send);
        panel.add(btn_reset);
        panel.add(btn_chooseFile);
        panel.add(dragDropField);
        panel.add(weeksLabel);
        panel.add(weeksSpinner);

        // Add components to the frame
        jframe.getContentPane().add(BorderLayout.NORTH, menuBar);
        jframe.getContentPane().add(BorderLayout.CENTER, new JScrollPane(textArea));
        jframe.getContentPane().add(BorderLayout.SOUTH, panel);
        jframe.setVisible(true);
    }

    public static void main(String[] args) {
        initialize();
    }
}
