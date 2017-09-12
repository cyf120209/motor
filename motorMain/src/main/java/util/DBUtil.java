package util;

import java.sql.*;

/**
 * Created by lenovo on 2017/6/15.
 */
public class DBUtil {

    private static Connection conn;
    static String driver="com.mysql.jdbc.Driver";
    static String url="jdbc:mysql://localhost:3306/motor";
    static String user="root";
    static String password="root";

    private DBUtil(){}
    static {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            if(!conn.isClosed()){
                System.out.println("Succeeded connecting to the Database!");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet query(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public static int excute(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        int num = ps.executeUpdate();
        if(ps!=null){
            ps.close();
        }
        return num;
    }

    public static int insert(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        int num = ps.executeUpdate();
        if(ps!=null){
            ps.close();
        }
        return num;
    }
}
