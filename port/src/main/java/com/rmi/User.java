package com.rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/17.
 */
public class User implements Serializable{

    private String name;

    private List<String> list=new ArrayList<String>();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
