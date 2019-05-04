package UI.Bottom.Functionality;

import UI.MainFrame;
import UI.Table.SortTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Auther: Di Zhu
 * @Date: 05-03-2019 12:40
 * @Description: Panel for performing searching operations
 */
public class SearchPanel extends JPanel {

    private JTable gradeTable;
    private JTextField searchingField;

    public SearchPanel() {

    }

    public SearchPanel(MainFrame mainFrame) {
        gradeTable = mainFrame.getGradeTable();
        initialization();
    }

    private void initialization() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchingField = new JTextField();
        searchingField.setPreferredSize(new Dimension(128, 54));

        searchingField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchingField.getText();
                TableRowSorter<SortTableModel> rowSorter = (TableRowSorter) gradeTable.getRowSorter();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });


        this.add(searchingField);
    }


}
