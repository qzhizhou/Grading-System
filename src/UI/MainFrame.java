package UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import UI.Bottom.SwitchPanel;
import UI.Table.Header.CheckBox.CheckBoxTableModel;
import UI.Table.Header.Groupable.ColumnGroup;
import UI.Table.Header.Groupable.GroupableTableHeader;
import UI.Bottom.ImagePanel;
import UI.Table.SortTableModel;
import course.Category;
import course.Course;
import course.ReadRawData;
import UI.Frames.AddCourse;
import course.NewCriterion;
import UI.Frames.CommentFrame;
import UI.Frames.SubCategory;
import UI.Frames.TotalCategory;
import grade.Grade;
import grade.GradeComp;
import personal.Student;

/**
 * @Auther: Di Zhu
 * @Date: 04-12-2019 12:19
 * @Description: Main interface for user to do grading
 */
public class MainFrame extends JFrame {

    //Containers
    private Container container;

    //Panels
    //private JPanel leftPanel;
    private JPanel middlePanel;
    private JPanel bottomPanel;
    //private JPanel rightPanel;

    /*** Components ***/
    private JMenuBar menuBar;
    private JTable gradeTable;
    private JTable fixedTable;
    private JTextField weightingField;

    //Left panel components
    //JTree
    private JTree jTree;
    //Initialization of JTree Node
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode("All courses");
    private DefaultTreeModel jMode = new DefaultTreeModel(root);

    //Bottom panel components
    private SwitchPanel switchPanel;

    private int windowWidth = 1280;
    private int windowHeight = 720;
    /*** Listener ***/
    //private TableMouseListener tableMouseListener;
    private boolean updatingFlag = false;

    /*** Data ***/
    private Map<String, Map<String, Course>> courseMap = new HashMap<>();
    private Course currentCourse = null;
    private Class[] type = {};

    private static final String fileDir = "course//";
    private static final String criDir = "pre//";
    private File directory;

