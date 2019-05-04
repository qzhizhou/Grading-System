package course;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @descriptions: this is a class that when a course is initialized,
 * the number of assignments, exams etc are assigned and each weight is clarified
 */
public class Criterion implements IO<Criterion>, Serializable {

    private final static int DEFAULT_NUMBER = 2;
    private List<CriComp> assignments;
    private List<CriComp> exams;
    private List<CriComp> projects;
    private double weightsOfAssignments;
    private double weightsOfExams;
    private double weightsOfProjects;
    private double weightsOfAttendance;
    private int numberOfAssignments;
    private int numberOfExams;
    private int numberOfProjects;
    private String name;


    /**
     * @Description: the constructors of Criterion, by default the params are valid
     * @Param: double: weightsOfAssignments,      int: numberOfAssignments
     *                  weightsOfExams,                 numberOfExams
     *                  weightsOfProjects,              numberOfProjects
     *                  weightsOfAttendance,
     * @Author: Zhizhou Qiu
     * @Date: 2019/4/12
     **/
    public Criterion(
            double weightsOfAssignments,
            double weightsOfExams,
            double weightsOfProjects,
            double weightsOfAttendance,
            int numberOfAssignments,
            int numberOfExams,
            int numberOfProjects,
            String name
    ) {
        this.name = name;
        this.weightsOfAssignments = weightsOfAssignments;
        this.weightsOfAttendance = weightsOfAttendance;
        this.weightsOfExams = weightsOfExams;
        this.weightsOfProjects = weightsOfProjects;
        this.numberOfAssignments = numberOfAssignments;
        this.numberOfExams = numberOfExams;
        this.numberOfProjects = numberOfProjects;
        assignments = new ArrayList<>();
        exams = new ArrayList<>();
        projects = new ArrayList<>();
        for(int i=0;i<numberOfAssignments;i++){
            assignments.add(new CriComp(1.0/numberOfAssignments,100.0));
        }
        for(int i=0;i<numberOfExams;i++){
            exams.add(new CriComp(1.0/numberOfExams,100.0));
        }
        for(int i=0;i<numberOfProjects;i++){
            projects.add(new CriComp(1.0/numberOfProjects,100.0));
        }
    }

    /**
     * @Description: default constructor
     * @Param:
     * @Return:
     * @Author: Zhizhou Qiu
     * @Date: 2019/4/12
     **/
    public Criterion() {
        createDefaultCriterion();
    }

    /**
     * add an exam, weights would be auto balanced according to size
     */
    public void addExam(){
        if (this.exams == null) exams = new ArrayList<CriComp>();
        this.exams.add(new CriComp(0,100.0));
        int size = exams.size();
        double newWeight = 1.0 / size;
        for (CriComp criComp : exams){
            criComp.setWeights(newWeight);
        }
        this.numberOfExams++;
    }


    /**
     * add an assignment, weights would be auto balanced according to size
     */
    public void addAssignment(){
        if (this.assignments == null) assignments = new ArrayList<CriComp>();
        this.assignments.add(new CriComp(0, 100.0));
        int size = assignments.size();
        double newWeight = 1.0 / size;
        for (CriComp criComp : assignments){
            criComp.setWeights(newWeight);
        }
        this.numberOfAssignments++;
    }

    /**
     * add a project, weights would be auto balanced according to size
     */
    public void addProject(){
        if (this.projects == null) projects = new ArrayList<CriComp>();
        this.projects.add(new CriComp(0, 100.0));
        int size = projects.size();
        double newWeight = 1.0 / size;
        for (CriComp criComp : projects){
            criComp.setWeights(newWeight);
        }
        this.numberOfProjects++;
    }

    public String toString(){
        return "total="+getWeightsOfAssignments()+"x"+"Assignment"+"+"+getWeightsOfExams()+"x"+"Exams"+"+"+getWeightsOfProjects()+"x"+"Projects"+"+"+getWeightsOfAttendance()+"x"+"Attendance";
    }
    public String toString(String type){
        String res = "";
        if(type=="a"){
            for(int i=0;i<getAssignments().size();i++){
                res+=getAssignments().get(i).getWeights()+"x"+"Assignment"+(i+1)+"+";
            }
        }else if(type=="e"){
            for(int i=0;i<getExams().size();i++){
                res+=getExams().get(i).getWeights()+"x"+"Exam"+(i+1)+"+";
            }
        }else{
            for(int i=0;i<getProjects().size();i++){
                res+=getProjects().get(i).getWeights()+"x"+"Project"+(i+1)+"+";
            }
        }
        return res.substring(0,res.length()-1);
    }

    @Override
    public Criterion readFromFile(String path) {
        Criterion criterion = null;
        try{
            FileInputStream file = new FileInputStream
                    (path);
            ObjectInputStream in = new ObjectInputStream
                    (file);

            criterion = (Criterion) in.readObject();
            in.close();
            file.close();
        } catch (IOException io){
            io.printStackTrace();
        } catch (ClassNotFoundException cl){
            cl.printStackTrace();
        }
        return criterion;
    }

    @Override
    public void writeToFile(String path){
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

    /**
     * @return the number of current assigments, exams, projects
     */
    public int getNumberOfAssignments() {
        if (this.assignments == null) return -1;
        return this.assignments.size();
    }

    public int getNumberOfExams() {
        if (this.exams == null) return -1;
        return this.exams.size();
    }

    public int getNumberOfProjects() {
        if (this.projects == null) return -1;
        return this.projects.size();
    }

    public List<CriComp> getAssignments() {
        return assignments;
    }

    public List<CriComp> getExams() {
        return exams;
    }

    public List<CriComp> getProjects() {
        return projects;
    }

    public double getWeightsOfAssignments() {
        return weightsOfAssignments;
    }

    public double getWeightsOfExams() {
        return weightsOfExams;
    }

    public double getWeightsOfProjects() {
        return weightsOfProjects;
    }

    public double getWeightsOfAttendance() {
        return weightsOfAttendance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @Description: create default criterion, for each category the default number is 2, and the weight is 0.25
     * @Param:
     * @Return:
     * @Author: Zhizhou Qiu
     * @Date: 2019/4/12
     **/
    private void createDefaultCriterion() {
        this.name = "Default_Criterion";
        this.assignments = new ArrayList<CriComp>();
        this.projects = new ArrayList<CriComp>();
        this.exams = new ArrayList<CriComp>();
        this.weightsOfProjects = 0.25;
        this.weightsOfExams = 0.25;
        this.weightsOfAttendance = 0.25;
        this.weightsOfAssignments = 0.25;
        this.numberOfProjects = DEFAULT_NUMBER;
        this.numberOfAssignments = DEFAULT_NUMBER;
        this.numberOfExams = DEFAULT_NUMBER;
        for (int i = 0; i < DEFAULT_NUMBER; i++) {
            this.assignments.add(new CriComp());
            this.projects.add(new CriComp());
            this.exams.add(new CriComp());
        }
    }
}
