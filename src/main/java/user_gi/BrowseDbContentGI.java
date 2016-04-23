package user_gi;

import components.GoodsTableModel;
import db.Goods;
import db.GoodsService;
import db.GoodsServiceImpl;
import frame_utils.FrameUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;

public class BrowseDbContentGI extends JFrame {

    private GoodsService goodsService = new GoodsServiceImpl();

    private JTabbedPane tabbedPane;

    public BrowseDbContentGI() throws HeadlessException {
        super("Лабораторна робота №2");
        FrameUtils.setLookAndFeel();

        prepateTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(924, 620));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepateTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(5, 5, 0, 5));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFocusable(false);

        List<Goods> goodsList = goodsService.list();
        for (int i = 1; i < 8; i++) {
            tabbedPane.addTab("Табл. " + i, createGoodsTable(goodsList));
        }
    }

    private JTable createGoodsTable(List<Goods> goodsList) {
        TableModel tableModel = new GoodsTableModel(goodsList);
        JTable goodsTable = new JTable(tableModel);
        goodsTable.getTableHeader().setReorderingAllowed(false);
        goodsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return goodsTable;
    }
}
