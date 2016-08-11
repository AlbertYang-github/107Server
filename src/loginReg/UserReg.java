package loginReg;

import dao.UserDao;

import java.sql.SQLException;

/**
 * 用户注册
 * <p>
 * Created by Yohann on 2016/8/11.
 */
public class UserReg {

    private UserDao userDao;

    public UserReg() throws SQLException, ClassNotFoundException {
        userDao = new UserDao();
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    public boolean register(String username, String password) {

        int rows = userDao.insert(username, password);

        if (rows != 0) {
            return true;
        } else {
            return false;
        }
    }
}
