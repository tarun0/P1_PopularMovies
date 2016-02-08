package com.example.p1;

import java.util.ArrayList;

/**
 * Created by Tarun on 22/01/2016.
 */
public class Reviews {
    private ArrayList<String> author;
    private ArrayList<String> review;

    public Reviews(){
        author = new ArrayList<>();
        review = new ArrayList<>();
    }

    public ArrayList<String> getAuthor(){
        return author;
    }

    public ArrayList<String> getReview(){
        return review;
    }

    public void addReview(String r){
        review.add(r);
    }

    public void addAuthor(String a){
        author.add(a);
    }
}
