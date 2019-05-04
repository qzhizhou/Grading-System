package UI.Table.Header.Groupable;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.*;

/**
 * @Auther: Di Zhu
 * @Date: 04-21-2019 17:00
 * @Description: Header which can group columns
 */
public class GroupableTableHeader extends JTableHeader {

    @SuppressWarnings("unused")
    private static final String uiClassID = "GroupableTableHeaderUI";

    protected List<ColumnGroup> columnGroups = new ArrayList<ColumnGroup>();

    public GroupableTableHeader(TableColumnModel model) {
        super(model);
        setUI(new GroupableTableHeaderUI());
        setReorderingAllowed(false);
        // setDefaultRenderer(new MultiLineHeaderRenderer());
    }

    @Override
    public void updateUI() {
        setUI(new GroupableTableHeaderUI());
    }

    @Override
    public void setReorderingAllowed(boolean b) {
        super.setReorderingAllowed(false);
    }

    public void addColumnGroup(ColumnGroup g) {
        columnGroups.add(g);
    }

    public List<ColumnGroup> getColumnGroups(TableColumn col) {
        for (ColumnGroup group : columnGroups) {
            List<ColumnGroup> groups = group.getColumnGroups(col);
            if (!groups.isEmpty()) {
                return groups;
            }
        }
        return Collections.emptyList();
    }

    public List<ColumnGroup> getColumnGroups() {
        return columnGroups;
    }

    public int findIndexOfGroup(String groupName) {
        for (int i = 0 ; i < columnGroups.size() ; i++) {
            if (columnGroups.get(i).getHeaderValue().equals(groupName)) {
                return i;
            }
        }
        return -1;
    }

    public void setColumnMargin() {
        int columnMargin = getColumnModel().getColumnMargin();
        for (ColumnGroup group : columnGroups) {
            group.setColumnMargin(columnMargin);
        }
    }

}
