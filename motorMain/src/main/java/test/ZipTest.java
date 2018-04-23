package test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipTest {

    public static void main(String[] args) {
        ZipTest zipTest = new ZipTest();
        zipTest.readZip();
    }

    /**
     * 读取zip文件内容
     */
    private void readZip() {
        File fil = new File("D:\\Android\\Android.zip");
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(new FileInputStream(fil));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ZipEntry zipEn = null;
        /**
         * 需要读取zip文件项的内容时，需要ZipFile类的对象的getInputStream方法取得该项的内容，
         * 然后传递给InputStreamReader的构造方法创建InputStreamReader对象，
         * 最后使用此InputStreamReader对象创建BufferedReader实例
         * 至此已把zip文件项的内容读出到缓存中，可以遍历其内容
         */
        ZipFile zfil = null;
        try {
            zfil = new ZipFile("D:\\Android\\Android.zip");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            while ((zipEn = zipIn.getNextEntry()) != null) {
                if (!zipEn.isDirectory()) { // 判断此zip项是否为目录
                    System.out.println(zipEn.getName() + ":\t");
                    /**
                     * 把是文件的zip项读出缓存，
                     * zfil.getInputStream(zipEn)：返回输入流读取指定zip文件条目的内容 zfil：new
                     * ZipFile();供阅读的zip文件 zipEn：zip文件中的某一项
                     */
//                    BufferedReader buff = new BufferedReader(
//                            new InputStreamReader(zfil.getInputStream(zipEn)));
//                    String str;
//                    while ((str = buff.readLine()) != null) {
//                        System.out.println("\t" + str);
//                    }
//                    buff.close();
                    long size = zipEn.getSize();
                    byte[] b = new byte[(int) size];
                    InputStream is = zfil.getInputStream(zipEn);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    while (bis.available()>0){
                        bis.read(b);
                    }
                    String s = new String(b);
                    System.out.println("\t"+s);
                    bis.close();
                }
                zipIn.closeEntry();// 关闭当前打开的项
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                zfil.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
