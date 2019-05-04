package course;

import java.io.*;
import grade.Grade;
import grade.GradeComp;
import javafx.util.Pair;
import personal.Student;
import java.util.*;

/**
 * @Auther:Maoxuan Zhu
 * @Date:04-15-201921:20
 * @Description: this class represent a course and store grade of each students enroll in this course in a hashmap
 **/
public class Course implements Analysis,IO<Course>,Serializable{
    private String cname;
    private String cid;
    private String semester;
    private String cyear;
    private NewCriterion ccriterion;
    private HashMap<Student, Grade> cgrade;
    private static final long serialVersionUID = -4689572748727448978L;
    // Default constructor
    public Course(){
        cname = "Course01";
        cid = "000";
        semester = "Fall";
        cyear = "2019";
        ccriterion = new NewCriterion();
        cgrade = new HashMap<>();
    }
    public Course(String name, String id, String s, String y, NewCriterion c){
        cname = name;
        cid = id;
        semester = s;
        cyear = y;
        ccriterion = c;
        cgrade = new HashMap<>();
    }
    //enroll a student to this course
    public void enrollStudent(Student s){
        Grade temp = new Grade(ccriterion,s.getId());
        cgrade.put(s,temp);
    }
    //get student object
    public Student getStudent(String id){
        for(Student key: cgrade.keySet()){
            if(key.getId().equals(id)){
                return key;
            }
        }
        return null;
    }
    //get the grade object of student s
    public Grade getsGrade(Student s){
        return cgrade.get(s);
    }
    //get grade list
    public HashMap<Student, Grade> getList(){
        return cgrade;
    }
    //get course information
    public String[] getInfo(){
        String[] res =new String[]{cname,cid,semester,cyear};
        return res;
    }
    //get grading criterion
    public NewCriterion getCcriterion(){
        return ccriterion;
    }
    //update grade #assignment exam project
    public void updateGrade(){
        for(Student k: cgrade.keySet()){
            for(int i=0;i<ccriterion.getCategories().size();i++){
                if(getsGrade(k).getCategory(i).size()<ccriterion.getCategories().get(i).getCriComps().size()){
                    int addnum = ccriterion.getCategories().get(i).getCriComps().size()-getsGrade(k).getCategory(i).size();
                    for(int j=0;j<addnum;j++){
                        getsGrade(k).getCategory(i).add(new GradeComp());
                    }
                }
            }
        }
    }

