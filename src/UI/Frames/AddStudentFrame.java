package UI.Frames;

import UI.Bottom.Functionality.AddingPanel;
import UI.MainFrame;
import course.Course;
import personal.Name;
import personal.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Auther: wangqitong
 * @Date: 04-30-2019 16:58
 * @Description:
 */
public class AddStudentFrame extends JFrame implements ActionListener {
    protected JButton okButton, deleteButton;
    protected JTextArea idTextArea;
    private JComboBox studentTypeBox;
    private AddingPanel addingPanel;
    private MainFrame mainFrame;
    private Course course;

    private String[] types = {"undergraduate", "gradute"};

    public AddStudentFrame(MainFrame mainFrame, Course course, AddingPanel addingPanel) {
        // TODO Auto-generated constructor stub

        this.mainFrame = mainFrame;
        this.course = course;
        this.addingPanel = addingPanel;

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setBounds(25, 150, 75, 25);
        deleteButton = new JButton("Reset");
        deleteButton.addActionListener(this);
        deleteButton.setBounds(200, 150, 75, 25);

        idTextArea = new JTextArea();
        idTextArea.setBorder(BorderFactory.createTitledBorder("Student Id"));
        idTextArea.setBounds(25, 25, 250, 50);

        studentTypeBox = new JComboBox(types);
        studentTypeBox.setBounds(25, 85, 250, 50);

        this.add(idTextArea);
        this.add(okButton);
        this.add(deleteButton);
        this.add(studentTypeBox);
        this.add(new Panel((new GridLayout(1,1))));

        this.setTitle("Add new student");
        this.setSize(300, 225);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setVisible(true);
        this.setResizable(false);

        this.addingPanel.setEnabled(false);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                addingPanel.setEnabled(true);
                dispose();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (course == null)
            return;

        if (e.getActionCommand()=="OK") {
            //addingPanel.setId(idTextArea.getText());
            course.enrollStudent(new Student(idTextArea.getText(), new Name(), "",studentTypeBox.getSelectedItem().toString()));
            mainFrame.update(course);
            this.addingPanel.setEnabled(true);
            this.dispose();

        }
        else if (e.getActionCommand()=="Delete") {
            idTextArea.setText("");
        }
    }
}
