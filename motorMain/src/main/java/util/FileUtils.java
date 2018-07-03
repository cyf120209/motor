package util;

import test.Country;

import java.io.*;

public class FileUtils {

    public static String readFile(String filePath){
        StringBuilder builder=new StringBuilder(2048);
        BufferedReader bufferedReader=null;
        FileReader fileReader=null;
        try {
            fileReader=new FileReader(filePath);
            bufferedReader=new BufferedReader(fileReader);
            char[] c=new char[2048];
            int length=0;
            while ((length=bufferedReader.read(c))!=-1){
                builder.append(c,0,length);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "";
    }

    public static void writeFile(String filePath,String json){
        try {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fw);
            bufferedWriter.write(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String filePath,StringBuilder builder){
        try {
            File file=new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(builder.toString().getBytes());
            byte[] b=new byte[2048];
            int len=0;
            while ((len=byteArrayInputStream.read(b))!=-1){
                fos.write(b,0,len);
            }
            builder.toString().getBytes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
