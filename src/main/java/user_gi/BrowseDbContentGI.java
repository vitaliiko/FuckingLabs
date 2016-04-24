package user_gi;

import components.BoxPanel;
import components.GoodsTableModel;
import components.LabelComponentPanel;
import db.Goods;
import db.GoodsService;
import db.GoodsServiceImpl;
import frame_utils.FrameUtil;
import model.User;
import model.UsersRights;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class BrowseDbContentGI extends JFrame {

    private GoodsService goodsService = new GoodsServiceImpl();
    private User user;

    private JTabbedPane tabbedPane;
    private JPanel userInfoPanel;
    private JButton infoButton;
    private JButton mainMenuButton;

    public BrowseDbContentGI(User user) throws HeadlessException {
        super("Лабораторна робота №2");
        this.user = user;
        FrameUtil.setLookAndFeel();

        prepareTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(424, 200));
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new MainWindowGI();
            }
        });
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

        BoxPanel buttonsPanel = new BoxPanel();
        userInfoPanel.add(panel, BorderLayout.NORTH);
        if (user.getRights() == UsersRights.ADMIN) {
            prepareInfoButton();
            buttonsPanel.add(infoButton);
        }
        prepareMainMenuButton();
        buttonsPanel.add(mainMenuButton);
        userInfoPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void prepareInfoButton() {
        infoButton = new JButton("Пользователи");
        infoButton.addActionListener(e -> new UsersInfoGI());
    }

    private void prepareMainMenuButton() {
        mainMenuButton = new JButton("Меню", new ImageIcon("resources/menu.png"));
        mainMenuButton.addActionListener(e -> {
            new MainWindowGI();
            this.dispose();
        });
    }
}
