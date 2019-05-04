package UI.Bottom.Functionality;

import UI.Table.Header.Groupable.ColumnGroup;
import UI.Table.Header.Groupable.GroupableTableHeader;
import UI.MainFrame;
import course.Course;
import personal.Student;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

/**
 * @Auther: Di Zhu
 * @Date: 04-30-2019 10:26
 * @Description: Panel for removing rows and columns of JTable
 */
public class RemovePanel extends JPanel {

    private MainFrame mainFrame;
    private Course course;
    private JComboBox choiceComboBox;
    private JButton removeButton;
    String[] choices = new String[]{"Selected Row", "Selected Column"};

    public RemovePanel() {
        super();
        initialization();
    }

    public RemovePanel(MainFrame mainFrame, Course course) {
        super();
        this.mainFrame = mainFrame;
        this.course = course;
        initialization();
    }

    private void initialization() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        choiceComboBox = new JComboBox(choices);
        choiceComboBox.setPreferredSize(new Dimension(128, 54));
        choiceComboBox.setSelectedIndex(-1);

        /*** Left Part ***/
        removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            if (course == null)
                return;

            JTable gradeTable = mainFrame.getGradeTable();
            if (choiceComboBox.getSelectedIndex() == 0) {
                String stuId = mainFrame.getSelectedStudentId();
                Student stu  = course.getStudent(stuId);
                course.getsGrade(stu).withDraw();
            } else if (choiceComboBox.getSelectedIndex() == 1) {
                List<Object> selections = mainFrame.getCheckBoxSelections();
                for (int i = selections.size() - 1 ; i >= 0 ; i--) {
                    if (selections.get(i) != null) {
                        GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
                        TableColumn tc = gradeTable.getColumnModel().getColumn(i);
                        List<ColumnGroup> columnGroups = header.getColumnGroups(tc);
                        if (columnGroups.size() != 0) {
                            int cat = header.findIndexOfGroup(columnGroups.get(0).getHeaderValue());
                            int index = Integer.parseInt(gradeTable.getColumnName(i)) - 1;
                            System.out.println("Cat: " + cat);
                            System.out.println("Index: " + index);
                            course.deleteOne(cat, Integer.parseInt(gradeTable.getColumnName(i)) - 1);
                        }
                    }
                }
            } else {
                System.out.println("No operation selected.");
            }
            mainFrame.clearCheckBoxSelection();
            mainFrame.update(course);
        });
        removeButton.setPreferredSize(new Dimension(96, 54));

        /*** Add components to main panel ***/

        this.add(removeButton);
        this.add(choiceComboBox);
    }

    public void refreshPanel(Course newCourse) {
        this.course = course;
        choiceComboBox.setSelectedIndex(-1);
    }
}
