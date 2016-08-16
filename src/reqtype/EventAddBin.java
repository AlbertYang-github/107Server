package reqtype;

import bean.BinaryBean;
import bean.EventId;
import constants.Constants;
import utils.StreamUtils;
import utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
    public void addBin() {

        //以返回的id在本地创建目录 (该目录中存放该事件的媒体文件)
        File file = new File(Constants.EVENTS_PATH + EventId.id);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            //路径不为空，才进行写入
            if (EventId.voicePath != null) {
                StreamUtils.writeFileToDisk(EventId.voicePath, binaryBean.getVoiceStream());
            }
            if (EventId.picPath != null) {
                String[] picPaths = StringUtils.getArrayFromString(EventId.picPath);
                for (int i = 0; i < picPaths.length; i++) {
                    switch (i) {
                        case 0:
                            StreamUtils.writeFileToDisk(picPaths[0], binaryBean.getPicStream1());
                            break;
                        case 1:
                            StreamUtils.writeFileToDisk(picPaths[1], binaryBean.getPicStream2());
                            break;
                        case 2:
                            StreamUtils.writeFileToDisk(picPaths[2], binaryBean.getPicStream3());
                            break;
                    }
                }
            }
            if (EventId.videoPath != null) {
                StreamUtils.writeFileToDisk(EventId.videoPath, binaryBean.getVideoStream());
            }

        } finally {
            try {
                objIn.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
