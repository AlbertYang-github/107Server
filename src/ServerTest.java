import dao.UserDao;
import loginReg.UserReg;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by Yohann on 2016/8/11.
 */
public class ServerTest {
    @Test
    public void test() {
        boolean result = false;
        try {
            result = new UserReg().register("yanghuan", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("result = " + result);
    }
}
