package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetLatLng {

    private static final Pattern patternUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

    public static void main(String[] args) {
        GetLatLng getLatLng = new GetLatLng();
        Map<String, String> latlng = getLatLng.getLatlng("");
    }

    private Map<String, String> getLatlng(String address) {
        try {
            address = URLEncoder.encode(address, "UTF-8");

            URL resjson = new URL("http://api.map.baidu.com/location/ip?ak=RB3sYPSHoONOaKVhfummljiCQb9xQUBf");
            BufferedReader in = null;
            if (resjson.openStream() != null) {
                in = new BufferedReader(new InputStreamReader(resjson.openStream()));
            }

            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            //System.out.println("return json:"+str);
//            String decode = URLDecoder.decode(str);
            str = unicode2StringWithStringBuilder(str);
            Map<String, String> map = null;
            if (str != null) {
                int lngStart = str.indexOf("\"y\":");
                int lngEnd = str.indexOf("\",\"x");
                int latEnd = str.indexOf("\"}},");
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lng = str.substring(lngStart + 5, lngEnd);
                    String lat = str.substring(lngEnd + 7, latEnd);
                    map = new HashMap<String, String>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String unicode2StringWithStringBuilder(final String unicode) {
        if (unicode != null) {
            try {
                Matcher matcher = patternUnicode.matcher(unicode);
                StringBuilder stringBuilder = new StringBuilder(unicode);
                int offset = 0; //StringBuilder替换长度不等的字符产生的位置偏移
                while (matcher.find()) {
                    String current = matcher.group();
                    String code = matcher.group(1);
                    String ch = String.valueOf((char) Integer.parseInt(code, 16));
                    stringBuilder.replace(matcher.start() + offset, matcher.end() + offset, ch);

                    offset += 1 - current.length(); //1为ch长度
                }
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return unicode;
            }
        } else {
            return unicode;
        }
    }

    public static String unicodeDecode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);

                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }


}
