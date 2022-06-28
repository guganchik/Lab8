package frame;

import anim.EatOperation;
import anim.MoveOperation;
import anim.StopOperation;
import app.Application;
import collections.User;
import collections.Vehicle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import monitor.MonitorPanel;

public class MainFrame {

    private final JFrame frame;
    
    private final JLabel userLabel;
    private final JLabel languageLabel;
    private final JLabel modeLabel;
    private final JLabel sortLabel;
    private final JLabel orderLabel;
    private final JLabel filterLabel;
    //private final JLabel valueLabel;
    private final JLabel visualizationLabel;
    private final JLabel propertiesLabel;
    
    private final JLabel userValue;
    
    
    private final JComboBox<String> langCombo;
    private DefaultComboBoxModel<String> langModel;

    private final JComboBox<String> modeCombo;
    private DefaultComboBoxModel<String> modeModel;
    
    private final JComboBox<String> sortCombo;
    private DefaultComboBoxModel<String> sortModel;

    private final JComboBox<String> orderCombo;
    private DefaultComboBoxModel<String> orderModel;
    
    //private final JComboBox<String> filterCombo;
    private DefaultComboBoxModel<String> filterModel;
    
    private final JTextField valueField;
    
    private JTable table;
    private VehicleTableModel tableModel;
    
    private TableRowSorter<TableModel> sorter;
    private int sortColumn;
    private SortOrder sortOrder = SortOrder.ASCENDING;
    
    private final JTextField[] propertyFields = new JTextField[Application.COLUMNS.length];
    
    MonitorPanel monitorPanel;
    
    JButton randomButton;
    JButton createButton;
    JButton saveButton;
    JButton deleteButton;
    
    boolean settingCBValue = false;
    
    JMenu menu1;
    JMenu menu2;
    JMenu menu3;
    
    JMenuItem mi11;
    JMenuItem mi12;
    
    JMenuItem mi21;
    JMenuItem mi22;
    JMenuItem mi23;
    JMenuItem mi24;
    
    JMenuItem mi31;
    JMenuItem mi32;

