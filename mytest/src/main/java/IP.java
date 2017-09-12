
import net.sf.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

/**
 * Created by lenovo on 2017/6/20.
 */
public class IP {
    public static void main(String[] args) {
        IP ip = new IP();
//        String addressByIP = ip.getAddressByIP();
//        System.out.println(addressByIP);
//        String jsonContent = GetAddressByIp("27.154.54.134");
//        System.out.println(jsonContent);
        try {
            ip.getIp();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void getIp() throws SocketException {
        Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
        while (interfs.hasMoreElements())
        {
            NetworkInterface interf = interfs.nextElement();
            Enumeration<InetAddress> addres = interf.getInetAddresses();
            while (addres.hasMoreElements())
            {
                InetAddress in = addres.nextElement();
                if (in instanceof Inet4Address)
                {
                    System.out.println("v4:" + in.getHostAddress());
                }
                else if (in instanceof Inet6Address)
                {
                    System.out.println("v6:" + in.getHostAddress());
                }
            }
        }
    }

    public String getAddressByIP() {
        try {
            String strIP = "27.154.54.134";
            URL url = new URL("http://ip.qq.com/cgi-bin/searchip?searchip1=" + strIP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            strIP = result.substring(result.indexOf("该IP所在地为："));
            strIP = strIP.substring(strIP.indexOf("：") + 1);
            String province = strIP.substring(6, strIP.indexOf("省"));
            String city = strIP.substring(strIP.indexOf("省") + 1, strIP.indexOf("市"));
        } catch (IOException e) {
            return "读取失败";
        }
        return "";
    }

    public static String GetAddressByIp(String IP){
        String resout = "";
        try{
            String str = getJsonContent("http://ip.taobao.com/service/getIpInfo.php?ip="+IP);

            System.out.println(str);
            JSONObject obj = JSONObject.fromObject(str);
            JSONObject obj2 =  (JSONObject) obj.get("data");
            String code = String.valueOf(obj.get("code"));
            if(code.equals("0")){

                resout =  obj2.get("country")+"--" +obj2.get("area")+"--" +obj2.get("city")+"--" +obj2.get("isp");
            }else{
                resout =  "IP地址有误";
            }
        }catch(Exception e){

            e.printStackTrace();
            resout = "获取IP地址异常："+e.getMessage();
        }
        return resout;

    }

    public static String getJsonContent(String urlStr)
    {
        try
        {// 获取HttpURLConnection连接对象
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url
                    .openConnection();
            // 设置连接属性
            httpConn.setConnectTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            // 获取相应码
            int respCode = httpConn.getResponseCode();
            if (respCode == 200)
            {
                return ConvertStream2Json(httpConn.getInputStream());
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }


    private static String ConvertStream2Json(InputStream inputStream)
    {
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        try
        {
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
            {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonStr;
    }
}
