package loginReg;

import dao.UserDao;

import java.sql.SQLException;

/**
 * 用户注册
 * <p>
 * Created by Yohann on 2016/8/11.
 */
public class UserLogin {

    private UserDao userDao;

    public UserLogin() throws SQLException, ClassNotFoundException {
        userDao = new UserDao();
    }

    /**
     * 登录
     *
     * @param username
     * @return
     */
    public boolean login(String username, String password) {

        String result = userDao.query(username);

        if (result != null) {
            if (result.equals(password)) {
               return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
