package reqtype;

import bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import loginreg.UserLogin;
import utils.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by Yohann on 2016/8/12.
 */
public class Login {
    private Socket socket;
    private Gson gson;
    private UserBean userBean;

    public Login(Socket socket) {
        this.socket = socket;
        gson = new Gson();
    }

    public void login(String body) throws SQLException, ClassNotFoundException, IOException {

        String msgReturn = "error";

        userBean = gson.fromJson(body, UserBean.class);
        //用户名
        String username = userBean.getUsername();
        //密码
        String password = userBean.getPassword();
        //登录
        UserLogin userLogin = new UserLogin();
        boolean result = userLogin.login(username, password);

        if (result) {
            //获取用户信息
            msgReturn = userLogin.getMsg(username);
        }
        System.out.println("msgReturn = " + msgReturn);

        //向客户端返回登录结果
        OutputStream out = socket.getOutputStream();
        StreamUtils.writeString(out, msgReturn);
        socket.shutdownOutput();

        //关闭流和socket
        StreamUtils.close();
        socket.close();
    }
}