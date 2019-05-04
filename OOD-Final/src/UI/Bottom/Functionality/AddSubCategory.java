package UI.Bottom.Functionality;

import UI.MainFrame;
import course.Category;
import course.Course;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Di Zhu
 * @Date: 05-03-2019 13:22
 * @Description: Panel for adding a subcategory
 */
public class AddSubCategory extends JPanel {

    private MainFrame mainFrame;
    private Course course;
    private JButton addSubCategory;
    private JComboBox categoryBox;
    String[] categories = new String[]{};

    public AddSubCategory() {
        initialization();
    }

    public AddSubCategory(MainFrame mainFrame, Course course) {
        this.mainFrame = mainFrame;
        this.course = course;
        if (course != null) {
            setCategories(course.getCcriterion().getCategories());
        }
        initialization();
    }

    private void initialization() {
        this.setLayout(new BorderLayout());
        addSubCategory = new JButton("Add column");
        addSubCategory.addActionListener(e-> {
            if (course == null)
                return;
            String cate = categories[categoryBox.getSelectedIndex()];
            for (int i = 0 ; i < course.getCcriterion().getCategories().size() ; i++) {
                Category temp = course.getCcriterion().getCategories().get(i);
                if (temp.getName().equals(cate)) {
                    course.getCcriterion().addTask(i);
                }
            }
            course.updateGrade();
            mainFrame.update(course);
        });

        categoryBox = new JComboBox<>(categories);
        this.add(categoryBox, BorderLayout.NORTH);
        this.add(addSubCategory, BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(128, 54));
    }

    public void setCategories(java.util.List<Category> categoryList) {
        List<String> temp = new ArrayList<>();
        for (Category category : categoryList) {
            temp.add(category.getName());
        }
        categories = temp.toArray(categories);
    }
}
