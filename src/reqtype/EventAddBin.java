package reqtype;

import bean.EventId;
import constants.Constants;
import dao.EventDao;
import utils.DetectionUtils;
import utils.StreamUtils;
import utils.StringUtils;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Yohann on 2016/8/16.
 */
public class EventAddBin {
    private Socket socket;
    private InputStream in;

    public EventAddBin(Socket socket, InputStream in) {
        this.socket = socket;
        this.in = in;
    }

    /**
     * 将客户端上传的二进制文件存放在本地
     */
    public void addBin() throws IOException, SQLException, ClassNotFoundException {

        //以返回的id在本地创建目录 (该目录中存放该事件的媒体文件)
        File file = new File(Constants.EVENTS_PATH + EventId.id);
        if (!file.exists()) {
            file.mkdirs();
        }

        //将zip文件写入磁盘
        StreamUtils.writeFileToDisk(Constants.EVENTS_PATH + EventId.id + ".zip", in);

        //向客户端返回执行结果
        OutputStream out = socket.getOutputStream();
        StreamUtils.writeString(out, "1");
        socket.shutdownOutput();
        System.out.println("zip文件上传完成");

        in.close();
        out.close();
        socket.close();

        //进行后续操作
        after();
    }

    /**
     * 服务器后续操作：
     * 1.解压
     * 2.数据库添加路径
     */
    public void after() throws IOException, SQLException, ClassNotFoundException {
        //解压
        FileInputStream fis = new FileInputStream(Constants.EVENTS_PATH + EventId.id + ".zip");
        StreamUtils.decompress(new File(Constants.EVENTS_PATH + EventId.id), fis);
        fis.close();
        System.out.println("解压完成");

        //指定文件路径
        String voicePath = null;
        String picPath = null;
        String videoPath = null;
        String zipPath = Constants.EVENTS_PATH + EventId.id + ".zip";
        ArrayList<String> picPathList = new ArrayList<>();

        File dir = new File(Constants.EVENTS_PATH + EventId.id);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String type = DetectionUtils.detectFileFormat(file);
            switch (type) {
                case "voice":
                    voicePath = file.getAbsolutePath();
                    break;
                case "picture":
                    picPathList.add(file.getAbsolutePath());
                    break;
                case "video":
                    videoPath = file.getAbsolutePath();
                    break;
            }
        }
        picPath = StringUtils.getStringFromArrayList(picPathList);

        //将路径添加到数据库中
        new EventDao().addBinaryPath(voicePath, picPath, videoPath, zipPath, EventId.id);
    }
}
