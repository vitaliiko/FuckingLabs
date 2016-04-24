package components;

import model.SingleController;
import model.User;
import model.UserRights;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class UsersTableModel implements TableModel {

    private SingleController controller;
    private List<User> usersList;
    private String[] items = {"Имя", "Фамилия", "Логин", "Тип учетной записи"};

    public UsersTableModel(SingleController controller, ArrayList<User> usersList) {
        this.controller = controller;
        this.usersList = usersList;
    }

    @Override
    public int getRowCount() {
        return controller.getUserSet().size();
    }

    @Override
    public int getColumnCount() {
        return items.length;
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
        User user = usersList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getFirstName();
            case 1:
                return user.getLastName();
            case 2:
                return user.getLogin();
            case 3:
                return UserRights.accountType(user.getRights());
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
