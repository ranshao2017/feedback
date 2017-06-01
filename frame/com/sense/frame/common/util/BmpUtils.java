package com.sense.frame.common.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BmpUtils {
    static byte[] head;
    static byte[] mixColors;
    static {
        head = new byte[] { 'B', 'M',(byte) 0xf6, (byte) 0x7a, 0, 0 // 整个文件的大小
                        , 0, 0, 0, 0 // 保留
                        , 0x36, 0x04, 0, 0 // 从文件开始到位图数据开始之间的数据(bitmap data)之间的偏移量
                        , 0x28, 0, 0, 0 // 位图信息头(Bitmap Info Header)的长度
                        , (byte) 0x98, 0, 0, 0 // 位图的宽度，以像素为单位
                        , (byte) 0xc8, 0, 0, 0 // 位图的高度，以像素为单位
                        , 1, 0 // 位图的位面数
                        // 图像信息头
                        , 8, 0 // 每个像素的位数
                        , 0, 0, 0, 0 // 压缩说明：
                        , (byte)0xc0, (byte) 0x76, 0, 0 // 用字节数表示的位图数据的大小。该数必须是4的倍数
                        , (byte) 0xe5, (byte) 0x4c, 0, 0 // 用像素/米表示的水平分辨率
                        , (byte) 0xe5, (byte) 0x4c, 0, 0 // 用像素/米表示的垂直分辨率
                        , 0, 0, 0, 0 // 位图使用的颜色数。如8-位/像素表示为100h或者 256.
                        , 0, 0, 0, 0 // 指定重要的颜色数。当该域的值等于颜色数时，表示所有颜色都一样重要
        };
        mixColors = new byte[4 * 256];
        for (int i = 0; i < 256; i++) {
            mixColors[i * 4] = (byte) i;
            mixColors[i * 4 + 1] = (byte) i;
            mixColors[i * 4 + 2] = (byte) i;
        }
    }

    public static void savePicAppendHead(InputStream is, String target) throws IOException {
        OutputStream os = new FileOutputStream(target);
        os.write(head);
        os.write(mixColors);
        os.flush();
        byte[] buffer = new byte[1024];
        int readSize = 0;
        while ((readSize = is.read(buffer)) > 0) {
            os.write(buffer, 0, readSize);
        }
        is.close();
        os.flush();
        os.close();
    }
    public static void savePicNoHead(InputStream is, String target) throws IOException {
    	OutputStream os = new FileOutputStream(target);
    	os.flush();
    	byte[] buffer = new byte[1024];
    	int readSize = 0;
    	while ((readSize = is.read(buffer)) > 0) {
    		os.write(buffer, 0, readSize);
    	}
    	is.close();
    	os.flush();
    	os.close();
    }
}
