package com.example.p1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        TextView textView_title = (TextView) findViewById(R.id.textViewTitle);
        textView_title.setText(intent.getStringExtra("title"));

        TextView textView_rating = (TextView) findViewById(R.id.textViewRating);
        textView_rating.setText(intent.getStringExtra("rating"));

        TextView textView_ReleaseDate = (TextView) findViewById(R.id.textViewRelease);
        textView_ReleaseDate.setText(intent.getStringExtra("release_date"));

        TextView textView_Synopsis = (TextView) findViewById(R.id.textViewSynopsis);
        textView_Synopsis.setText(intent.getStringExtra("synopsis"));

        ImageView posterImage = (ImageView) findViewById(R.id.detailPosterImageview);

        ImageView backgroundImage = (ImageView) findViewById(R.id.backgroundImageView);

        Picasso.with(getApplicationContext()).load(intent.getStringExtra("poster")).into(posterImage);
        Picasso.with(getApplicationContext()).load(intent.getStringExtra("background")).into(backgroundImage);
    }

}
