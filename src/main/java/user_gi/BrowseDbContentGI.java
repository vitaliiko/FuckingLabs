package user_gi;

import components.BoxPanel;
import components.GoodsTableModel;
import components.LabelComponentPanel;
import db.Goods;
import db.GoodsService;
import db.GoodsServiceImpl;
import frame_utils.FrameUtils;
import model.User;
import model.UsersRights;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;

public class BrowseDbContentGI extends JFrame {

    private GoodsService goodsService = new GoodsServiceImpl();
    private User user;

    private JTabbedPane tabbedPane;
    private JPanel userInfoPanel;
    private JButton infoButton;

    public BrowseDbContentGI(User user) throws HeadlessException {
        super("Лабораторна робота №2");
        this.user = user;
        FrameUtils.setLookAndFeel();

        prepareTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(424, 200));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(5, 5, 0, 5));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFocusable(false);

        prepareUserInfoPanel();
        tabbedPane.addTab("Информация", userInfoPanel);

        List<Goods> goodsList = goodsService.list();
        tabbedPane.addTab("Табл. 1", createGoodsTable(goodsList));
        if (user.getRights() == UsersRights.SIMPLE_USER) {
            tabbedPane.addTab("Табл. 2", createGoodsTable(goodsList));
        }
        if (user.getRights() == UsersRights.ADMIN) {
            for (int i = 3; i < 8; i++) {
                tabbedPane.addTab("Табл. " + i, createGoodsTable(goodsList));
            }
        }
    }

    private JTable createGoodsTable(List<Goods> goodsList) {
        TableModel tableModel = new GoodsTableModel(goodsList);
        JTable goodsTable = new JTable(tableModel);
        goodsTable.getTableHeader().setReorderingAllowed(false);
        goodsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return goodsTable;
    }

    private void prepareUserInfoPanel() {
        userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout());
        JPanel panel = new BoxPanel(BoxLayout.Y_AXIS);

        panel.add(new LabelComponentPanel("Логин: ", user.getLogin()));
        panel.add(new LabelComponentPanel("Тип учетной записи: ", UsersRights.accountType(user.getRights())));
        panel.add(new LabelComponentPanel("Телефон: ", user.getTelephoneNum()));
        panel.add(new LabelComponentPanel("E-mail: ", user.getMailAddress()));

        userInfoPanel.add(panel, BorderLayout.NORTH);
        if (user.getRights() == UsersRights.ADMIN) {
            prepareInfoButton();
            userInfoPanel.add(new BoxPanel(infoButton), BorderLayout.SOUTH);

        }
    }

    private void prepareInfoButton() {
        infoButton = new JButton("Пользователи");
        infoButton.addActionListener(e -> new UsersInfoGI());}

}
