package test;

import java.io.File;
import java.util.Vector;

/**
 * 搜索文件的线程
 * @author liuyazhuang
 *
 */
public class SearchFileThread extends Thread {

    private Vector<File> fileVector = null;

    private int scanNum = 1;

    /** 线程安全的变量，用于退出线程 */
    volatile boolean isExist = true;

    public SearchFileThread(Vector<File> fileVector) {
        this.fileVector = fileVector;
        System.out.println("fileVector size:" + fileVector.size());
    }

    @Override
    public void run() {

        File file = fileVector.elementAt(scanNum - 1);
        long totalMemory = file.getFreeSpace();

        while (isExist) {

            while (scanNum <= fileVector.size()) {

                try {
                    System.out.println("search:"
                            + fileVector.elementAt(scanNum - 1).toString()
                            + " Total Space:"
                            + fileVector.elementAt(scanNum - 1).getTotalSpace()
                            / 1024 / 1024 + "MB Free Space:"
                            + fileVector.elementAt(scanNum - 1).getFreeSpace()
                            / 1024 / 1024 + "MB");
                    /** 遍历文件内容 */
                    getFiles(fileVector.elementAt(scanNum - 1).getPath());
                    scanNum++;
                } catch (Exception e) {
                    e.printStackTrace();
                    scanNum++;
                }
            }
            /** 如果盘符的大小发生变化，则有文件进出 */
            if (totalMemory != file.getFreeSpace()) {
                System.out.println("文件发生变化----------------------");
                getFiles(file.getPath());
                totalMemory = file.getFreeSpace();
            }
        }

    }

    /**
     * 递归遍历文件
     * @param path
     */
    public void getFiles(String path) {
        try {
            File file = new File(path);
            if (file.isDirectory()) {
                File[] list = file.listFiles();
                for (int i = 0; i < list.length; i++) {
                    if (list[i].isDirectory()) {
                        /** 递归调用 */
                        getFiles(list[i].getPath());
                    }
                    System.out.println("Find File:" + list[i].getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void setIsExistToFalse() {
        if (isExist)
            isExist = false;
    }
}
