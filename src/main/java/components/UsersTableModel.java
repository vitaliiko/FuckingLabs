package components;

import model.SingleController;
import model.User;
import model.UsersRights;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class UsersTableModel implements TableModel {

    private SingleController controller;
    private ArrayList<User> usersList;

    public UsersTableModel(SingleController controller, ArrayList<User> usersList) {
        this.controller = controller;
        this.usersList = usersList;
    }

    private String[] items = {"Name", "Surname", "Username", "Account type"};

    @Override
    public int getRowCount() {
        return controller.getUserSet().size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return items[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return usersList.get(rowIndex).getFirstName();
            case 1:
                return usersList.get(rowIndex).getLastName();
            case 2:
                return usersList.get(rowIndex).getLogin();
            case 3:
                return UsersRights.accountType(usersList.get(rowIndex).getRights());
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
