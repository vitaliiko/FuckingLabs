package components;

import model.Controller;
import model.User;
import model.UsersRights;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class UsersTableModel implements TableModel {

    private Controller controller;
    private ArrayList<User> usersList;

    public UsersTableModel(Controller controller, ArrayList<User> usersList) {
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
                return usersList.get(rowIndex).getName();
            case 1:
                return usersList.get(rowIndex).getSurname();
            case 2:
                return usersList.get(rowIndex).getUserName();
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