    private void initialization() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        //Setting up navigation bar
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File (F)");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        final JMenuItem saveChangesItem = new JMenuItem("Save changes");
        saveChangesItem.addActionListener(e-> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            int retrival = chooser.showSaveDialog(null);
            if (retrival == JFileChooser.APPROVE_OPTION) {
                try {
                    currentCourse.getCcriterion().writeToFile(criDir + chooser.getSelectedFile());
                    currentCourse.writeToFile(fileDir + chooser.getSelectedFile() + ".txt");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            saveCourseAndCriterion();
        });
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(saveChangesItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        JMenu courseMenu = new JMenu("Course (C)");
        final JMenuItem deleteCourse = new JMenuItem("Delete Selected Course");
        deleteCourse.addActionListener(e-> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

            /** Remove File ***/
            for (String fileName : directory.list()) {
                //Remove all semester
                if (courseMap.containsKey(node.getUserObject())) {
                    String[] split = fileName.split("_");
                    if (split[0].equals(node.getUserObject())) {
                        File temp = new File(fileDir + fileName + ".txt");
                        temp.delete();
                    }
                }
                //Remove one semester
                else {
                    File temp = new File(fileDir + parent.getUserObject() + "_" + node.getUserObject() + ".txt");
                    temp.delete();
                }
            }

            /*** Remove from map ***/
            if (courseMap.containsKey(node.getUserObject())) {
                courseMap.remove(node.getUserObject());
            } else {
                courseMap.get(parent.getUserObject()).remove(node.getUserObject());
            }

            /*** Remove from tree ***/
            DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
            model.removeNodeFromParent(node);

            /*** Clear JTable ***/
            SortTableModel sortTableModel = (SortTableModel) gradeTable.getModel();
            Object[][] empty = new Object[][]{};
            sortTableModel.setDataVector(empty, empty);

            CheckBoxTableModel checkBoxTableModel = (CheckBoxTableModel) fixedTable.getModel();
            checkBoxTableModel.setColumnCount(0);
            //checkBoxTableModel.setDataVector(empty, empty);

            /*** Reset Bottom Panel ***/
            currentCourse = null;
            switchPanel.refreshPanel(currentCourse);
        });

        final JMenuItem newCourseMenuItem = new JMenuItem("New Course");
        newCourseMenuItem.addActionListener(e-> {
            try {
                new AddCourse(this);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
        });

        final JMenuItem importCourseMenuItem = new JMenuItem("Import Course from File");
        importCourseMenuItem.addActionListener(e-> {
            JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("json(*.json)", "json"));
            jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("text(*.txt)", "txt"));
            jFileChooser.showDialog(this, "Select");

            if (jFileChooser.getSelectedFile().getAbsolutePath() != null) {
                String type = jFileChooser.getSelectedFile().getAbsolutePath().toString().split("\\.")[1];
                Course course = null;
                if (type.equals("json")) {
                    course = ReadRawData.readRawData(jFileChooser.getSelectedFile().getAbsolutePath());
                    addCourse(course, root);
                } else if (type.equals("txt")) {
                    Course tool = new Course();
                    course = tool.readFromFile(jFileChooser.getSelectedFile().getAbsolutePath());
                    addCourse(course, root);
                } else {
                    JOptionPane.showMessageDialog(null, "File type illegal or unreadable!", "Info", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        courseMenu.add(newCourseMenuItem);
        courseMenu.add(importCourseMenuItem);
        courseMenu.add(deleteCourse);

        //JMenu studentMenu = new JMenu("Student (S)");
        //JMenu gradeMenu = new JMenu("Grade (G)");
        //JMenu settingsMenu = new JMenu("Settings (E)");
        menuBar.add(fileMenu);
        menuBar.add(courseMenu);
        //menuBar.add(studentMenu);
        //menuBar.add(gradeMenu);
        //menuBar.add(settingsMenu);
        this.setJMenuBar(menuBar);

        /*** Left Panel ***/
        //Set up JTree
        jTree = new JTree(jMode);
        jTree.setVisibleRowCount(0);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTree.addTreeSelectionListener(e-> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    jTree.getLastSelectedPathComponent();
            if (node == null)
                return;
            if (node.isRoot() && node.isLeaf()) {
                JOptionPane.showMessageDialog(null, "Please create or import course!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (node.isLeaf()) {
                DefaultMutableTreeNode potentialRoot = (DefaultMutableTreeNode) node.getParent();
                if (potentialRoot.isRoot()) {
                    JOptionPane.showMessageDialog(null, "This course does not have any semesters", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    //Clear selection first
                    //If index of the selected item does not exist in another table
                    gradeTable.getSelectionModel().clearSelection();
                    weightingField.setText("");
                    Object nodeInfo = node.getUserObject();
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                    Course course = courseMap.get(parent.getUserObject()).get(nodeInfo);
                    update(course);
                    currentCourse = course;
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
            }
        });

        JScrollPane treeScrollPane = new JScrollPane(jTree);
        treeScrollPane.setPreferredSize(new Dimension(windowWidth / 8, windowHeight));
        container.add(treeScrollPane, BorderLayout.WEST);

        /*** Center Panel ***/
        //North
        weightingField = new JTextField();

        //Center
        //Set up grade table
        gradeTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }

            Color color = getForeground();

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {


                int modelRow = convertRowIndexToModel(row);
                //int modelColumn = convertColumnIndexToModel(column);

                Component component = super.prepareRenderer(renderer, row, column);
                DefaultTableModel model =(DefaultTableModel) this.getModel();

                GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
                TableColumn tc = gradeTable.getColumnModel().getColumn(column);
                List<ColumnGroup> columnGroups = header.getColumnGroups(tc);

                String category = getCategory(column, columnGroups, columnGroups.size() != 0);
                String stuID = model.getValueAt(modelRow, 1).toString();
                Student stu = currentCourse.getStudent(stuID);

                if (columnGroups.size() != 0) {
                    int subCategory = getSubCategory(column, true);

                    if (!currentCourse.getsGrade(stu).getOne(header.findIndexOfGroup(category), subCategory - 1).getNote().getNote().equals("")) {
                        component.setForeground(Color.RED);
                    } else {
                        component.setForeground(color);
                    }
                } else if (category.equals("Extra")){
                    if(!currentCourse.getsGrade(stu).getExtra().getNote().getNote().equals(""))
                        component.setForeground(Color.RED);
                } else {
                    component.setForeground(color);
                }

                return component;
            }
        };

        //Set up table model
        SortTableModel sortTableModel = new SortTableModel();
        sortTableModel.setClassList(Arrays.asList(type));

        //Set up selection model
        ListSelectionModel lsm = gradeTable.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lsm.addListSelectionListener(new SelectionChangedListener());

        //Set up column model
        TableColumnModel tcm = gradeTable.getColumnModel();
        tcm.getSelectionModel().addListSelectionListener(new SelectionChangedListener());
        tcm.addColumnModelListener(new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {

            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {

            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {

            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                try {
                    final TableColumnModel tableColumnModel = gradeTable.getColumnModel();
                    TableColumnModel fixedTableColumnModel = fixedTable.getColumnModel();
                    for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
                        int w = tableColumnModel.getColumn(i).getWidth();
                        fixedTableColumnModel.getColumn(i).setMinWidth(w);
                        fixedTableColumnModel.getColumn(i).setMaxWidth(w);
                    }
                    fixedTable.doLayout();
                    fixedTable.repaint();
                    repaint();
                } catch (ArrayIndexOutOfBoundsException e1) {

                }
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {

            }
        });

        //Add models to table and set up mouse listener
        gradeTable.setModel(sortTableModel);
        TableMouseListener tableMouseListener = new TableMouseListener();
        gradeTable.addMouseListener(tableMouseListener);

        TableHeaderListener tableHeaderListener = new TableHeaderListener();
        gradeTable.getTableHeader().addMouseListener(tableHeaderListener);

        gradeTable.setSelectionModel(lsm);
        gradeTable.setColumnModel(tcm);

        gradeTable.setFillsViewportHeight(true);
        gradeTable.setAutoCreateRowSorter(true);
        gradeTable.setRowSelectionAllowed(true);
        gradeTable.setColumnSelectionAllowed(true);

        //South
        fixedTable = new JTable();

        CheckBoxTableModel defaultTableModel = new CheckBoxTableModel(1, gradeTable.getColumnCount());
        fixedTable.setModel(defaultTableModel);
        fixedTable.getTableHeader().setUI(null);

        fixedTable.setFillsViewportHeight(true);
        fixedTable.setRowSelectionAllowed(true);
        fixedTable.setColumnSelectionAllowed(true);

        JScrollPane fixedScrollPane = new JScrollPane(fixedTable);
        fixedScrollPane.setPreferredSize(new Dimension(windowWidth * (7 / 8), 20));

        JPanel middleSubPanel = new JPanel(new BorderLayout());
        middleSubPanel.add(weightingField, BorderLayout.NORTH);

        JScrollPane tableScrollPane = new JScrollPane(gradeTable);
        middleSubPanel.add(tableScrollPane, BorderLayout.CENTER);

        middleSubPanel.add(fixedScrollPane, BorderLayout.SOUTH);

        middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(middleSubPanel, BorderLayout.CENTER);
        middlePanel.setPreferredSize(new Dimension(windowWidth * (6 / 8), windowHeight));

        container.add(middlePanel, BorderLayout.CENTER);

        /*** Bottom Panel ***/
        //bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(new EmptyBorder(4, 16, 0, 16));
        bottomPanel.setPreferredSize(new Dimension(windowWidth, windowHeight / 8));

        //Bottom Left Panel
        File logo = new File("bu.bmp");
        ImagePanel bottomLeft = new ImagePanel(new ImageIcon("bu.png").getImage());
        try {
            Image image = ImageIO.read(logo);
            bottomLeft = new ImagePanel(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bottomLeft.setPreferredSize(new Dimension(windowWidth / 10, windowHeight / 8));

        //Bottom Center Panel
        JPanel bottomCenter = new JPanel();
        bottomCenter.setLayout(new BoxLayout(bottomCenter, BoxLayout.X_AXIS));
        bottomCenter.setBorder(new EmptyBorder(0, 8, 24, 8));

        switchPanel = new SwitchPanel(this, currentCourse);

        bottomCenter.add(switchPanel);

        bottomPanel.add(bottomLeft);
        bottomPanel.add(bottomCenter);

        container.add(bottomPanel, BorderLayout.SOUTH);

        /*** Settings of UI.MainFrame ***/
        setTitle("Grading System");
        setBounds((int)(screenSize.getWidth() - windowWidth)/ 2,
                (int)(screenSize.getHeight() - windowHeight)/ 2,
                windowWidth, windowHeight);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    //Save course and criterion information when closing
                    for (String courseName : courseMap.keySet()) {
                        for (String semester : courseMap.get(courseName).keySet()) {
                            Course course = courseMap.get(courseName).get(semester);
                            course.getCcriterion().writeToFile(criDir + courseName + "_" + semester);
                            course.writeToFile(fileDir + courseName + "_" + semester + ".txt");
                        }
                    }
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    //Constructor
    public MainFrame() {
        directory = new File(fileDir);
        if (!directory.exists())
            directory.mkdir();
        File cri = new File(criDir);
        if (!cri.exists())
            cri.mkdir();
        initialization();
        try {
            setUpTreeNodes();
        } catch (ClassCastException e) {

        }
    }

    /*** JTree Methods ***/
    private void setUpTreeNodes() {
        //Set up index tree nodes when initializing the UI.frame
        for (String s : directory.list()) {
            //System.out.println(s);   591_fall2019.txt
            Course readCourse = new Course();
            Course temp = readCourse.readFromFile(fileDir + s);
            if (temp != null)
                addCourse(temp);
        }
    }

    private TreePath find(DefaultMutableTreeNode root, String s) {
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = e.nextElement();
            if (node.toString().equalsIgnoreCase(s)) {
                return new TreePath(node.getPath());
            }
        }
        return null;
    }

    /*** Table Set-ups ***/
    //Method for outside calling
    public void update(Course course) {
        update(course, root);
    }

    private void update(Course course, DefaultMutableTreeNode root) {
        updatingFlag = true;
        String courseName = course.getInfo()[0];
        String courseSemester = course.getInfo()[2] + course.getInfo()[3];

        /*** Update Map ***/
        Map<String, Course> subCourseMap = courseMap.get(courseName);
        subCourseMap.put(courseSemester, course);
        courseMap.put(courseName, subCourseMap);

        /*** Update Table ***/
        setUpGradeTable(course.getCcriterion(), course.getList());

        /*** Update Components ***/
        currentCourse = course;
        switchPanel.refreshPanel(currentCourse);
        updatingFlag = false;
    }

    //Method for outside calling
    public void addCourse(Course course) {
        //Clear selection first
        //If index of the selected item does not exist in another table
        gradeTable.getSelectionModel().clearSelection();
        weightingField.setText("");
        addCourse(course, root);
    }

    private void addCourse(Course course, DefaultMutableTreeNode root) {
        String courseName = course.getInfo()[0];
        String courseSemester = course.getInfo()[2] + course.getInfo()[3];
        TreePath coursePath = find(root, courseName);
        TreePath semesterPath;

        HashMap<Student, Grade> gradeMap = course.getList();

        Map<String, Course> subCourseMap = new HashMap<>();
        if (!courseMap.containsKey(courseName)) {
            //Set up new table model
            setUpGradeTable(course.getCcriterion(), gradeMap);
            //Update Tree
            DefaultMutableTreeNode courseNode = new DefaultMutableTreeNode(courseName);
            DefaultMutableTreeNode semesterNode = new DefaultMutableTreeNode(courseSemester);
            courseNode.add(semesterNode);
            root.add(courseNode);
            //Update Map
            subCourseMap.put(courseSemester, course);
            courseMap.put(courseName, subCourseMap);
        } else if (courseMap.containsKey(courseName)) {
            if (courseMap.get(courseName).containsKey(courseSemester)) {
                //Do nothing because course of this semester already exists
            } else {
                //Set up new table model
                setUpGradeTable(course.getCcriterion(), gradeMap);
                //Update Tree
                DefaultMutableTreeNode courseNode = (DefaultMutableTreeNode) coursePath.getLastPathComponent();
                DefaultMutableTreeNode semesterNode = new DefaultMutableTreeNode(courseSemester);
                courseNode.add(semesterNode);
                //Update Map
                subCourseMap = courseMap.get(courseName);
                subCourseMap.put(courseSemester, course);
                courseMap.put(courseName, subCourseMap);
            }
        }

        currentCourse = course;
        switchPanel.refreshPanel(currentCourse);

        jTree.setModel(new DefaultTreeModel(root));
        //Expand the newest node
        semesterPath = find(root, courseSemester);
        jTree.setSelectionPath(semesterPath);
        jTree.scrollPathToVisible(semesterPath);
    }

    //Method for outside calling
    public void updateComment(String comment) {
        updateCellComment(comment);
    }

    private void updateCellComment(String comment) {
        int selectedColumn = gradeTable.getSelectedColumn();
        GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
        List<ColumnGroup> columnGroups = getColumnGroups(selectedColumn);

        String category = getCategory(selectedColumn, columnGroups, columnGroups.size() != 0);
        String stuId = gradeTable.getValueAt(gradeTable.getSelectedRow(), 1).toString();
        Student student = currentCourse.getStudent(stuId);

        if (columnGroups.size() == 0) {
            if (category.equals("Extra")) {
                currentCourse.getsGrade(student).getExtra().getNote().setNote(comment);
                saveCourseAndCriterion();
            } else {
                //Do nothing.
                //Cannot add comment to such columns
            }
        } else {
            int subCategoryId = getSubCategory(selectedColumn, true);

            Grade grade = currentCourse.getsGrade(student);

            grade.getCategory(header.findIndexOfGroup(category)).get(subCategoryId - 1).getNote().setNote(comment);

            currentCourse.getList().put(student, grade);
            saveCourseAndCriterion();
        }
    }

    private void setWeighting(int selectedColumn) {
        if (selectedColumn == -1)
            return;

        GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
        List<ColumnGroup> columnGroups = getColumnGroups(selectedColumn);

        if (columnGroups.size() != 0) {
            String category = columnGroups.get(0).getHeaderValue();
            weightingField.setText(currentCourse.getCcriterion().
                    getCategories().get(header.findIndexOfGroup(category)).toString());
        } else {
            if (selectedColumn == gradeTable.getColumnCount() - 2) {
                weightingField.setText(currentCourse.getCcriterion().toString());
            } else {
                weightingField.setText("");
            }
        }
    }

    public void updateCriterion(NewCriterion newCriterion) {
        currentCourse.setCcriterion(newCriterion);
        currentCourse.calculateAll();
        update(currentCourse);

        saveCourseAndCriterion();
    }

    private void setUpGradeTable(NewCriterion criterion, HashMap<Student, Grade> grade) {
        Vector<String> headers = setUpTableHeader(criterion);
        Vector<Object> grades = loadData(criterion.getCategories().size(), grade);

        setUpTypes(criterion);

        SortTableModel dm = (SortTableModel) gradeTable.getModel();
        dm.setClassList(Arrays.asList(type));
        dm.setDataVector(grades, headers);

        groupingHeaders(criterion);

        dm.addTableModelListener(new TableChangedListener());

        CheckBoxTableModel fixedTableModel = (CheckBoxTableModel) fixedTable.getModel();
        fixedTableModel.setColumnCount(gradeTable.getColumnCount());
        fixedTable.setTableHeader(null);
    }

    private Vector<String> setUpTableHeader(NewCriterion criterion) {
        String[] basic = new String[]{"Name", "Id", "Email", "Type"};

        Vector<String> vector = new Vector<>();
        vector.addAll(Arrays.asList(basic));

        for (int i = 0 ; i < criterion.getCategories().size() ; i++) {
            Category currentCategory = criterion.getCategories().get(i);
            for (int j = 0 ; j < currentCategory.getNumberOfTasks() ; j++) {
                vector.add(String.valueOf(j + 1));
            }
        }

        vector.add("Extra");
        vector.add("Total");
        vector.add("Withdraw");
        return vector;
    }

    private Vector<Object> loadData(int categoryNum, HashMap<Student, Grade> gradeMap) {
        Vector<Object> grades = new Vector<>();
        for (Student key : gradeMap.keySet()) {
            Vector<String> temp = new Vector<>();
            temp.add(key.getName().toString());
            temp.add(key.getId());
            temp.add(key.getEmail());
            temp.add(key.getType());
            Grade grade = gradeMap.get(key);
            for (int i = 0 ; i < categoryNum ; i++) {
                List<GradeComp> gradeComps = grade.getCategory(i);
                for (int j = 0 ; j < gradeComps.size() ; j++) {
                    temp.add(gradeComps.get(j).getScore());
                }
            }
            temp.add(grade.getExtra().getScore());      //Extra
            temp.add(String.valueOf(grade.getTtscore()));   //Total
            if (grade.ifWithDraw())
                temp.add("W");
            else
                temp.add("");
            grades.add(temp);
        }
        return grades;
    }

    //Types are for sorting
    private void setUpTypes(NewCriterion criterion) {
        Vector<Class> classes = new Vector<>();
        classes.add(String.class);
        classes.add(String.class);
        classes.add(String.class);
        classes.add(String.class);

        for (int i = 0 ; i < criterion.getCategories().size() ; i++) {
            Category currentCategory = criterion.getCategories().get(i);
            for (int j = 0 ; j < currentCategory.getNumberOfTasks() ; j++) {
                classes.add(String.class);
            }
        }

        classes.add(String.class);  //Extra Credit
        classes.add(double.class);  //Total
        classes.add(String.class);  //Withdraw
        type = classes.toArray(new Class[]{});
    }

    private void groupingHeaders(NewCriterion criterion) {
        TableColumnModel cm = gradeTable.getColumnModel();

        int startIndex = 4;
        GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
        header.getColumnGroups().clear();

        for (int i = 0 ; i < criterion.getCategories().size() ; i++) {
            Category currentCategory = criterion.getCategories().get(i);
            ColumnGroup categoryGroup = new ColumnGroup(currentCategory.getName());
            for (int j = 0 ; j < currentCategory.getNumberOfTasks() ; j++) {
                categoryGroup.add(cm.getColumn(startIndex));
                startIndex++;
            }
            header.addColumnGroup(categoryGroup);
        }
        gradeTable.setTableHeader(header);
    }

    /*** Fixed Table Methods ***/
    public List<Object> getCheckBoxSelections() {
        List<Object> selections = new ArrayList<>();
        for (int i = 0 ; i < fixedTable.getColumnCount() ; i++) {
            selections.add(fixedTable.getValueAt(0, i));
        }
        return selections;
    }

    public void clearCheckBoxSelection() {
        for (int i = 0 ; i < fixedTable.getColumnCount() ; i++) {
            fixedTable.setValueAt(null, 0, i);
        }
    }

    /*** Grade Table Methods ***/
    public JTable getGradeTable() {
        return gradeTable;
    }

    public String getSelectedStudentId() {
        return gradeTable.getValueAt(gradeTable.getSelectedRow(), 1).toString();
    }

    /**
     * Save changes of gradeTable to file.
     * @param id Id of the student whose information is changed.
     * @param category Which category is changed.
     * @param itemIndex Index of the changed item in the category. Already subtracted 1 when inputting.
     * @param value New value
     */
    private void saveTableChanges(String id, String category, int itemIndex, String value) {
        String courseName = currentCourse.getInfo()[0];
        String courseSemester = currentCourse.getInfo()[2] + currentCourse.getInfo()[3];

        GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();

        Student student = currentCourse.getStudent(id);
        Grade grade = currentCourse.getsGrade(student);

        switch (category) {
            case "Name":
                String[] name = value.split(" ");
                if (name.length == 2) {
                    student.setName(name[0], "", name[1]);
                } else if (name.length == 3) {
                    student.setName(name[0], name[1], name[2]);
                }
                break;
            case "Id":
                break;
            case "Email":
                student.setEmail(value);
                break;
            case "Extra":
                grade.setExtra(value);
                break;
            case "Withdraw":
                if (value.equals("W"))
                    grade.withDraw();
                break;
            default:
                if (checkCell(value)) {
                    for (int i = 0 ; i < currentCourse.getCcriterion().getCategories().size() ; i++) {
                        if (currentCourse.getCcriterion().getCategories().get(i).getName().equals(category)) {
                            int categoryIndex = header.findIndexOfGroup(category);
                            if (categoryIndex == -1) {
                                System.out.println("Category not found!");
                                break;
                            } else {
                                grade.getOne(categoryIndex, itemIndex).setScore(value);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Input must be either numbers or percentage!", "Info",JOptionPane.WARNING_MESSAGE);
                }
                break;
        }

        currentCourse.getList().put(student, grade);
        currentCourse.calculateAll();

        saveCourseAndCriterion();

        /*** Update Map ***/
        Map<String, Course> subCourseMap = courseMap.get(courseName);
        subCourseMap.put(courseSemester, currentCourse);
        courseMap.put(courseName, subCourseMap);

        update(currentCourse);
    }

    private String getCommentOfSelectedCell(int selectedRow, int selectedColumn) {
        //Get note
        String stuId = gradeTable.getValueAt(selectedRow, 1).toString();

        GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
        List<ColumnGroup> columnGroups = getColumnGroups(selectedColumn);

        String category = getCategory(selectedColumn, columnGroups, columnGroups.size() != 0);
        Student student = currentCourse.getStudent(stuId);

        if (columnGroups.size() != 0) {
            int subCategory = Integer.parseInt(gradeTable.getColumnName(selectedColumn));
            return currentCourse.getsGrade(student).
                    getCategory(header.findIndexOfGroup(category)).get(subCategory - 1).getNote().getNote();
        } else if (category.equals("Extra")) {
            return currentCourse.getsGrade(student).getExtra().getNote().getNote();
        }
        return "";
    }

    /*** Table Listener ***/
    //Display weighting in the textfield
    private class SelectionChangedListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (gradeTable.getSelectedRow() != -1)
                setWeighting(gradeTable.getSelectedColumn());
        }
    }

    //Listen to table value changed events
    private class TableChangedListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            int selectedColumn = gradeTable.getSelectedColumn();
            int selectedRow = gradeTable.getSelectedRow();
            if (selectedColumn == -1 || selectedRow == -1) {
                System.out.println("Nothing is selected!");
            } else if (updatingFlag) {
                //Ignore because table is already changing
            } else {
                //!!Only useful when student id is in the second column!!
                String stu_id = gradeTable.getValueAt(selectedRow, 1).toString();
                String value = gradeTable.getValueAt(selectedRow, selectedColumn).toString();

                List<ColumnGroup> columnGroups = getColumnGroups(selectedColumn);

                String category = getCategory(selectedColumn, columnGroups,
                        (columnGroups.size() != 0));
                int index = getSubCategory(selectedColumn,
                        (columnGroups.size() != 0));
                saveTableChanges(stu_id, category, index - 1, value);
            }
        }
    }

    //Listen to mouse clicks
    private class TableMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            int selectedColumn = gradeTable.columnAtPoint(event.getPoint());
            int selectedRow = gradeTable.rowAtPoint(event.getPoint());

            if (SwingUtilities.isRightMouseButton(event)) {
                gradeTable.changeSelection(selectedRow, selectedColumn, false, false);

                List<ColumnGroup> columnGroups = getColumnGroups(selectedColumn);

                String category = getCategory(selectedColumn, columnGroups, columnGroups.size() != 0);

                if (columnGroups.size() != 0 || category.equals("Extra")) {
                    createCommentFrame(selectedRow, selectedColumn);
                }
            } else {
                setWeighting(selectedColumn);
            }
        }
    }

    //Listens to header clicked events
    private class TableHeaderListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            if (event.getClickCount() == 2) {
                int selectedColumn = gradeTable.columnAtPoint(event.getPoint());
                //int selectedRow = gradeTable.rowAtPoint(event.getPoint());

                GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
                List<ColumnGroup> columnGroups = getColumnGroups(selectedColumn);

                String category = getCategory(selectedColumn, columnGroups, (columnGroups.size()!=0));
                if (columnGroups.size() != 0 ) {
                    int categoryIndex = header.findIndexOfGroup(category);
                    createSubCategoryFrame(categoryIndex);
                } else if (category.equals("Total")) {
                    createTotalCategoryFrame();
                }
            }
        }
    }

    /*** Other Methods ***/
    private List<ColumnGroup> getColumnGroups(int selectedColumn) {
        GroupableTableHeader header = (GroupableTableHeader) gradeTable.getTableHeader();
        TableColumn tc = gradeTable.getColumnModel().getColumn(selectedColumn);
        return header.getColumnGroups(tc);
    }

    private String getCategory(int selectedColumn, List<ColumnGroup> columnGroups, boolean headerGrouped) {
        if (headerGrouped) {
            return columnGroups.get(0).getHeaderValue();
        } else {
            return gradeTable.getColumnName(selectedColumn);
        }
    }

    private int getSubCategory(int selectedColumn, boolean headerGrouped) {
        if (headerGrouped)
            return Integer.parseInt(gradeTable.getColumnName(selectedColumn));
        else
            return -1;
    }

    private void createCommentFrame(int selectedRow, int selectedColumn) {
        new CommentFrame(this, getCommentOfSelectedCell(selectedRow, selectedColumn));
    }

    private void createSubCategoryFrame(int categoryIndex) {
        new SubCategory(this, currentCourse.getCcriterion(), categoryIndex);
    }

    private void createTotalCategoryFrame() {
        new TotalCategory(this, currentCourse.getCcriterion());
    }

    private void saveCourseAndCriterion() {
        currentCourse.getCcriterion().writeToFile(criDir + currentCourse.getInfo()[0] + "_" + currentCourse.getInfo()[2] + currentCourse.getInfo()[3]);
        currentCourse.writeToFile(fileDir + currentCourse.getInfo()[0] + "_" + currentCourse.getInfo()[2] + currentCourse.getInfo()[3] + ".txt");
    }

    /*** Checker ***/
    private boolean checkCell(String content){
        if (content == null || content.length() == 0) return false;
        if (checkNegative(content)) return true;
        else if (checkPresentage(content)) return true;
        else return false;
    }

    private boolean checkNegative(String content){
        String pattern = "^([-])?\\d+(\\.\\d+)?";

        if (content.charAt(0) == '-' && Pattern.matches(pattern,content))
            return true;

        return false;
    }

    private boolean checkPresentage(String content){
        String pattern = "^\\d+(\\.\\d+)?([%])?";

        if (Pattern.matches(pattern, content)){
            if (content.charAt(content.length()-1) == '%'){
                content = content.substring(0,content.length()-1);
                double val = Double.valueOf(content);
                if (val < 0 || val > 100.0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
