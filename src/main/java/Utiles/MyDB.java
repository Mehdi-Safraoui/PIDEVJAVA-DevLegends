package Utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {
    String url = "jdbc:mysql://localhost:3306/innerbloom";
    String user = "root";
    String password = "";
    private Connection con;
    private static MyDB instance;

    private MyDB() {
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static MyDB getInstance() {
        if (instance == null) {
            instance = new MyDB();
        }
        return instance;
    }

    public Connection getCon() {
        return con;
    }
}