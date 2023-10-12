package fxControllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMethods {

    public static Connection connectTotDB() throws ClassNotFoundException {
        Connection conn = null;
        String DBLink = "jdbc:mysql://localhost/trasportsystemdb";
        String user = "root";
        String password = "";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DBLink, user, password);
        }
        catch(SQLException | ClassNotFoundException throwables){
            throwables.printStackTrace();
        }
        return conn;
    }

    public static void disconnect(Connection connection, Statement statement){
        try{
            if(connection != null && statement != null) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
