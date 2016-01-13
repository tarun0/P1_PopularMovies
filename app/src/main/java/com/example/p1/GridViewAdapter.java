package com.example.p1;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tarun on 29/12/2015.
 */
public class GridViewAdapter extends ArrayAdapter <RetrievedValues> {
    public GridViewAdapter(Activity context, List<RetrievedValues> retrievedValuesList) {super(context,0,retrievedValuesList);}


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.poster,parent, false);
        }

        ImageView poster = (ImageView) convertView.findViewById(R.id.imageView);
        Picasso.with(getContext()).load(getItem(position).get_poster_link().toString()).into(poster);
        Log.e("adapter",getItem(position).get_poster_link().toString());
        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        poster.setAdjustViewBounds(true);
        return convertView;
    }
}
