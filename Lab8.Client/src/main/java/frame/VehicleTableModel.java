package frame;

import app.Application;
import collections.Vehicle;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.Date;
import java.util.TreeSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class VehicleTableModel extends AbstractTableModel/** implements TableModelListener*/ {
    
    // TableModel's column names
    private final String[] columnNames = Application.COLUMNS;

    // TableModel's data
    private Object[][] data = new Object[0][Application.COLUMNS.length];

    public void setData(Set<Vehicle> collection) {
        data = new Object[collection.size()][Application.COLUMNS.length];
        int i = 0;
        for(Vehicle vehicle: collection) {
            data[i][0] = vehicle.getId();
            data[i][1] = vehicle.getName();
            data[i][2] = vehicle.getCoordinates().getX();
            data[i][3] = vehicle.getCoordinates().getY();
            data[i][4] = vehicle.getEnginePower();
            data[i][5] = vehicle.getCapacity();
            data[i][6] = vehicle.getDistanceTravelled();
            data[i][7] = vehicle.getSpeed();
            data[i][8] = Application.getInstance().getDateFormat().format(vehicle.getCreationDate());
            data[i][9] = vehicle.getTypeAsString();
            data[i][10] = vehicle.getOwner();
            i++;
        }
    }
    
    /**
     * Returns the number of rows in the table model.
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * Returns the number of columns in the table model.
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the column name for the column index.
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
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
            return data[rowIndex][columnIndex];
        } catch (Exception e) {
            return new Object();
        }
    }
    
    public void update(int id, float x, float y) {
        for (int i=0; i<data.length; i++) {
            if ((int)data[i][0] == id) {
                data[i][2] = x;
                data[i][3] = y;
            }
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