    public void addCategory(){
        for(Student k: cgrade.keySet()) {
            getsGrade(k).addList();
        }
    }
    //get analysis data of total score
    public String[] getAnalysis(){
        String[] res = new String[5];
        double tt = 0;
        int count = 0;
        double maxd = 0;
        double mind = 100;
        double dd;
        List<Grade> sortList = new ArrayList<>();
        for (Student key : cgrade.keySet()) {
            if(!getsGrade(key).ifWithDraw()){
                tt += cgrade.get(key).getTtscore();
                maxd = Math.max(maxd,cgrade.get(key).getTtscore());
                mind = Math.min(mind,cgrade.get(key).getTtscore());
                count += 1;
                sortList.add(cgrade.get(key));
            }
        }
        Collections.sort(sortList);
        res[0] = Double.toString(tt/(double)count);
        res[1] = Double.toString(maxd);
        res[2] = Double.toString(mind);
        if(sortList.size()%2==0){
            dd = sortList.get(sortList.size()/2).getTtscore()+sortList.get(sortList.size()/2-1).getTtscore();
            dd /= 2;
        }else{
            dd = sortList.get(sortList.size()/2).getTtscore();
        }
        res[3] = Double.toString(dd);
        double sum = 0;
        double ave = tt/(double)count;
        for(int i=0;i<sortList.size();i++){
            sum+= Math.pow(ave-sortList.get(i).getTtscore(),2);
        }
        sum = Math.sqrt(sum/(double)count);
        res[4] = Double.toString(sum);
        return res;
    }
    //get analysis data of one chosen field("a" for assignment. "e" for exam, "p" for project)
    public String[] getAnalysis(int cat,int index){
        String[] res = new String[5];
        double tt = 0;
        int count = 0;
        double maxd = 0;
        double mind = 100;
        double dd;
        List<Pair<String,Double>> sg = new ArrayList<>();
        double fs = ccriterion.getCategories().get(cat).getCriComps().get(index).getToatalScore();
        double sc;
        for (Student key : cgrade.keySet()) {
            if(!getsGrade(key).ifWithDraw()){
                String rawsc = cgrade.get(key).getOne(cat,index).getScore();
                if (rawsc.charAt(rawsc.length() - 1) == '%') {
                    sc = Double.valueOf(rawsc.substring(0, rawsc.length() - 1)) / 100;
                } else if (rawsc.charAt(0) == '-') {
                    sc = (fs - Double.valueOf(rawsc.substring(1))) / fs;
                } else {
                    sc = Double.valueOf(rawsc) / fs;
                }
                sg.add(new Pair<>(key.getId(),sc * fs));
                maxd = Math.max(maxd,sc * fs);
                mind = Math.min(mind,sc * fs);
                tt += sc * fs;
                count += 1;
            }
        }
        Collections.sort(sg, Comparator.comparing(p -> p.getValue()));
        res[0] = Double.toString(tt / (double) count);
        res[1] = Double.toString(maxd);
        res[2] = Double.toString(mind);
        if(sg.size()%2==0){
            dd = sg.get(sg.size()/2).getValue()+sg.get(sg.size()/2-1).getValue();
            dd /= 2;
        }else{
            dd = sg.get(sg.size()/2).getValue();
        }
        res[3] = Double.toString(dd);
        double sum = 0;
        double ave = tt/(double)count;
        for(int i=0;i<sg.size();i++){
            sum+= Math.pow(ave-sg.get(i).getValue(),2);
        }
        sum = Math.sqrt(sum/(double)count);
        res[4] = Double.toString(sum);
        return res;
    }
    //calculate the total score of a given grade object
    public void calculateTotal(Grade g){
        double total = 0;
        for(int i=0;i<ccriterion.getCategories().size();i++) {
            double cw = ccriterion.getCategories().get(i).getWeight();
            for (int j = 0; j < ccriterion.getCategories().get(i).getCriComps().size(); j++) {
                double fs = ccriterion.getCategories().get(i).getCriComps().get(j).getToatalScore();
                double we = ccriterion.getCategories().get(i).getCriComps().get(j).getWeights();
                String sc = g.getCategory(i).get(j).getScore();
                double stdsc;
                if(sc.charAt(sc.length()-1)=='%'){
                    stdsc = Double.valueOf(sc.substring(0,sc.length()-1))/100;
                }else if(sc.charAt(0)=='-'){
                    stdsc = (fs-Double.valueOf(sc.substring(1)))/fs;
                }else {
                    stdsc = Double.valueOf(sc) / fs;
                }
                total += stdsc*we*cw*100;
            }
        }
        g.setTtscore(total);

    }
    //calculate total score of all students
    public void calculateAll(){
        for (Student key : cgrade.keySet()) {
            calculateTotal(cgrade.get(key));
        }
    }

    public void setCcriterion(NewCriterion c1){
        ccriterion = c1;
    }

    public void withDraw(Student s){
        getsGrade(s).withDraw();
    }

    @Override
    public Course readFromFile(String path) {
        Course c = null;
        try{
            FileInputStream file = new FileInputStream
                    (path);
            ObjectInputStream in = new ObjectInputStream
                    (file);

            c = (Course) in.readObject();
            in.close();
            file.close();
        } catch (IOException io){
            io.printStackTrace();
        } catch (ClassNotFoundException cl){
            cl.printStackTrace();
        }
        return c;
    }

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

        } catch (IOException io){
            io.printStackTrace();
        }
    }

    public void deleteOne(int cat, int index){
        getCcriterion().getCategories().get(cat).deleteOne(index);
        for(Student k: cgrade.keySet()){
            getsGrade(k).getCategory(cat).remove(index);
        }
    }

    public HashMap<Student, Grade> getCgrade() {
        return cgrade;
    }
}
