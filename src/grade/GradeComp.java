package grade;

import java.io.*;


public class GradeComp implements Serializable{
    private String score;
    private Note gNote;
    public GradeComp() {
        score = "0";
        gNote = new Note();
    }
    public GradeComp(String s) {
        score = s;
        gNote = new Note();
    }

    public String getScore(){
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Note getNote(){
        return gNote;
    }

}
