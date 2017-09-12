package com;

import java.math.BigInteger;

/**
 * Created by dministrator on 2016/3/8.
 */
public class Byte2IntUtils {
//    public static int bytes2Short(byte h,byte l){
//        return ((h&0xff)<<8)+(l&0xff);
//    }

    /**
     * 将两位byte转化成十进制数
     * @param h
     * @param l
     * @return
     */
    public static int bytes2Short(byte h, byte l) {
        return new BigInteger(new byte[] { h, l }).shortValue();
    }

    /**
     * 将十进制数转成两位byte
     * @param i
     * @return
     */
    public static byte[] Short2bytes(int i) {
        byte[] tmp=new byte[2];
        tmp[0]=(byte)((i&0xff00)>>8);
        tmp[1]=(byte)((i&0xff));
        return tmp;
    }

    /**
     * 将byte转成int
     * @param h
     * @return
     */
    public static int byte2Int(byte h){
        return (h&0xff);
    }

    /**
     * byte数组转化成十六进制字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString != null) {
                if (hexString.length() == 1) {
                    hexString = '0' + hexString;
                }
                result += hexString.toUpperCase();
            }
        }
        return result;
    }

    /**
     * Convert hex string to byte[]
     * 十六进制字符串转 byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}

