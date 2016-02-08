package com.example.p1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tarun on 08/02/2016.
 */
public class DetailsActivityOnePane extends AppCompatActivity{
    Intent intent;
    String mID, mTitle, mRating, mRelDate, mSynopsis, mPoster;

    @Override
    protected void onCreate (Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);

        //Retrieving data from fragment to Activity
        intent = getIntent();

        mID = intent.getStringExtra("id");
        mTitle = intent.getStringExtra("title");
        mSynopsis = intent.getStringExtra("synopsis");
        mRelDate = intent.getStringExtra("release_date");
        mRating = intent.getStringExtra("rating");
        mPoster = intent.getStringExtra("poster");

        //Now setting the data to be passed to the fragment into Bundle

        Bundle bundle = new Bundle();
        bundle.putString("id", mID);
        bundle.putString("title", mTitle);
        bundle.putString("synopsis", mSynopsis);
        bundle.putString("release_date", mRelDate);
        bundle.putString("rating", mRating);
        bundle.putString("poster", mPoster);

        setContentView(R.layout.activity_details);

        android.support.v4.app.Fragment fragment = new DetailsActivity();
        fragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.containerOnePane, fragment);
        ft.commit();

    }

}