    public MainFrame() {
        Application application = Application.getInstance();
        
        // init models
        cbLangModel();
        cbModeModel();
        cbSortModel();
        cbOrderModel();
        //cbFilterModel();
        tableModel = new VehicleTableModel();
        
        // main frame
        frame = new JFrame(application.getLocalizedString("login.login"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800);
        frame.setResizable(false);
        
        JMenuBar mb = new JMenuBar();
        menu1 = new JMenu("menu.info");
        mi11 = new JMenuItem("command.info");
        mi11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                application.sendCommand("info", null, false);
            }
        });
        mi12 = new JMenuItem("command.exit");
        mi12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu1.add(mi11);
        menu1.add(mi12);
        mb.add(menu1);
        
        menu2 = new JMenu("menu.edit");
        mi21 = new JMenuItem("command.add_if_max");
        mi21.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String params[] = {"name", "x", "y", "engine","capacity","distance", "speed", "type"};
                new ParamsDialog(frame, application.getLocalizedString("command.add_if_max"), "add_if_max", params);
            }
        });
        mi22 = new JMenuItem("command.remove_greater");
        mi22.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String params[] = {"engine","capacity","distance"};
                new ParamsDialog(frame, application.getLocalizedString("command.remove_greater"), "remove_greater", params);
            }
        });
        mi23 = new JMenuItem("command.remove_lower");
        mi23.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String params[] = {"engine","capacity","distance"};
                new ParamsDialog(frame, application.getLocalizedString("command.remove_lower"), "remove_lower", params);
            }
        });
        mi24 = new JMenuItem("command.clear");
        mi24.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                application.sendCommand("clear", null, true);
            }
        });
        menu2.add(mi21);
        menu2.add(mi22);
        menu2.add(mi23);
        menu2.add(mi24);
        mb.add(menu2);
        
        menu3 = new JMenu("menu.filter");
        mi31 = new JMenuItem("command.max_by_id");
        mi31.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                application.sendCommand("max_by_id", null, false);
            }
        });
        mi32 = new JMenuItem("command.show");
        mi32.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                application.sendCommand("get_collection", null, true);
            }
        });
        menu3.add(mi31);
        menu3.add(mi32);
        mb.add(menu3);
        
        frame.setJMenuBar(mb);

        JPanel pane;
        JPanel pane1;
        JPanel pane2;
        Font font = new Font("Tahoma", 0, 14);
        
        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        /*
         * 
         * User & Language & Mode inputs
         * 
         */
        
        pane1 = new JPanel();
        pane1.setLayout(new BorderLayout());
        pane1.setBorder(new EmptyBorder(10, 10, 10, 10));

        /** user **/
        pane2 = new JPanel();
        pane2.setLayout(new FlowLayout());
        
        userLabel = new JLabel("main.user");
        userLabel.setFont(font);
        pane2.add(userLabel);
        
        userValue = new JLabel("null");
        userValue.setFont(font);
        pane2.add(userValue);
        
        pane1.add(pane2, BorderLayout.WEST);
        
        /** language **/
        pane2 = new JPanel();
        pane2.setLayout(new FlowLayout());
        
        languageLabel = new JLabel("main.language");
        languageLabel.setFont(font);
        pane2.add(languageLabel);
        
        langCombo = new JComboBox<String>(langModel);
        langCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = langModel.getIndexOf(langCombo.getSelectedItem()); 
                application.setLang(idx);
                localize();
                langCombo.setSelectedIndex(idx);
            }
        });
        pane2.add(langCombo);
        
        /** mode **/
        modeLabel = new JLabel("main.mode");
        modeLabel.setFont(font);
        pane2.add(modeLabel);

        modeCombo = new JComboBox<String>(modeModel);
        modeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isHoleMode()) {
                    monitorPanel.checkHole();
                }
            }
        });
        pane2.add(modeCombo);
        
        pane1.add(pane2, BorderLayout.EAST);
        
        pane.add(pane1);
        
        /*
         * 
         * Table controls
         * 
         */
        
        pane1 = new JPanel();
        pane1.setLayout(new BorderLayout());
        pane1.setBorder(new EmptyBorder(0, 10, 0, 10));

        /** Sort control **/
        pane2 = new JPanel();
        pane2.setLayout(new FlowLayout());
        
        sortLabel = new JLabel("main.sort");
        sortLabel.setFont(font);
        pane2.add(sortLabel);
        
        sortCombo = new JComboBox<String>(sortModel);
        sortCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortColumn = sortModel.getIndexOf(sortCombo.getSelectedItem());
                if (!settingCBValue) {
                    sortTable();
                }
            }
        });
        pane2.add(sortCombo);
        
        orderLabel = new JLabel("main.order");
        orderLabel.setFont(font);
        pane2.add(orderLabel);
        
        orderCombo = new JComboBox<String>(orderModel);
        orderCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = orderModel.getIndexOf(orderCombo.getSelectedItem()); 
                if (idx == 0) {
                    sortOrder = SortOrder.ASCENDING;
                } else {
                    sortOrder = SortOrder.DESCENDING;
                }
                if (!settingCBValue) {
                    sortTable();
                }
            }
        });
        pane2.add(orderCombo);
        
        pane1.add(pane2, BorderLayout.WEST);
        
        /** Filter control **/
        pane2 = new JPanel();
        pane2.setLayout(new FlowLayout());
        
        filterLabel = new JLabel("main.filter");
        filterLabel.setFont(font);
        pane2.add(filterLabel);

        /**
        filterCombo = new JComboBox<String>(filterModel);
        filterCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        pane2.add(filterCombo);

        valueLabel = new JLabel("main.value");
        valueLabel.setFont(font);
        pane2.add(valueLabel);
        */
        
        valueField = new JTextField(15);
        valueField.setFont(font);
        valueField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(valueField.getText());
                filterTable();
            }
        });        
        pane2.add(valueField);
        
        pane1.add(pane2, BorderLayout.EAST);
        
        pane.add(pane1);
        
        /*
         * 
         * Table
         * 
         */
        
        pane1 = new JPanel();
        pane1.setLayout(new BorderLayout());
        pane1.setBorder(new EmptyBorder(0, 10, 0, 10));

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow()>=0 && table.getSelectedRow()<tableModel.getRowCount()) {
                    monitorPanel.setSelected((Integer)table.getValueAt(table.getSelectedRow(), 0));
                    for (int i=0; i<Application.COLUMNS.length; i++) {
                        propertyFields[i].setText(table.getValueAt(table.getSelectedRow(), i).toString());
                    }
                }
            }
        });       
        sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);
        sorter.addRowSorterListener( new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (RowSorterEvent.Type.SORT_ORDER_CHANGED == e.getType()) {
                    List<? extends SortKey> sortKeys = table.getRowSorter().getSortKeys();
                    int i=0;
                    for(SortKey key : sortKeys){
                        if (i==0) {
                            settingCBValue = true;
                            sortCombo.setSelectedIndex(key.getColumn());
                            orderCombo.setSelectedIndex((key.getSortOrder() == SortOrder.ASCENDING)?0:1);
                            settingCBValue = false;
                        }
                        i++;
                    }                    
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        scrollPane.setPreferredSize(new Dimension(1240, 350));
        pane1.add(scrollPane);
        
        pane.add(pane1);

        /*
         * 
         * Visualization & properties
         * 
         */
        
        pane1 = new JPanel();
        pane1.setLayout(new GridLayout(1,2));
        pane1.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        visualizationLabel = new JLabel("main.graphics");
        visualizationLabel.setFont(font);
        visualizationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pane1.add(visualizationLabel);
        
        propertiesLabel = new JLabel("main.properties");
        propertiesLabel.setFont(font);
        propertiesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pane1.add(propertiesLabel);
        
        pane.add(pane1);

        
        pane1 = new JPanel();
        pane1.setLayout(new GridLayout(1,2));
        pane1.setBorder(new EmptyBorder(0, 10, 0, 10));
        
        pane2 = new JPanel();
        pane2.setLayout(new GridLayout(1,1));
        monitorPanel = new MonitorPanel();
        monitorPanel.init(this);
        monitorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        pane2.add(monitorPanel);
        pane1.add(pane2);
        
        pane2 = new JPanel();
        pane2.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel label;
        int y = 0;
        for (String column : Application.COLUMNS) {
            label = new JLabel(column);
            label.setFont(font);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            //label.setBorder(new EmptyBorder(0, 0, 0, 5));
            constraints.insets = new Insets(5,10,0,0); 
            constraints.gridx = 0;
            constraints.gridy = y;
            constraints.weightx = 1;
            pane2.add(label, constraints);

            propertyFields[y] = new JTextField();
            propertyFields[y].setFont(font);
            constraints.gridx = 1;
            constraints.gridy = y;
            constraints.weightx = 10;
            pane2.add(propertyFields[y], constraints);
            
            y++;
        }
        propertyFields[0].setEnabled(false);
        propertyFields[8].setEnabled(false);
        propertyFields[10].setEnabled(false);
        
        
        JPanel pane3 = new JPanel();
        pane3.setLayout(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.fill = GridBagConstraints.HORIZONTAL;
        constraints1.gridy = 0;
        constraints1.insets = new Insets(0,15,0,0);    

        randomButton = new JButton("main.random");
        randomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                if (Application.getInstance().saveVehicle(
                        "",
                        "object_" + random.nextInt(), 
                        "" + (random.nextFloat()*40-20), 
                        "" + (random.nextFloat()*40-20), 
                        "" + (random.nextFloat()*100), 
                        "" + (long)(5+random.nextFloat()*15), 
                        "" + random.nextDouble()*100, 
                        "" + (5+random.nextFloat()*30), 
                        "SHIP",
                        ""
                )) {
                    clearPropertyFields();
                    table.getSelectionModel().clearSelection();
                }
            }
        });
        randomButton.setFont(font);
        randomButton.setPreferredSize(new Dimension(140,40));
        constraints1.gridx = 0;
        pane3.add(randomButton, constraints1);

        
        createButton = new JButton("main.create");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearPropertyFields();
                table.getSelectionModel().clearSelection();
            }
        });
        createButton.setFont(font);
        createButton.setPreferredSize(new Dimension(140,40));
        constraints1.gridx = 1;
        pane3.add(createButton, constraints1);
        
        saveButton = new JButton("main.save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Application.getInstance().saveVehicle(
                        propertyFields[0].getText(),
                        propertyFields[1].getText(), 
                        propertyFields[2].getText(), 
                        propertyFields[3].getText(), 
                        propertyFields[4].getText(), 
                        propertyFields[5].getText(), 
                        propertyFields[6].getText(), 
                        propertyFields[7].getText(), 
                        propertyFields[9].getText(),
                        propertyFields[10].getText()
                        
                )) {
                    clearPropertyFields();
                    table.getSelectionModel().clearSelection();
                }
            }
        });
        saveButton.setFont(font);
        saveButton.setPreferredSize(new Dimension(140,40));
        constraints1.gridx = 2;
        pane3.add(saveButton, constraints1);
        
        deleteButton = new JButton("main.delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Application.getInstance().deleteVehicle(
                        propertyFields[0].getText(),
                        propertyFields[10].getText()
                )) {
                    clearPropertyFields();
                    table.getSelectionModel().clearSelection();
                }
            }
        });
        deleteButton.setFont(font);
        deleteButton.setPreferredSize(new Dimension(140,40));
        constraints1.gridx = 3;
        pane3.add(deleteButton, constraints1);
        
        constraints.insets = new Insets(10,0,0,0); 
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.gridwidth = 2;
        pane2.add(pane3, constraints);
        
        pane1.add(pane2);
        
        pane.add(pane1);

        pane1 = new JPanel();
        pane.add(pane1);
        
        
        frame.add(pane, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.pack();
        localize();
    }

    private void clearPropertyFields() {
        for (int i=0; i<Application.COLUMNS.length; i++) {
            propertyFields[i].setText("");
        }
    }
    
    
    public void cbLangModel() {
        langModel = new DefaultComboBoxModel();
        for (String lang : Application.LANGS) {
            langModel.addElement(Application.getInstance().getLocalizedString(lang));
        }
    }
    
    public void cbModeModel() {
        modeModel = new DefaultComboBoxModel();
        modeModel.addElement(Application.getInstance().getLocalizedString("main.mode_0"));
        modeModel.addElement(Application.getInstance().getLocalizedString("main.mode_1"));
    }

    public void cbSortModel() {
        sortModel = new DefaultComboBoxModel();
        for (String column : Application.COLUMNS) {
            sortModel.addElement(column);
        }
    }

    public void cbOrderModel() {
        orderModel = new DefaultComboBoxModel();
        orderModel.addElement(Application.getInstance().getLocalizedString("main.order_asc"));
        orderModel.addElement(Application.getInstance().getLocalizedString("main.order_desc"));
    }
    
    public void cbFilterModel() {
        filterModel = new DefaultComboBoxModel();
        for (String column : Application.COLUMNS) {
            filterModel.addElement(column);
        }
    }

    public void show() {
        frame.setVisible(true);
    }
    
    public void hide() {
        frame.setVisible(false);
    }

    public JFrame getFrame() {
        return frame;
    }
    
    
    
    public void updateUser(User user) {
        userValue.setText((user!=null)?user.getLogin():"");
    }
    
    public void updateCollection(TreeSet<Vehicle> collection) {
        tableModel.setData(collection);
        tableModel.fireTableDataChanged();
        monitorPanel.setCollection(collection);
        if (isHoleMode()) {
            monitorPanel.checkHole();
        }
    }
    
    public boolean isHoleMode() {
        return modeModel.getIndexOf(modeCombo.getSelectedItem()) == 1; 
    }
    
    public void animateMove(MoveOperation moveOperation) {
        monitorPanel.animateMove(moveOperation);
        tableModel.update(moveOperation.getVehicleId(), (float)moveOperation.getStartPoint().getX(), (float)moveOperation.getStartPoint().getY());
        //System.out.println("table.getSelectedRow(): " + table.getSelectedRow());
        int selectedId = 0;
        if (table.getSelectedRow()>=0 && table.getSelectedRow()<tableModel.getRowCount()) {
            selectedId = (Integer)table.getValueAt(table.getSelectedRow(), 0);
        }        
        tableModel.fireTableDataChanged();
        selectRow(selectedId);
    }
    
    public void animateStop(StopOperation stopOperation) {
        monitorPanel.animateStop(stopOperation);
        tableModel.update(stopOperation.getVehicleId(), (float)stopOperation.getTargetPoint().getX(), (float)stopOperation.getTargetPoint().getY());
        //System.out.println("table.getSelectedRow(): " + table.getSelectedRow());
        int selectedId = 0;
        if (table.getSelectedRow()>=0 && table.getSelectedRow()<tableModel.getRowCount()) {
            selectedId = (Integer)table.getValueAt(table.getSelectedRow(), 0);
        }        
        tableModel.fireTableDataChanged();
        selectRow(selectedId);
    }
    

    public void animateEat(EatOperation eatOperation) {
        monitorPanel.animateEat(eatOperation);
        int selectedId = 0;
        if (table.getSelectedRow()>=0 && table.getSelectedRow()<tableModel.getRowCount()) {
            selectedId = (Integer)table.getValueAt(table.getSelectedRow(), 0);
        }        
        if (eatOperation.getVehicle1Capacity() == 0) {
            tableModel.deleteRow(eatOperation.getVehicle1Id());
        } else {
            tableModel.update(eatOperation.getVehicle1Id(), eatOperation.getVehicle1Capacity());
        }
        if (eatOperation.getVehicle2Capacity() == 0) {
            tableModel.deleteRow(eatOperation.getVehicle2Id());
        }
        selectRow(selectedId);
    }

    
    public void sortTable() {
        List<SortKey> sortKeys = new ArrayList<SortKey>();
        sortKeys.add(new RowSorter.SortKey(sortColumn, sortOrder));
        sorter.setSortKeys(sortKeys);        
    }
    
    public void filterTable() {
        /**
        RowFilter<VehicleTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(valueField.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);*/
        if(valueField.getText().trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter(valueField.getText().trim()));
            } catch(PatternSyntaxException pse) {
                System.out.println("Bad regex pattern");
            }
        }        
    }
    
    public void selectRow(int id) {
        //Object[] rowData = new Object [jTable1.getRowCount()];
        for (int i = 0; i < table.getRowCount(); i++) { 
            if ((Integer)table.getValueAt(i, 0) == id) {
                table.clearSelection();
                table.addRowSelectionInterval(i, i);
            }
            //System.out.println(table.getValueAt(i, 0));
        }
    }
    
    public void localize() {
        
        Application application = Application.getInstance();
        
        frame.setTitle(application.getLocalizedString("main.title"));
        
        userLabel.setText(application.getLocalizedString("main.user"));
        languageLabel.setText(application.getLocalizedString("main.language"));
        modeLabel.setText(application.getLocalizedString("main.mode"));
        sortLabel.setText(application.getLocalizedString("main.sort"));
        orderLabel.setText(application.getLocalizedString("main.order"));
        filterLabel.setText(application.getLocalizedString("main.filter"));
        //valueLabel.setText(application.getLocalizedString("main.value"));
        visualizationLabel.setText(application.getLocalizedString("main.graphics"));
        propertiesLabel.setText(application.getLocalizedString("main.properties"));

        randomButton.setText(application.getLocalizedString("main.random"));
        createButton.setText(application.getLocalizedString("main.create"));
        saveButton.setText(application.getLocalizedString("main.save"));
        deleteButton.setText(application.getLocalizedString("main.delete"));

        menu1.setText(application.getLocalizedString("menu.info"));
        mi11.setText(application.getLocalizedString("command.info"));
        mi12.setText(application.getLocalizedString("command.exit"));
                
        menu2.setText(application.getLocalizedString("menu.edit"));
        mi21.setText(application.getLocalizedString("command.add_if_max"));
        mi22.setText(application.getLocalizedString("command.remove_greater"));
        mi23.setText(application.getLocalizedString("command.remove_lower"));
        mi24.setText(application.getLocalizedString("command.clear"));
        
        menu3.setText(application.getLocalizedString("menu.filter"));
        mi31.setText(application.getLocalizedString("command.max_by_id"));
        mi32.setText(application.getLocalizedString("command.show"));
        
        cbLangModel();
        langCombo.setModel(langModel);
        
        cbModeModel();
        modeCombo.setModel(modeModel);
        
        cbOrderModel();
        orderCombo.setModel(orderModel);
        
        /**
        cbFilterModel();
        filterCombo.setModel(filterModel);
        */
        
    }
    
    
    
    
    
    
    
}
