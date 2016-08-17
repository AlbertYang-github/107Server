package dao;

import java.sql.*;
import java.util.Properties;

/**
 * 访问交通事件信息的数据接口
 * <p>
 * Created by Yohann on 2016/8/13.
 */
public class EventDao {

    //数据库连接对象
    private final Connection conn;

    /**
     * 构造方法, 连接MySQL数据库
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public EventDao() throws ClassNotFoundException, SQLException {
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
     * 插入一行路况
     *
     * @param startLocation
     * @param endLocation
     * @param startLongitude
     * @param endLongitude
     * @param startLatitude
     * @param endLatitude
     * @param eventLabels
     * @param eventTitle
     * @param eventDesc
     * @param startTime
     * @return 插入的最后一条记录的id
     */
    public int insertEvent(String startLocation,
                           String endLocation,
                           Double startLongitude,
                           Double endLongitude,
                           Double startLatitude,
                           Double endLatitude,
                           String eventLabels,
                           String eventTitle,
                           String eventDesc,
                           long startTime) {

        String sql = "INSERT INTO events (" +
                "start_location," +
                "end_location," +
                "start_longitude," +
                "end_longitude," +
                "start_latitude," +
                "end_latitude," +
                "event_labels," +
                "event_title," +
                "event_desc," +
                "start_time) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";

        int id = 0;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, startLocation);
            pstmt.setString(2, endLocation);
            pstmt.setDouble(3, startLongitude);
            pstmt.setDouble(4, endLongitude);
            pstmt.setDouble(5, startLatitude);
            pstmt.setDouble(6, endLatitude);
            pstmt.setString(7, eventLabels);
            pstmt.setString(8, eventTitle);
            pstmt.setString(9, eventDesc);
            pstmt.setTimestamp(10, new Timestamp(startTime));

            pstmt.executeUpdate();
            resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    /**
     * 添加二进制文件路径
     *
     * @param voicePath
     * @param picPath
     * @param videoPath
     * @return
     */
    public int addBinaryPath(String voicePath, String picPath, String videoPath, int id) {

        String sql = "UPDATE events SET voice_path = ?, picture_path = ?, video_path = ? WHERE id = ?";
        int rows = 0;
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, voicePath);
            pstmt.setString(2, picPath);
            pstmt.setString(3, videoPath);
            pstmt.setInt(4, id);
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
}
