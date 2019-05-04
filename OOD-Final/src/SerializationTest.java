import course.Category;
import course.Course;
import course.Criterion;
import course.NewCriterion;
import personal.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: Test Serialization
 * @author: Zhizhou Qiu
 * @create: 04-21-2019
 **/
public class SerializationTest {
    public static void main(String[] args){

        // write to file
        Category assignment = new Category("Assignment", 0.5,2);
        Category exam = new Category("Exam", 0.5, 3);
        List<Category> categories = new ArrayList<>();
        categories.add(assignment);
        categories.add(exam);
        NewCriterion criterion = new NewCriterion(categories, "mycriterion");
        Course course = new Course("mycourse", "123","fall","2019", criterion);
//        course.writeToFile("mycourse");
//        criterion.writeToFile("criterion");

        // read from file
        NewCriterion newCriterion = criterion.readFromFile("criterion");
        System.out.println(newCriterion.toString());
        Course newCourse = course.readFromFile("mycourse");
        System.out.println(newCourse.toString());
    }
}
