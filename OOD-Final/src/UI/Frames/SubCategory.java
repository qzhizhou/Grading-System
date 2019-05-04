package UI.Frames;

import UI.MainFrame;
import course.Category;
import course.NewCriterion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @Auther: wangqitong
 * @Date: 04-12-2019 20:57
 * @Description:
 */
public class SubCategory extends JFrame implements ActionListener {
    protected JButton okButton;
    protected JLabel weightString;
    protected JTable table;
    protected JScrollPane tableScrollPane;
    protected JPanel weightPanel, tablePanel, buttonPanel;
    protected TableColumn column;
    private NewCriterion newCriterion;
    private MainFrame mainFrame;
    private int itemNumber, savedIndex;

    public SubCategory(MainFrame inputMainFrame, NewCriterion inputNewCriterion, int index){
        this.mainFrame = inputMainFrame;
        this.newCriterion = inputNewCriterion;
        this.savedIndex = index;
        this.itemNumber = newCriterion.getCategories().get(index).getNumberOfTasks();

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        String[] tableHead = {"Category", "Weight", "Full Marks"};

        List<List<String>> data = new ArrayList<>();
        List<String> tempFirstList = new ArrayList<>();
        tempFirstList.add(newCriterion.getCategories().get(index).getName());
        tempFirstList.add("Weight");
        tempFirstList.add("Full Marks");
        data.add(tempFirstList);
        for (int i = 1; i <= itemNumber; i++){
            List<String> tempList = new ArrayList<>();
            tempList.add(String.valueOf(i));
            tempList.add(String.format("%.8f", newCriterion.getCategories().get(index).getCriComps().get(i - 1).getWeights()));
            tempList.add(String.valueOf(newCriterion.getCategories().get(index).getCriComps().get(i - 1).getToatalScore()));
            data.add(tempList);
        }

        Object[][] finalData = new Object[data.size()][3];
        for (int i = 0; i <= itemNumber; i++){
            finalData[i][0] = data.get(i).get(0);
            finalData[i][1] = data.get(i).get(1);
            finalData[i][2] = data.get(i).get(2);
        }

        table = new JTable(finalData, tableHead){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    if (row == 0)
                        return true;
                    else
                        return false;
                } else if (row == 0)
                    return false;
                return true;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.setShowGrid(true);
        table.setGridColor(Color.gray);
//        inputNewCriterion.getCategories().get(index).setName();
        table.setRowHeight(50);// Setting the width of rol
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);// Setting the width of column
        for (int i = 0; i < 3; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(134);
        }
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, center);

        weightString = new JLabel("");
        tableScrollPane = new JScrollPane(table);
//        add(tableScrollPane);
//        table.setFillsViewportHeight(true);

        buttonPanel = new JPanel();
        weightPanel = new JPanel();
        tablePanel = new JPanel();

        weightPanel.add(weightString);
        tablePanel.add(table);
        buttonPanel.add(okButton);

        weightPanel.setBounds(0, 20, 402, 50);
        tablePanel.setBounds(0, 50, 402, 50 * itemNumber + 50);
        buttonPanel.setBounds(151, 50 * itemNumber + 140, 100, 50 * itemNumber + 200);

        this.add(buttonPanel);
        this.add(tablePanel);
        this.add(weightPanel);

        this.setTitle("Sub-category");
//        this.setLayout(new GridLayout(3, 1));
        this.setSize(402, 50 * itemNumber + 220);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setVisible(true);
        this.setResizable(false);
        this.mainFrame.setEnabled(false);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mainFrame.setEnabled(true);
                dispose();
            }
        });
    }

    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        if(pattern.matcher(str).matches()){
            if(Double.parseDouble(str) >= 0.0 && Double.parseDouble(str) <= 1.0)
                return true;
            else
                return false;
        }
        return false;
    }

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        if(pattern.matcher(str).matches()){
            if(Double.valueOf(str) >= 0)
                return true;
            else
                return false;
        }
        return false;
    }

    private boolean judgeWeightNumber(){
        boolean returnValue = true;
        for (int i = 1; i <= itemNumber; i++){
            if(!isDouble(table.getValueAt(i, 1).toString()))
                returnValue = false;
        }
        return returnValue;
    }

    private boolean judgeFullMarksNumber(){
        boolean returnValue = true;
        for (int i = 1; i <= itemNumber; i++){
            if(!isNumeric(table.getValueAt(i, 2).toString()))
                returnValue = false;
        }
        return returnValue;
    }

    private boolean judgeWeightSum(){
        double sum = 0.0;
        for (int i = 1; i <= itemNumber; i++)
            sum += Double.parseDouble(table.getValueAt(i, 1).toString());
        System.out.println(sum);
        if(Math.abs(sum - 1.0) <= 0.001)
            return true;
        else
            return false;
    }

    private void updateTabelData(){
        Category category = newCriterion.getCategories().get(this.savedIndex);
        category.setName(this.table.getValueAt(0, 0).toString());
        System.out.println("nums"+category.getNumberOfTasks());
        for(int i=0;i<category.getNumberOfTasks();i++){
            category.getCriComps().get(i).setWeights(Double.valueOf(this.table.getValueAt(i+1,1).toString()));
            category.getCriComps().get(i).setToatalScore(Double.valueOf(this.table.getValueAt(i+1,2).toString()));
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="OK") {
            if(judgeWeightNumber()) {
                if(judgeFullMarksNumber())
                {
                    if(judgeWeightSum())
                    {
                        updateTabelData();
                        this.mainFrame.updateCriterion(newCriterion);

//                    this.mainFrame.repaint();
                        this.mainFrame.setEnabled(true);
                        this.dispose();
                    }
                    else
                        JOptionPane.showMessageDialog(null, "The sum of item weight must be 1.0!", "Info", JOptionPane.WARNING_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Full marks of each item must be legal number bigger than 0!", "Info", JOptionPane.WARNING_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Item weight must be float ranging from 0 to 1!", "Info", JOptionPane.WARNING_MESSAGE);
            }
        }

    }
}
