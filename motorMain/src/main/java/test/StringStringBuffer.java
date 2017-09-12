package test;

import java.util.HashMap;

/**
 * Created by lenovo on 2017/7/14.
 */
public class StringStringBuffer {

    public static void main(String[] args) {

        String s1="hello";
        String s2="world";
        System.out.println(s1+"---"+s2);
        change(s1,s2);
        System.out.println(s1+"---"+s2);
        StringBuffer sb1=new StringBuffer("hello");
        StringBuffer sb2=new StringBuffer("world");
        System.out.println(sb1+"---"+sb2);
        change(sb1,sb2);
        System.out.println(sb1+"---"+sb2);
        String a = new String("hello");
        String b = new String("hello");
//        String a = "hello";
//        String b = "hello";
        System.out.println(a==b);
        System.out.println(a.equals(b));
        HashMap<Integer,String> map=new HashMap<>(1024);
        map.put(1,"1");

        String s = map.get(1);
    }

    private static void change(StringBuffer sb1, StringBuffer sb2) {
        sb1=sb2;
        sb2.append(sb1);

    }

    private static void change(String s1, String s2) {
        s1=s2;
        s2=s1+s2;
    }
}
