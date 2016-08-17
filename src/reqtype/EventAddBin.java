package reqtype;

import bean.BinaryBean;
import bean.EventId;
import constants.Constants;
import utils.StreamUtils;
import utils.StringUtils;

import java.io.*;
import java.net.Socket;

/**
 * Created by Yohann on 2016/8/16.
 */
public class EventAddBin {
    private Socket socket;
    private BinaryBean binaryBean;
    private InputStream in;
    private ObjectInputStream objIn;

    public EventAddBin(Socket socket, BinaryBean binaryBean, InputStream in, ObjectInputStream objIn) {
        this.socket = socket;
        this.binaryBean = binaryBean;
        this.in = in;
        this.objIn = objIn;
    }

    /**
     * 将客户端上传的二进制文件存放在本地
     */
    public void addBin() throws IOException {

        //以返回的id在本地创建目录 (该目录中存放该事件的媒体文件)
        File file = new File(Constants.EVENTS_PATH + EventId.id);
        if (!file.exists()) {
            file.mkdirs();
        }

        //路径不为空，才进行写入
        if (EventId.voicePath != null) {
            StreamUtils.writeFileToDisk(EventId.voicePath, binaryBean.getVoiceBytes());
        }
        if (EventId.picPath != null) {
            String[] picPaths = StringUtils.getArrayFromString(EventId.picPath);
            for (int i = 0; i < picPaths.length; i++) {
                switch (i) {
                    case 0:
                        StreamUtils.writeFileToDisk(picPaths[0], binaryBean.getPicBytes1());
                        break;
                    case 1:
                        StreamUtils.writeFileToDisk(picPaths[1], binaryBean.getPicBytes2());
                        break;
                    case 2:
                        StreamUtils.writeFileToDisk(picPaths[2], binaryBean.getPicBytes3());
                        break;
                }
            }
        }
        if (EventId.videoPath != null) {
            StreamUtils.writeFileToDisk(EventId.videoPath, binaryBean.getVideoBytes());
        }

        //向客户端返回执行结果
        OutputStream out = socket.getOutputStream();
        StreamUtils.writeString(out, "1");
        socket.shutdownOutput();

        objIn.close();
        in.close();
        out.close();
        socket.close();
    }

}
