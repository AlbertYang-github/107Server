package reqtype;

import bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import loginreg.UserReg;
import utils.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by Yohann on 2016/8/12.
 */
public class Register {
    private Socket socket;
    private UserBean userBean;
    private Gson gson;

    public Register(Socket socket) {
        this.socket = socket;
        gson = new Gson();
    }

    public void register(String body) throws SQLException, ClassNotFoundException, IOException {

        String msgReturn = "error";

        userBean = gson.fromJson(body, UserBean.class);
        //用户名
        String username = userBean.getUsername();
        //密码
        String password = userBean.getPassword();
        //注册
        UserReg userReg = new UserReg();
        boolean result = userReg.register(username, password);

        if (result) {
            //获取用户信息
            msgReturn = userReg.getMsg(username);
        }
        System.out.println("msgReturn = " + msgReturn);

        //向客户端返回注册结果
        OutputStream out = socket.getOutputStream();
        StreamUtils.writeString(out, msgReturn);
        socket.shutdownOutput();

        //关闭流和socket
        StreamUtils.close();
        socket.close();
    }
}
