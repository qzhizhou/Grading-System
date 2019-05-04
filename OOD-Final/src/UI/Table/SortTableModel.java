package UI.Table;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @Auther: Di Zhu
 * @Date: 04-16-2019 21:05
 * @Description: Table model for gradeTable, for sorting
 */
public class SortTableModel extends DefaultTableModel {

    private List<Class> classList = new ArrayList<>();

    public SortTableModel() {
        super();
    }

    public SortTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }


    public void addOneClass(Class type) {
        this.classList.add(type);
    }


    public void addColumn(Object columnName, Class type) {
        this.addOneClass(type);
        super.addColumn(columnName);
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return this.classList.get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 1 || column == getColumnCount() - 1 || column == 3) {
            return false;
        }
        return true;
    }

    /*** Setter and Getter ***/
    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }
}
