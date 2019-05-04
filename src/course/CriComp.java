package course;

import java.io.Serializable;

/**
 * @description: this a class to manage the weight of every assignment/exam/project
 * @author: Zhizhou Qiu
 * @date: 04-12-2019
 */
public class CriComp implements Serializable {
    private final static double DEFAULT_TOTALSCORE = 100.0;
    private final static int DEFAULT_NUMBER = 2;
    private double weights;
    private double toatalScore;

    public CriComp(double weights, double totalScore){
        this.weights = weights;
        this.toatalScore = totalScore;
    }

    /**
    * @Description: default constructor
    * @Author: Zhizhou Qiu
    * @Date: 2019/4/12
    **/
    public CriComp() {
        this.weights = 1.0 / DEFAULT_NUMBER;
        this.toatalScore = DEFAULT_TOTALSCORE;
    }

    @Override
    public String toString() {
        return "{ weight: " + weights +
                ", toatalScore=" + toatalScore +
                "}\n";
    }

    /**
    * @Description: getters and setters for current weight and totalscore
    * @Author: Zhizhou Qiu
    * @Date: 2019/4/12
    **/
    public double getToatalScore() {
        return toatalScore;
    }

    public void setToatalScore(double totalScore) {
        System.out.println(totalScore);
        this.toatalScore = totalScore;
    }

    public double getWeights() {
        return weights;
    }

    public void setWeights(double weights) {
        System.out.println(weights);
        this.weights = weights;
    }
}
