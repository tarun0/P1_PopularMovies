package com.example.p1;

import java.util.ArrayList;

/**
 * Created by Tarun on 8/02/2016.
 */
public class Trailer {
    private ArrayList<String> key;
    private ArrayList<String> name;

    public Trailer(){
        key = new ArrayList<>();
        name = new ArrayList<>();
    }

    public ArrayList<String> getKey(){
        return key;
    }

    public ArrayList<String> getName(){
        return name;
    }

    public void addKey(String r){
        key.add(r);
    }

    public void addName(String a){
        name.add(a);
    }

    public String toUrlString (String key){
        return ("http://www.youtube.com/watch?v="+key);
    }
}