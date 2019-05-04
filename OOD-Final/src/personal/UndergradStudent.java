package personal;

public class UndergradStudent extends Student{

    private final String TYPE = "UndergradStudent";

    public UndergradStudent(String id, Name name, String email){
        super();
    }

    public UndergradStudent(){ super();}

    // return the type of current student
    public String getTYPE(){
        return TYPE;
    }

}
