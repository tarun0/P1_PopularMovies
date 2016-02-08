package com.example.p1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends android.support.v4.app.Fragment {
    boolean fab_state;
    ArrayAdapter<String> reviewAdaptor, trailerAdaptor;
    ListView reviewListView, trailerListView;
    Context context;
    PostsDatabaseHelper db;


    String mID, mTitle, mRating, mRelDate, mSynopsis, mPoster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new PostsDatabaseHelper(getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.activity_details, container, false);

        mID = getArguments().getString("id");
        mTitle = getArguments().getString("title");
        mSynopsis = getArguments().getString("synopsis");
        mRelDate = getArguments().getString("release_date");
        mRating = getArguments().getString("rating");
        mPoster = getArguments().getString("poster");

        context = getContext();
        fab_state = db.ispresent(mID);

        final FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);


        if (fab_state) {
            Log.e("ISPRESENT",fab_state+"");
            floatingActionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
        }
        else {
            floatingActionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fab_state) {

                    Log.e("ISPRESENT",fab_state+"");
                    floatingActionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    db.insertMovies(mID, mSynopsis, mRelDate, mRating, mTitle, mPoster);
                    fab_state = db.ispresent(mID);
                    
                } else {
                    floatingActionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    db.deleteMovie(mID);
                    fab_state = db.ispresent(mID);

                }

            }
        });

        View seperatorView = (View) rootView.findViewById(R.id.separator);
        seperatorView.setVisibility(View.VISIBLE);

        TextView reviewHead = (TextView) rootView.findViewById(R.id.reviewHeading);
        reviewHead.setText("Reviews");

        TextView trailerHead = (TextView) rootView.findViewById(R.id.trailerHeading);
        trailerHead.setText("Trailers");

        TextView synHead = (TextView) rootView.findViewById(R.id.synopsisHeading);
        synHead.setText("Synopsis");

        TextView textView_title = (TextView) rootView.findViewById(R.id.textViewTitle);
        textView_title.setText(mTitle);

        TextView textView_rating = (TextView) rootView.findViewById(R.id.textViewRating);
        textView_rating.setText(mRating);

        TextView textView_ReleaseDate = (TextView) rootView.findViewById(R.id.textViewRelease);
        textView_ReleaseDate.setText(mRelDate);

        TextView textView_Synopsis = (TextView) rootView.findViewById(R.id.textViewSynopsis);
        textView_Synopsis.setText(mSynopsis);

        ImageView posterImage = (ImageView) rootView.findViewById(R.id.detailPosterImageview);
        Picasso.with(getActivity().getApplicationContext()).load(mPoster).into(posterImage);


        reviewListView = (ListView) rootView.findViewById(R.id.listViewReview);
        trailerListView= (ListView) rootView.findViewById(R.id.trailerListView);

        int[] colors = {0xFFFFFFFF, 0x00000000, 0xFFFFFFFF};
        reviewListView.setDivider(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));
        reviewListView.setDividerHeight(1);


        String url = "http://api.themoviedb.org/3/movie/" + mID + "/reviews?" +"api_key=" + BuildConfig.TMDb_KEY;
        String urlTrailer = "http://api.themoviedb.org/3/movie/" + mID + "/videos?" +"api_key=" + BuildConfig.TMDb_KEY;
        Log.e("FetchRev", urlTrailer);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            List<String> dummyList = new ArrayList<>();
            dummyList.add("A for apple");
            dummyList.add("B for ball");

            List<String> dummyList1 = new ArrayList<>();
            dummyList1.add("A");
            dummyList1.add("B");
            reviewAdaptor = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.review, R.id.reviewItemTextView, dummyList);
            trailerAdaptor = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.trailer, R.id.trailerItem, dummyList1);

            new Reviewbackground().execute(url);
            new TrailerBackground().execute(urlTrailer);
            reviewAdaptor.notifyDataSetChanged();
            trailerAdaptor.notifyDataSetChanged();
        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "No internet, Can't Fetch Reviews, Poster and Trailers!", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }



    public class Reviewbackground extends AsyncTask<String, Void, Reviews> {

        protected Reviews retrievefromJSON(String jsonStr) throws JSONException {

            JSONObject moviesJSON = new JSONObject(jsonStr);
            JSONArray movie_review_results = moviesJSON.getJSONArray("results");

            int num_results = movie_review_results.length();
            Log.e("CHeck", jsonStr);
            Reviews review_object = new Reviews();

            for (int i = 0; i < num_results; i++) {
                JSONObject movie_review = movie_review_results.getJSONObject(i);
                String review = movie_review.getString("content");
                String author = movie_review.getString("author");

                review_object.addAuthor(author);
                review_object.addReview(review);
            }


            return review_object;
    }

        @Override
        protected Reviews doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String fetchedJSONStr = null;

            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                fetchedJSONStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FetchReview", "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        try {
                            return retrievefromJSON(fetchedJSONStr);
                        } catch (JSONException p) {
                            Log.e("JSON Error", "Error parsing JSON", p);
                        }
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("fetchReview", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Reviews reviews){

            super.onPostExecute(reviews);
                for (int p = 0; p< reviews.getReview().size(); p++){
                    Log.e("R"+p , reviews.getAuthor().get(p)+"\n");
                }

            reviewAdaptor.clear();
            reviewAdaptor.addAll(reviews.getReview());
            reviewListView.setAdapter(reviewAdaptor);
            reviewAdaptor.notifyDataSetChanged();

            Log.e("PostExecute", "Review onPOSTEXECUTE");

        }
    }

    public class TrailerBackground extends AsyncTask<String, Void, Trailer>{

        protected Trailer mretrievefromJSON (String jsonStr) throws JSONException{
            JSONObject moviesJSON = new JSONObject(jsonStr);
            JSONArray trailer_results = moviesJSON.getJSONArray("results");

            int num_results = trailer_results.length();
            Log.e("CHeck", jsonStr);
            Trailer allTrailers = new Trailer();

            for (int i = 0; i < num_results; i++) {
                JSONObject trailerItem = trailer_results.getJSONObject(i);
                allTrailers.addKey(allTrailers.toUrlString(trailerItem.getString("key")));
                Log.e("Added Key", trailerItem.getString("key"));

                allTrailers.addName(trailerItem.getString("name"));
            }

            return allTrailers;
        }

        @Override
        protected Trailer doInBackground (String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String fetchedJSONStr = null;

            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                fetchedJSONStr = buffer.toString();
            } catch (IOException e) {

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        try {
                            return mretrievefromJSON(fetchedJSONStr);
                        } catch (JSONException p) {
                            Log.e("JSON Error", "Error parsing JSON", p);
                        }
                        reader.close();
                    } catch (final IOException e) {
                       Log.e(getActivity().getLocalClassName(), "JSON Parse IOException");
                    }
                }
            }
            return null;
        }

        @Override
        public void onPostExecute (final Trailer trailer){
            super.onPostExecute(trailer);

            trailerAdaptor.clear();
            trailerAdaptor.addAll(trailer.getName());
            trailerListView.setAdapter(trailerAdaptor);
            trailerAdaptor.notifyDataSetChanged();

            trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Uri uri = Uri.parse(trailer.getKey().get(i));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }

}
