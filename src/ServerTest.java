import dao.UserDao;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by Yohann on 2016/8/11.
 */
public class ServerTest {
    @Test
    public void test() {
        try {
            String password = new UserDao().queryPassword("ya");
            System.out.println("password = " + password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
