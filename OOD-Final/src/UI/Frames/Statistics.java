package UI.Frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import course.*;

/**
 * @Auther: wangqitong
 * @Date: 04-12-2019 21:50
 * @Description:
 */
public class Statistics extends JFrame implements ActionListener {
    private JButton closeButton;
    private JLabel meanString, maxString, minString, stdString, medianString;
    private JPanel meanPanel, maxPanel, minPanel, stdPanel, medianPanel, buttonPanel, nullPanel;
    private String[] pointScore;

    public Statistics(Course inputCourse) {
        // TODO Auto-generated constructor stub
        pointScore = inputCourse.getAnalysis();
        initialization();
    }

    public Statistics(Course inputCourse, int cateIndex, int index) {
        pointScore = inputCourse.getAnalysis(cateIndex, index);
        initialization();
    }


    private void initialization() {
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);

        String initMeanString = "Mean: " + pointScore[0];
        String initMaxString = "Max Score: " + pointScore[1];
        String initMinString = "Min Score: " + pointScore[2];
        String initStdString = "Std: " + pointScore[4];
        String initMedianString = "Median: " + pointScore[3];

        meanString = new JLabel(initMeanString);
        maxString = new JLabel(initMaxString);
        minString = new JLabel(initMinString);
        stdString = new JLabel(initStdString);
        medianString = new JLabel(initMedianString);

        meanPanel = new JPanel();
        maxPanel = new JPanel();
        minPanel = new JPanel();
        stdPanel = new JPanel();
        medianPanel = new JPanel();
        buttonPanel = new JPanel();
        nullPanel = new JPanel();

        meanPanel.add(meanString);
        maxPanel.add(maxString);
        minPanel.add(minString);
        stdPanel.add(stdString);
        medianPanel.add(medianString);
        buttonPanel.add(closeButton);

        nullPanel.setBounds(0, 0, 300, 10);
        meanPanel.setBounds(25, 25, 250, 25);
        maxPanel.setBounds(25, 50, 250, 25);
        minPanel.setBounds(25, 75, 250, 25);
        stdPanel.setBounds(25, 100, 250, 25);
        medianPanel.setBounds(25, 125, 250, 25);
        buttonPanel.setBounds(100, 160, 100, 30);

        this.add(buttonPanel);
        this.add(medianPanel);
        this.add(stdPanel);
        this.add(minPanel);
        this.add(maxPanel);
        this.add(meanPanel);
        this.add(nullPanel);

        this.setTitle("Statistics");
//        this.setLayout(new GridLayout(3, 1));
        this.setSize(300, 230);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setVisible(true);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="Close") {
            this.dispose();
//            System.exit(0);
        }
    }
}
