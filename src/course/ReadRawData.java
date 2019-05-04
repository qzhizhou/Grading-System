package course;

import grade.Grade;
import grade.GradeComp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import personal.Name;
import personal.Student;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description: This is a class to read raw data from json file
 * @author: Zhizhou Qiu
 * @create: 04-23-2019
 **/
public class ReadRawData {

    public static Course readRawData(String path) {
        JSONParser jsonParser = new JSONParser();

        try {
            FileReader fileReader = new FileReader(path);

            Object obj = jsonParser.parse(fileReader);

            JSONArray jsonArray = (JSONArray) obj;

            JSONObject jsoncourse = (JSONObject) jsonArray.get(0);

            JSONObject jsonCriterion = (JSONObject) jsonArray.get(1);

//            System.out.println(jsonCriterion.toString());

            List<Category> categories = new ArrayList<>();

            JSONArray catergoryArr = (JSONArray) jsonCriterion.get("category");

            for (Object o : catergoryArr){
                JSONObject category = (JSONObject) o;
                categories.add(new Category(category.get("name").toString(),
                        Double.valueOf(category.get("weight").toString()),
                        Integer.valueOf(category.get("number").toString())));
            }

            NewCriterion criterion = new NewCriterion(
                    categories, jsonCriterion.get("name").toString()
            );

//            System.out.println(criterion);

            Course course = new Course(
                    jsoncourse.get("name").toString(),
                    jsoncourse.get("id").toString(),
                    jsoncourse.get("semester").toString(),
                    jsoncourse.get("year").toString(),
                    criterion
            );

            HashMap<Student, Grade> gradeMap = course.getList();

            JSONObject jsonStudent = (JSONObject) jsonArray.get(2);

            JSONArray studentArray = (JSONArray) jsonStudent.get("student");

//            System.out.println(studentArray.toString());

            // set grade of every student
            for (Object o : studentArray){
                JSONObject jsonObject = (JSONObject) o;

                // create a Student Object
                Student student = new Student(
                        jsonObject.get("id").toString(),
                        createName(jsonObject),
                        jsonObject.get("email").toString(),
                        jsonObject.get("type").toString()
                );
                course.enrollStudent(student);
                // get the mapping Grade object
                Grade g = course.getsGrade(student);

                // for each student, set score of each category
                for (int i = 0; i < categories.size(); i++){
                    Category category = categories.get(i);
                    String name = category.getName();

                    // set score for each grade component in each category
                    List<GradeComp> gradeComps = g.getCategory(i);
                    for (int j = 0; j < gradeComps.size(); j++){
                        GradeComp gradeComp = gradeComps.get(j);
                        String key = prefix+name+(j+1);
                        String score = jsonObject.get(key).toString();
                        gradeComp.setScore(score);
                    }
                }

                gradeMap.put(student,g);

            }

            return course;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return new Course();
    }

    private static Name createName(JSONObject jsonObject){
        String name = jsonObject.get("name").toString();
        String[] arr = name.split(" ");
        if (arr.length == 0) return new Name();
        else if (arr.length == 1) return new Name(arr[0]);
        else if (arr.length == 2) return new Name(arr[0],"", arr[1]);
        else if (arr.length == 3) return new Name(arr[0], arr[1], arr[2]);
        else return new Name(name);
    }

    private static final String prefix = "gradeOf";
}
