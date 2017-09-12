package com;

import java.util.*;

/**
 * Created by lenovo on 2017/4/25.
 */
public class HashMapTest {

    public static void main(String[] args) {
        Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        for (int i=0;i<10000;i++){
            map.put(i,i);
        }
        long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
        Collection<Integer> values = map.values();
        Iterator<Integer> iterator = values.iterator();
        while (iterator.hasNext()){
            Integer next = iterator.next();
//            System.out.println("--"+next);
        }
        System.out.println(Calendar.getInstance().getTimeInMillis()-currentTimeMillis);
        long l = Calendar.getInstance().getTimeInMillis();
        Iterator<Map.Entry<Integer, Integer>> iterator1 = map.entrySet().iterator();
        while (iterator1.hasNext()){
            Map.Entry<Integer, Integer> next = iterator1.next();
            Integer value = next.getValue();
//            System.out.println("--"+value);
        }
        System.out.println(Calendar.getInstance().getTimeInMillis()-l);
    }
}
