package UI.Bottom.Functionality;

import course.Category;
import course.Course;
import UI.Frames.Statistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Di Zhu
 * @Date: 04-23-2019 10:01
 * @Description: Panel from which the user can see statistics of a course
 */
public class StatisticsPanel extends JPanel {

    private Course course;
    private JButton statBy;
    private JComboBox categoryBox;
    private JComboBox subCategoryBox;
    private String[] categories = new String[]{};


    public StatisticsPanel() {
        super();
        initialization();
    }

    public StatisticsPanel(Course course) {
        super();
        this.course = course;
        if (course != null)
            setCategories(course.getCcriterion().getCategories());
        initialization();
    }

    public void setCategories(List<Category> categoryList) {
        List<String> temp = new ArrayList<>();
        temp.add("Total");
        for (Category category : categoryList) {
            temp.add(category.getName());
        }
        categories = temp.toArray(categories);
    }

    private void initialization() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        /*** Left part ***/
        statBy = new JButton("Show statistics of");
        statBy.setPreferredSize(new Dimension(128, 54));
        statBy.addActionListener(e-> {
            if (course == null)
                return;
            if (categoryBox.getSelectedIndex() == 0) {
                new Statistics(course);
            } else {
                new Statistics(course, categoryBox.getSelectedIndex() - 1, subCategoryBox.getSelectedIndex());
            }
        });

        /*** Right part ***/
        JPanel rightPanel = new JPanel(new BorderLayout(4, 8));
        categoryBox = new JComboBox<>(categories);
        categoryBox.setPrototypeDisplayValue("Assignment ");

        subCategoryBox = new JComboBox();
        subCategoryBox.setPrototypeDisplayValue("Assignment ");

        rightPanel.add(categoryBox, BorderLayout.NORTH);
        rightPanel.add(subCategoryBox, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(128, 54));

        this.add(statBy);
        this.add(rightPanel);

        /*** Listener for ComboBox ***/
        categoryBox.addActionListener(new ComboBoxListener());
    }

    /*** Getter and Setter ***/
    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void refreshPanel(Course newCourse) {
        setCourse(newCourse);
        setCategories(newCourse.getCcriterion().getCategories());

        categoryBox.removeAllItems();

        for (String str : categories) {
            categoryBox.addItem(str);
        }
    }

    private class ComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            subCategoryBox.removeAllItems();
            if (categoryBox.getSelectedIndex() == -1) {

            } else if (categoryBox.getSelectedIndex() == 0) {
                subCategoryBox.setEnabled(false);
            } else {
                subCategoryBox.setEnabled(true);
                int categoryIndex = categoryBox.getSelectedIndex();
                int numSubCategory = course.getCcriterion().getCategories().get(categoryIndex - 1).getNumberOfTasks();

                for (int i = 0 ; i < numSubCategory ; i++) {
                    subCategoryBox.addItem(String.valueOf(i + 1));
                }
            }
        }
    }
}
