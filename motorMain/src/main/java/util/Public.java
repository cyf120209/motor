package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/9/14.
 */
public class Public {

    /**
     * 正则匹配
     *
     * @param str
     * @param regx
     * @return
     */
    public static Boolean matchString(String str, String regx) {
        //1.将正在表达式封装成对象Patten 类来实现
        Pattern pattern = Pattern.compile(regx);
        //2.将字符串和正则表达式相关联
        Matcher matcher = pattern.matcher(str);
        //3.String 对象中的matches 方法就是通过这个Matcher和pattern来实现的。
        System.out.println(matcher.matches());
//        String group = "";
        //查找符合规则的子串
        while (matcher.find()) {
            //获取 字符串
//            group = matcher.group();
            //获取的字符串的首位置和末位置
//            System.out.println(matcher.start() + "--" + matcher.end());
            return true;
        }
        return false;
    }
}
