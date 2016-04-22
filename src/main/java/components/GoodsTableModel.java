package components;

import db.Goods;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class GoodsTableModel implements TableModel {

    private List<Goods> goodsList;

    private String[] items = {"Тип", "Производитель", "Модель", "Цена", "Описание"};

    public GoodsTableModel(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public int getRowCount() {
        return goodsList.size();
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
        Goods goods = goodsList.get(rowIndex);
        switch (columnIndex) {
            case 1:
                return goods.getType();
            case 2:
                return goods.getProducer();
            case 3:
                return goods.getModel();
            case 4:
                return goods.getPrice();
            case 5:
                return goods.getDescription();
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
