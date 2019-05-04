package UI.Frames;

import UI.MainFrame;
import course.Category;
import course.NewCriterion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: wangqitong
 * @Date: 05-03-2019 01:03
 * @Description:
 */
public class TotalCategory extends JFrame implements ActionListener {
    protected JButton okButton;
    protected JLabel weightString;
    protected JTable table;
    protected JScrollPane tableScrollPane;
    protected JPanel weightPanel, tablePanel, buttonPanel;
    protected TableColumn column;
    private NewCriterion newCriterion;
    private MainFrame mainFrame;
    private int itemNumber;

    public TotalCategory(MainFrame inputMainFrame, NewCriterion inputNewCriterion){
        this.mainFrame = inputMainFrame;
        this.newCriterion = inputNewCriterion;
        this.itemNumber = newCriterion.getCategories().size();

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        String[] tableHead = {"Category", "Weight"};

        List<List<String>> data = new ArrayList<>();
        List<String> tempFirstList = new ArrayList<>();
        tempFirstList.add("");
        tempFirstList.add("Weight");
        data.add(tempFirstList);
        for (int i = 0; i < itemNumber; i++){
            List<String> tempList = new ArrayList<>();
            tempList.add(newCriterion.getCategories().get(i).getName());
            tempList.add(String.format("%.8f", newCriterion.getCategories().get(i).getWeight()));
            data.add(tempList);
        }

        Object[][] finalData = new Object[data.size()][2];
        for (int i = 0; i <= itemNumber; i++){
            finalData[i][0] = data.get(i).get(0);
            finalData[i][1] = data.get(i).get(1);
        }

        table = new JTable(finalData, tableHead){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
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
        for (int i = 0; i < 2; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(200);
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

        weightPanel.setBounds(0, 20, 400, 50);
        tablePanel.setBounds(0, 50, 400, 50 * itemNumber + 50);
        buttonPanel.setBounds(150, 50 * itemNumber + 140, 100, 50 * itemNumber + 200);

        this.add(buttonPanel);
        this.add(tablePanel);
        this.add(weightPanel);

        this.setTitle("Sub-category");
//        this.setLayout(new GridLayout(3, 1));
        this.setSize(400, 50 * itemNumber + 220);
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
        List<Category> category = newCriterion.getCategories();

        for(int i=0;i<category.size();i++){
            category.get(i).setWeight(Double.valueOf(this.table.getValueAt(i+1,1).toString()));
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="OK") {
            if(judgeWeightNumber()) {
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
                JOptionPane.showMessageDialog(null, "Item weight must be float ranging from 0 to 1!", "Info", JOptionPane.WARNING_MESSAGE);
            }
        }

    }
}
