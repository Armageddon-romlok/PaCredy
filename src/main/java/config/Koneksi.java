package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/pa_creedy";
    private static final String USER = "root";
    private static final String PASS = "";
    private static Connection conn;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Koneksi Database Gagal: " + e.getMessage());
        }
        return conn;
    }
}