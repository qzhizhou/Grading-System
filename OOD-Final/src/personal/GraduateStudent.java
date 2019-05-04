package personal;

public class GraduateStudent extends Student{

    private final String TYPE = "GraduateStudent";

    public GraduateStudent(String id, Name Name, String email){
        super();
    }

    public GraduateStudent() {super();}

    // return the type of current student
    public String getTYPE(){
        return TYPE;
    }

}
