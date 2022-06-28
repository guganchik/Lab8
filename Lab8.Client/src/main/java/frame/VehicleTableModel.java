package frame;

import app.Application;
import collections.Vehicle;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

public class VehicleTableModel extends AbstractTableModel/** implements TableModelListener*/ {
    
    // TableModel's column names
    private final ArrayList<String> columnNames = new ArrayList(Application.COLUMNS.length);

    // TableModel's data
    private ArrayList<Object[]> data = new ArrayList();

    public VehicleTableModel() {
        for(String column: Application.COLUMNS) {
            columnNames.add(column);
        }
    }
    
    public void setData(Set<Vehicle> collection) {
        data.clear();
        Object[] row;
        for(Vehicle vehicle: collection) {
            row = new Object[Application.COLUMNS.length];
            row[0] = vehicle.getId();
            row[1] = vehicle.getName();
            row[2] = vehicle.getCoordinates().getX();
            row[3] = vehicle.getCoordinates().getY();
            row[4] = vehicle.getEnginePower();
            row[5] = vehicle.getCapacity();
            row[6] = vehicle.getDistanceTravelled();
            row[7] = vehicle.getSpeed();
            row[8] = Application.getInstance().getDateFormat().format(vehicle.getCreationDate());
            row[9] = vehicle.getTypeAsString();
            row[10] = vehicle.getOwner();
            data.add(row);
        }
    }
    
    /**
     * Returns the number of rows in the table model.
     */
    public int getRowCount() {
        return data.size();
    }

    /**
     * Returns the number of columns in the table model.
     */
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Returns the column name for the column index.
     */
    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    /**
     * Returns data type of the column specified by its index.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    /**
     * Returns the value of a table model at the specified row index and column
     * index.
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return data.get(rowIndex)[columnIndex];
        } catch (Exception e) {
            return new Object();
        }
    }
    
    public void update(int id, float x, float y) {
        for (int i=0; i<data.size(); i++) {
            Object[] row = data.get(i);
            if ((int)row[0] == id) {
                row[2] = x;
                row[3] = y;
            }
        }
    }

    public void update(int id, long capacity) {
        for (int i=0; i<data.size(); i++) {
            Object[] row = data.get(i);
            if ((int)row[0] == id) {
                row[5] = capacity;
            }
        }
    }
    
    public void deleteRow(int id) {
        int index = -1;
        for (int i=0; i<data.size(); i++) {
            Object[] row = data.get(i);
            if ((int)row[0] == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            data.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }
}
    
    
/**    
  protected TableModel base;

  protected int sortColumn;

  protected int[] row;

  public SampleSortingTableModel(TableModel tm, int sortColumn) {
    this.base = tm;
    this.sortColumn = sortColumn;
    tm.addTableModelListener(this);
    rebuild();
  }

  public Class getColumnClass(int c) {
    return base.getColumnClass(c);
  }

  public int getColumnCount() {
    return base.getColumnCount();
  }

  public String getColumnName(int c) {
    return base.getColumnName(c);
  }

  public int getRowCount() {
    return base.getRowCount();
  }

  public Object getValueAt(int r, int c) {
    return base.getValueAt(row[r], c);
  }

  public boolean isCellEditable(int r, int c) {
    return base.isCellEditable(row[r], c);
  }

  public void setValueAt(Object value, int r, int c) {
    base.setValueAt(value, row[r], c); // Notification will cause re-sort
  }

  public void tableChanged(TableModelEvent event) {
    rebuild();
  }

  protected void rebuild() {
    int size = base.getRowCount();
    row = new int[size];
    for (int i = 0; i < size; i++) {
      row[i] = i;
    }
    sort();
  }

  protected void sort() { // Sort and notify listeners
    for (int i = 1; i < row.length; i++) {
      int j = i;
      while (j > 0 && compare(j - 1, j) > 0) {
        int temp = row[j];
        row[j] = row[j - 1];
        row[j - 1] = temp;
        j--;
      }
    }
    fireTableStructureChanged();
  }

  protected int compare(int i, int j) {
    String s1 = base.getValueAt(row[i], sortColumn).toString();
    String s2 = base.getValueAt(row[j], sortColumn).toString();
    return s1.compareTo(s2);
  }
  */
