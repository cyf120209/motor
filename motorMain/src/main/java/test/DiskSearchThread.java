package test;

import java.io.File;
import java.util.Vector;

/**
 * 搜索文件系统的盘符
 * @author liuyazhuang
 *
 */
public class DiskSearchThread implements Runnable {

    /** root 现有文件系统的盘符 */
    private File[] roots = File.listRoots();
    /** fileVector 为了遍历U盘内文件 */
    private Vector<File> fileVector = new Vector<File>();
    volatile boolean sign = false;
    SearchFileThread t = null;

    public DiskSearchThread() {
    }

    @Override
    public void run() {
        System.out.println("Checking System...");

        while (true) {
            File[] tempFiles = File.listRoots();

            fileVector.removeAllElements();

            /** 检测到了有U盘插入 */
            if (tempFiles.length > roots.length) {
                for (int i = tempFiles.length - 1; i >= 0; i--) {
                    sign = false;
                    for (int j = roots.length - 1; j >= 0; j--) {
                        /** 如果前后比较的盘符相同 */
                        if (tempFiles[i].equals(roots[j])) {
                            sign = true;
                        }
                    }
                    /** 如果前后比较的盘符不相同，将不相同的盘符写入向量，并做进一步处理 */
                    if (!sign) {
                        fileVector.add(tempFiles[i]);
                    }

                }
                roots = File.listRoots();
                t = new SearchFileThread(fileVector);
                t.start();

            } else {
                for (int i = roots.length - 1; i >= 0; i--) {
                    sign = false;
                    for (int j = tempFiles.length - 1; j >= 0; j--) {
                        if (tempFiles[j].equals(roots[i])) {
                            sign = true;
                        }
                    }
                    /** 如果前后比较的盘符不相同，表明U盘被拔出 */
                    if (!sign) {
                        System.out.println("QUIT:" + roots[i].toString());
                        fileVector.removeAllElements();
                        t.setIsExistToFalse();
                        // roots=File.listRoots();
                    }
                }
                roots = File.listRoots();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new DiskSearchThread()).start();
    }
}
