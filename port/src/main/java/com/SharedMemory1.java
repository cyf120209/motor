package com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by lenovo on 2017/4/14.
 */
public class SharedMemory1 {

    public static void main(String[] args) {
        try {
        String filename="D:/file";
        //获得一个只读的随机存取文件对象
        RandomAccessFile RAFile = new RandomAccessFile(filename, "r");
//获得相应的文件通道
        FileChannel fc = RAFile.getChannel();
//取得文件的实际大小
        int size = (int) fc.size();
//获得共享内存缓冲区，该共享内存只读
        MappedByteBuffer mapBuf = fc.map(FileChannel.MapMode.READ_ONLY,0, size);
//获得一个可读写的随机存取文件对象
        RAFile = new RandomAccessFile(filename, "rw");
//获得相应的文件通道
        fc = RAFile.getChannel();
//获得文件的实际大小，以便映像到共享内存
        size = (int) fc.size();
//获得共享内存缓冲区，该共享内存可读写
        mapBuf = fc.map(FileChannel.MapMode.READ_WRITE, 0, size);
//获取头部消息：存取权限
        int mode = mapBuf.getInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
