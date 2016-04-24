package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {

    public static final String DB_LOGIN = "root";
    public static final String DB_PASSWORD = "1111";
    public static final String DB_NAME = "lab2";

    public static Connection createConnection(String login, String password, String dbName) throws Exception {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, login, password);
    }

    public static Connection createConnection() throws Exception {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NAME, DB_LOGIN, DB_PASSWORD);
    }
}
