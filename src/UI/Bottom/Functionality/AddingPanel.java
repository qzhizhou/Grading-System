package UI.Bottom.Functionality;

import UI.MainFrame;
import course.Category;
import course.Course;
import UI.Frames.AddStudentFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @Auther: Di Zhu
 * @Date: 04-29-2019 20:44
 * @Description: Panel for performing adding operations
 */
public class AddingPanel extends JPanel {

    private JPanel cards;
    private final static String none = "None";
    private final static String student = "Student";
    private final static String category = "Category";
    private final static String subcategory = "SubCategory";

    private MainFrame mainFrame;
    private Course course;
    //private JComboBox categoryBox;
    private JComboBox addingBox;
    private JButton addStudent;
    //private JButton addSubCategory;
    private JButton addCategory;
    String[] addings = new String[]{"", "Student", "Category", "SubCategory"};

    private String Id = "";

    public AddingPanel() {
        super();
        initialization();
    }

    public AddingPanel(MainFrame mainFrame, Course course) {
        super();
        this.mainFrame = mainFrame;
        this.course = course;
        initialization();
    }

    private void initialization() {
        this.setLayout(new FlowLayout());
        addingBox = new JComboBox(addings);
        addingBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, (String) e.getItem());
            }
        });

        JPanel controller = new JPanel(new BorderLayout());
        controller.setPreferredSize(new Dimension(128, 54));
        controller.add(addingBox, BorderLayout.CENTER);

        /*** Left Part ***/
        addStudent = new JButton("Add student");
        addStudent.addActionListener(e -> {
            if (course != null) {
                new AddStudentFrame(mainFrame, course, this);
            }
        });
        addStudent.setPreferredSize(new Dimension(128, 54));

        addCategory = new JButton("Add category");
        addCategory.addActionListener(e -> {
            if (course != null) {
                Category category = new Category();
                course.getCcriterion().getCategories().add(category);
                //course.updateGrade();
                course.addCategory();
                mainFrame.update(course);
            }
        });
        addCategory.setPreferredSize(new Dimension(128, 54));

        cards = new JPanel();
        cards.setLayout(new CardLayout());
        cards.add(new JPanel(), none);
        cards.add(addStudent, student);
        cards.add(addCategory, category);
        cards.add(new AddSubCategory(mainFrame, course), subcategory);

        /*** Add components to main panel ***/
        this.add(controller);
        this.add(cards);
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setId(String id) {
        this.Id = id;
    }
}
