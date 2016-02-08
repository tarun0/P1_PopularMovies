package com.example.p1;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tarun on 29/12/2015.
 */
public class RetrievedValues {
    public int total_items = -1;
    private String original_title;
    private URL poster_link;
    private String overview;
    private String rating;
    private String release_date;
    private URL background_link;
    private String id;

    public RetrievedValues(RetrievedValues copy){
        original_title=copy.get_title();
        poster_link=copy.get_poster_link();
        overview=copy.get_overview();
        rating=copy.get_rating();
        release_date=copy.get_release_date();
        total_items=copy.get_total_items();
        background_link = copy.getBackground_link();
        id = copy.getID();
    }

    public RetrievedValues(String title, String poster_path, String over_view, String rate, String rel_date, String background_path, String id){
        original_title = title;
        overview=over_view;
        rating = rate;
        release_date=rel_date;
        this.id = id;

        setBackground_link(background_path);
        set_poster_link(poster_path);
    }

    public String get_title() {
        return original_title;
    }

    public String getID(){ return id;}

    public  void set_title(String t){
        original_title = t;
    }

    public URL get_poster_link() {
        return poster_link;
    }

    public void set_poster_link (String string){

        URL url = null;

        try {
            url = new URL("http://image.tmdb.org/t/p/w185" + string);
        } catch (MalformedURLException u){
            Log.e("Retrieved class", "URL construct Error");
        }

    poster_link = url;
    }

    public URL getBackground_link(){return background_link;}

    public void  setBackground_link(String string){

        try{
            background_link = new URL("http://image.tmdb.org/t/p/w500" + string);
        } catch (MalformedURLException u){
            Log.e("Background url","Construction error");
        }
    }

    public void setID(String id){this.id = id;}

    public String get_overview() {
        return overview;
    }

    public void set_overview(String t) {
        overview = t;
    }

    public String get_release_date(){
        return release_date;
    }

    public void set_release_date(String t) {
        release_date = t;
    }

    public String get_rating(){
        return rating;
    }

    public void set_rating(String t) {
        rating = t;
    }

    public int get_total_items(){return total_items;}

    public void set_total_items(int i) {
        total_items = i;
    }
}
