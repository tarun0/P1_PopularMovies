package com.example.p1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Tarun on 31/01/2016.
 */
public class Favourites extends android.support.v4.app.Fragment {

    GridView grid;
    GridFavouriteAdapter gridFavouriteAdapter;
    View rootView;
    PostsDatabaseHelper db;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_main_toprated, container, false);
        }

        db = new PostsDatabaseHelper(getActivity().getApplicationContext());
        if (db.isEmpty()){
            Toast.makeText(rootView.getContext(), "No FAVOURITE!", Toast.LENGTH_SHORT).show();
        }
        else{
            final ArrayList<String> arrayList = db.getValues();
            Log.e("Values",arrayList.get(0));
            gridFavouriteAdapter = new GridFavouriteAdapter(getActivity(), arrayList);
            grid = (GridView) rootView.findViewById(R.id.griview_toprated);
            grid.setAdapter(gridFavouriteAdapter);
            intent = new Intent(getActivity().getApplicationContext(), DetailsActivityOnePane.class);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Cursor cursor = db.getMovieValues(arrayList.get(i));
                    cursor.moveToFirst();

                    if(!MainActivity.mIsTablet)  {          //If it's a One Pane Device

                        Log.e("Selected item", arrayList.get(i));

                        intent.putExtra("id", cursor.getString(0));

                        intent.putExtra("synopsis", cursor.getString(1));
                        intent.putExtra("release_date",cursor.getString(2));
                        intent.putExtra("rating",cursor.getString(3));
                        intent.putExtra("title", cursor.getString(4));
                        intent.putExtra("poster", cursor.getString(5));

                        startActivity(intent);
                    }
                    else{                                   //If it's a tablet
                        Bundle bundle = new Bundle();
                        bundle.putString("id", cursor.getString(0));
                        bundle.putString("synopsis", cursor.getString(1));
                        bundle.putString("release_date",cursor.getString(2));
                        bundle.putString("title", cursor.getString(4));
                        bundle.putString("poster", cursor.getString(5));
                        bundle.putString("rating", cursor.getString(3));

                        android.support.v4.app.Fragment fragment = new DetailsActivity();
                        fragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.addToBackStack("Favorite added to Backstack");
                        ft.replace(R.id.containerTablet,fragment);
                        ft.commit();
                    }


                }
            });

        }

        return rootView;
    }

}
