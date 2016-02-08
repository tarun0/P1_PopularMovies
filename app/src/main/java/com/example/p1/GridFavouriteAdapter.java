package com.example.p1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tarun on 29/12/2015.
 */
public class GridFavouriteAdapter extends ArrayAdapter <String> {
    public GridFavouriteAdapter(Activity context, ArrayList<String> titles) {super(context,0,titles);}


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.check_title_xml,parent, false);
        }


        TextView textView = (TextView) convertView.findViewById(R.id.checkTextView);
        textView.setText(getItem(position));

        return convertView;
    }
}
