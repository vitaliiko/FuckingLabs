//package db;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Vector;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//
//public class DBTable {
//
//    public static void main(String[] args) {
//        String databaseURL = "jdbc:mysql://localhost:3306/tehnika";
//        String user = "root";
//        String password = "1111";
//        String driverName = "com.mysql.jdbc.Driver";
//        JFrame f = new JFrame("DataBaseTable");
//        final JDBCAdapter dbAdapter = new JDBCAdapter(databaseURL, driverName, user,password);
//        final JDBCAdapter dbAdapter1 = new JDBCAdapter(databaseURL,	driverName, user,password);
//        dbAdapter.executeQuery("select * from info");
//        dbAdapter1.executeQuery("select * from product");
//
//        final JTable table = new JTable(dbAdapter);
//        final JTable table1 = new JTable(dbAdapter1);
//        GridLayout grid = new GridLayout(2,3);
//        JScrollPane scp = new JScrollPane(table);
//        JScrollPane scp1 = new JScrollPane(table1);
//        scp.getViewport().add(table, null);
//        scp1.getViewport().add(table1, null);
//        f.add(scp);
//        f.add(scp1);
//        f.setLayout(grid);
//
//        JPanel panel = new JPanel();
//        JButton btn1 = new JButton("Удалить");
//        btn1.addActionListener(e -> {
//            int curRow=table.getSelectedRow();
//            dbAdapter.deleteRow(curRow);
//            dbAdapter1.deleteRow(curRow);
//            table.repaint();
//        });
//
//        JButton btn2=new JButton("Вставить");
//        f.getContentPane().setVisible(false);
//        f.getContentPane().setVisible(true);
//
//        btn2.addActionListener(e -> {
//            Vector<String> dats = new Vector<>();
//            dats.add("1"); // номер раз
//            dats.add("a"); // номер два
//            dats.add("a");
//            dats.add("1");
//            dats.add("1");// номер Х, где Х == число колонок
//            dbAdapter.insertRow(dats);
//            dbAdapter1.insertRow(dats);
//            table.repaint();
//        });
//
//
//        panel.add(btn1);
//        panel.add(btn2);
//        f.getContentPane().add(panel,BorderLayout.CENTER);
//        f.setVisible(true);
//        f.pack();
//        f.setSize(new Dimension(1000, 300));
//        f.show();
//
//    }
//}
