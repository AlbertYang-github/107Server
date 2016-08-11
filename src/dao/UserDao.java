package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            rows =  pstmt.executeUpdate();
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
}
