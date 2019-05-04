package grade;

import course.NewCriterion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @Auther:Maoxuan Zhu
 * @Date:04-15-201921:20
 * @Description: this class represent the grade consist of 4 component(assignment, exam, project and attendance)
 **/
public class Grade implements Serializable,Comparable {
    private String uid;
    private List<List<GradeComp>> grade;
    private GradeComp extraCredit;
    private double ttscore = 0;
    private String wd;
    //default constructor
    public Grade(){
        grade = new ArrayList<>();
        extraCredit = new GradeComp();
        wd = "";
    }
    public Grade(NewCriterion c, String id){
        uid = id;
        grade = new ArrayList<>();
        extraCredit = new GradeComp();
        for(int i=0;i<c.getCategories().size();i++){
            List<GradeComp> temp = new ArrayList<>();
            for(int j=0;j<c.getCategories().get(i).getCriComps().size();j++){
                temp.add(new GradeComp());
            }
            grade.add(temp);
        }
        wd = "";
    }

    //get list of grade of one category
    public List<GradeComp> getCategory(int cat){
        return grade.get(cat);
    }
    //get particular assignment/exam/project
    public GradeComp getOne(int cat, int index){
        return grade.get(cat).get(index);
    }
    public String getUid(){ return uid; }

    public GradeComp getExtra(){
        return extraCredit;
    }
    //add new assignment/exam/project
    public void addNew(int cat){
        grade.get(cat).add(new GradeComp());
    }
    //setter
    public void setUid(String s){
        uid = s;
    }
    public void setTtscore(double a){
        ttscore = a;
    }

    @Override
    public int compareTo(Object o) {
        Double a = this.getTtscore();
        Double b = ((Grade) o).getTtscore();
        return a.compareTo(b);
    }
    public void setExtra(String s){
        extraCredit = new GradeComp(s);
    }
    //getter
    public double getTtscore(){
        return ttscore;
    }

    public int getCatelength(int cat){
        return grade.get(cat).size();
    }

    public void withDraw(){
        wd = "W";
    }

    public boolean ifWithDraw(){
        return wd.equals("W");
    }
    
    public void addList(){
        List<GradeComp> temp = new ArrayList<>();
        for(int j=0;j<2;j++){
            temp.add(new GradeComp());
        }
        grade.add(temp);
    }
}
