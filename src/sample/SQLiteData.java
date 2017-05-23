package sample;


import java.sql.*;

class SQLiteData {
    private static Connection conn = null;

    protected Connection connect() {

        try {
            // db parameters
            String url = "jdbc:sqlite:./src/Products.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

}
