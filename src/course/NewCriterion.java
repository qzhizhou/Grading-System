package course;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: This a the new criterion that allows adding new category
 * @author: Zhizhou Qiu
 * @create: 04-30-2019
 **/
public class NewCriterion implements IO<NewCriterion>, Serializable {
    private List<Category> categories;
    private String name;


    /**
    * @Description: this is to create a NewCriterion object consists a list of Categories
    * @Param: list of Category, String name
    * @Return: a NewCriterion object
    * @Author: Zhizhou Qiu
    * @Date: 2019/4/30
    **/
    public NewCriterion(List<Category> list, String name){
        if (list == null) categories = new ArrayList<Category>();
        else categories = list;
        this.name = name;
    }

    // default constructor
    public NewCriterion(){
        categories = new ArrayList<Category>();
        name = "";
    }

    /**
    * @Description: this is to add a new Task in a certain category
    * @Param: index of the category list
    * @Return: boolean
    * @Author: Zhizhou Qiu
    * @Date: 2019/4/30
    **/
    public boolean addTask(int indexOfCategory){
        if (indexOfCategory >= categories.size() || indexOfCategory < 0) return false;
        Category category = categories.get(indexOfCategory);
        List<CriComp> criComps = category.getCriComps();
        if (criComps == null) criComps = new ArrayList<CriComp>();
        criComps.add(new CriComp(0, 100.0));
        double newWeight = 1.0 / criComps.size();
        for (CriComp criComp : criComps){
            criComp.setWeights(newWeight);
        }
        return true;
    }

    // return example: Total = weight * assigments + weight * exam
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total = ");
        for (int i = 0; i < categories.size(); i++){
            sb.append(String.format("%.2f",categories.get(i).getWeight()));
            sb.append("x");
            sb.append(categories.get(i).getName());
            sb.append("+");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    // return formated String of a certain category
    public String toString(int indexOfCategory){
        if (indexOfCategory < 0 || indexOfCategory >= categories.size()) return "index is not valid";
        Category category = categories.get(indexOfCategory);
        if (category == null) return "Such category does not exist.";
        List<CriComp> criComps = category.getCriComps();
        if (criComps == null || criComps.size() == 0) return "Please add some taks in this category.";
        StringBuilder sb = new StringBuilder();
        sb.append("Total = ");
        for (int i = 0; i < criComps.size(); i++){
            sb.append(String.format("%.2f",criComps.get(i).getWeights())+"x"+category.getName()+(i+1)+"+");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    /**
    * @Description: read the serialized object
    * @Param: String path
    * @Return: a NewCriterion object
    * @Author: Zhizhou Qiu
    * @Date: 2019/4/30
    **/
    @Override
    public NewCriterion readFromFile(String path) {
        NewCriterion newCriterion = null;
        try{
            FileInputStream file = new FileInputStream
                    (path);
            ObjectInputStream in = new ObjectInputStream
                    (file);

            newCriterion = (NewCriterion) in.readObject();
            in.close();
            file.close();
        } catch (IOException io){
            io.printStackTrace();
        } catch (ClassNotFoundException cl){
            cl.printStackTrace();
        }
        return newCriterion;
    }

    /**
    * @Description: serialize a NewCriterion object and write to file
    * @Param: String path
    * @Return: void
    * @Author: Zhizhou Qiu
    * @Date: 2019/4/30
    **/
    @Override
    public void writeToFile(String path) {
        try{
            FileOutputStream file = new FileOutputStream
                    (path);
            ObjectOutputStream out = new ObjectOutputStream
                    (file);
            out.writeObject(this);
            out.close();
            file.close();
//            System.out.println("object has been serialized\n + Data before serialization.");

        } catch (IOException io){
            io.printStackTrace();
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
