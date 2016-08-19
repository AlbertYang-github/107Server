package test;

import org.junit.Test;
import utils.DetectionUtils;

import java.awt.*;
import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Created by Yohann on 2016/8/11.
 */
public class ServerTest {
    @Test
    public void test() {
        String s = DetectionUtils.detectFileFormat(new File("F:/video.avi"));
        System.out.println(s);
    }
}
