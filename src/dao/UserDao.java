package dao;

import java.sql.*;
import java.util.Properties;

/**
 * 数据访问接口
 * <p>
 * 提供一系列访问数据库的方法
 * <p>
 * Created by Yohann on 2016/8/11.
 */
public class UserDao {

    //数据库连接对象
    private final Connection conn;

    /**
     * 构造方法, 连接MySQL数据库
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public UserDao() throws ClassNotFoundException, SQLException {
        //加载JDBC驱动
        Class.forName("com.mysql.jdbc.Driver");

        //准备数据库连接数据
        Properties info = new Properties();
        String url = "jdbc:mysql://localhost:3306/107db";
        info.put("user", "root");
        info.put("password", "1044510273yh");

        //获取连接对象
        conn = DriverManager.getConnection(url, info);
    }

    /**
     * 添加用户
     *
     * @param username
     * @param password
     * @return
     */
    public int insert(String username, String password) {

        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        PreparedStatement pstmt = null;
        int rows = 0;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rows;
    }

    /**
     * 根据用户名查询密码
     *
     * @param username
     * @return
     */
    public String query(String username) {
        String sql = "SELECT * FROM USER WHERE username = ?";
        PreparedStatement pstmt = null;
        String password = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                password = resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return password;
    }
}
