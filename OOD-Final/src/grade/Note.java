package grade;

import java.io.*;

public class Note implements Serializable{
    private String content;
    public Note() {
        content = "";
    }
    public Note(String s) {
        content = s;
    }
    public void setNote(String s) {
        content = s;
    }
    public String getNote() {
        return content;
    }
}
