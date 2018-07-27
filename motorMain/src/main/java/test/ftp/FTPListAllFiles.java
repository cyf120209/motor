package test.ftp;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FTPListAllFiles {
    private static Logger logger = Logger.getLogger(FTPListAllFiles.class);
    public FTPClient ftp;
    public ArrayList<String> arFiles;

    /**
     * 重载构造函数
     * @param isPrintCommmand 是否打印与FTPServer的交互命令
     */
    public FTPListAllFiles(boolean isPrintCommmand){
        ftp = new FTPClient();
        arFiles = new ArrayList<String>();
        if(isPrintCommmand){
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        }
    }

    /**
     * 登陆FTP服务器
     * @param host FTPServer IP地址
     * @param port FTPServer 端口
     * @param username FTPServer 登陆用户名
     * @param password FTPServer 登陆密码
     * @return 是否登录成功
     * @throws IOException
     */
    public boolean login(String host,int port,String username,String password) throws IOException{
        this.ftp.connect(host,port);
        if(FTPReply.isPositiveCompletion(this.ftp.getReplyCode())){
            if(this.ftp.login(username, password)){
                this.ftp.setControlEncoding("GBK");
                return true;
            }
        }
        if(this.ftp.isConnected()){
            this.ftp.disconnect();
        }
        return false;
    }

    /**
     * 关闭数据链接
     * @throws IOException
     */
    public void disConnection() throws IOException{
        if(this.ftp.isConnected()){
            this.ftp.disconnect();
        }
    }

    /**
     * 递归遍历出目录下面所有文件
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @throws IOException
     */
    public void List(String pathName) throws IOException{
        if(pathName.startsWith("/")&&pathName.endsWith("/")){
            String directory = pathName;
            //更换目录到当前目录
            this.ftp.changeWorkingDirectory(directory);
            FTPFile[] files = this.ftp.listFiles();
            for(FTPFile file:files){
                if(file.isFile()){
                    arFiles.add(directory+file.getName());
                }else if(file.isDirectory() && !file.getName().equals(".") && !file.getName().equals("..")){
                    List(directory+file.getName()+"/");
                }
            }
        }
    }

    /**
     * 递归遍历目录下面指定的文件扩展名
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param ext 文件的扩展名
     * @throws IOException
     */
    public void List(String pathName,String ext) throws IOException{
        if(pathName.startsWith("/")&&pathName.endsWith("/")){
            String directory = pathName;
            //更换目录到当前目录
            this.ftp.changeWorkingDirectory(directory);
            FTPFile[] files = this.ftp.listFiles();
            for(FTPFile file:files){
                if(file.isFile()){
                    if(file.getName().endsWith(ext)){
                        arFiles.add(file.getName());
                    }
                }
                else if(file.isDirectory() && !file.getName().equals(".") && !file.getName().equals("..")){
                    List(directory+file.getName()+"/",ext);
                }
            }
        }
    }

    /***
     * 下载文件
     * @param remoteFileName 待下载文件名称
     * @param localDires 下载到当地那个路径下
     * @param remoteDownLoadPath remoteFileName所在的路径
     * */
    public boolean downloadFile(String remoteFileName, String localDires,
                                String remoteDownLoadPath) {
        String strFilePath = localDires + remoteFileName;
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            ftp.changeWorkingDirectory(remoteDownLoadPath);
            outStream = new BufferedOutputStream(new FileOutputStream(
                    strFilePath));
            logger.info(remoteFileName + "开始下载....");
            success = ftp.retrieveFile(remoteFileName, outStream);
            if (success == true) {
                logger.info(remoteFileName + "成功下载到" + strFilePath);
                return success;
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(remoteFileName + "下载失败");
        }finally {
            if (null != outStream) {
                try {
                    outStream.flush();
                    outStream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (success == false) {
            logger.error(remoteFileName + "下载失败!!!");
        }
        return success;
    }

    public static void main(String[] args) throws IOException {
        FTPListAllFiles f = new FTPListAllFiles(true);
        if(f.login("110.80.31.162", 21, "draper", "draperpadmate")){
            f.List("/Padmate to Draper/kasim/firmware/","zip");
        }

        File file1 = new File("D:\\gujian\\");
        if (!file1.exists()){
            file1.mkdirs();
        }
        ArrayList<String> files = f.arFiles;
        for (String str:files){
            logger.info(str);
            String next = str;
            f.downloadFile(next,"D:\\gujian\\","/Padmate to Draper/kasim/firmware/");

        }
        f.disConnection();

    }
}