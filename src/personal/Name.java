package personal;


import java.io.Serializable;

public class Name implements Serializable {

    private String fname;
    private String mname;
    private String lname;

    public Name(){
        this.fname = "";
        this.mname = "";
        this.lname = "";
    }

    public Name(String name){
        this.fname = name;
        this.mname = "";
        this.lname = "";
    }



    public Name(String f, String m, String l){
        this.fname = f;
        this.mname = m;
        this.lname = l;
    }

    @Override
    public String toString() {
        return fname + ' ' + mname + ' ' + lname;
    }

    /**
     * @Description: getters and setters
     * @Author: Zhizhou Qiu
     * @Date: 2019/4/12
     **/
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
