package com.example.p1;

/**
 * Created by Tarun on 31/12/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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

public class TopRated extends android.support.v4.app.Fragment {

    View rootView;

    GridViewAdapter gridViewAdapter;
    GridView grid;
    List<RetrievedValues> retrievedValuesList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        String url = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_count.desc&api_key=" + BuildConfig.TMDb_KEY;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        rootView = inflater.inflate(R.layout.fragment_main_toprated, container, false);

        if (isConnected){
            new background().execute(url);

            gridViewAdapter = new GridViewAdapter(getActivity(), retrievedValuesList);

            grid = (GridView) rootView.findViewById(R.id.griview_toprated);
            grid.setAdapter(gridViewAdapter);
        }

        else{
            Toast.makeText(rootView.getContext(), "No internet", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    public class background extends AsyncTask<String, Void, RetrievedValues[]> {

        protected RetrievedValues[] retrievefromJSON(String jsonStr) throws JSONException {

            JSONObject moviesJSON = new JSONObject(jsonStr);
            JSONArray movies_list = moviesJSON.getJSONArray("results");

            int num_results = movies_list.length();

            RetrievedValues[] retrieved_values_objects = new RetrievedValues[num_results];

            for (int i = 0; i < num_results; i++) {
                JSONObject movie_details = movies_list.getJSONObject(i);
                String movie_title = movie_details.getString("title");
                String movie_poster_link = movie_details.getString("poster_path");
                String movie_background_link_path = movie_details.getString("backdrop_path");
                String movie_overview = movie_details.getString("overview");
                String movie_rating = movie_details.getString("vote_average");
                String movie_release_date = movie_details.getString("release_date");
                String movie_ID = movie_details.getString("id");

                retrieved_values_objects[i] = new RetrievedValues(movie_title, movie_poster_link, movie_overview, movie_rating, movie_release_date, movie_background_link_path, movie_ID);
                retrieved_values_objects[i].set_total_items(num_results);

            }

            Log.e("CHeck", retrieved_values_objects[19].get_rating());
            return retrieved_values_objects;
        }



        @Override
        protected RetrievedValues[] doInBackground(String... params) {

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
                Log.e("FetchWeather", "Error ", e);

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
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(RetrievedValues[] retrievedValues){

            super.onPostExecute(retrievedValues);
            gridViewAdapter.clear();
            Log.e("PostExecute", retrievedValues[1].get_rating());
            gridViewAdapter.addAll(retrievedValues);
            gridViewAdapter.notifyDataSetChanged();
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    android.support.v4.app.Fragment fragment = new DetailsActivity();
                    Bundle bundle=new Bundle();

                    Toast.makeText(rootView.getContext(), retrievedValuesList.get(i).get_title(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivityOnePane.class);




                    if(MainActivity.mIsTablet){
                        bundle.putString("title", retrievedValuesList.get(i).get_title());
                        bundle.putString("rating", retrievedValuesList.get(i).get_rating());
                        bundle.putString("release_date", retrievedValuesList.get(i).get_release_date());
                        bundle.putString("poster", retrievedValuesList.get(i).get_poster_link().toString());
                        bundle.putString("background", retrievedValuesList.get(i).getBackground_link().toString());
                        bundle.putString("synopsis", retrievedValuesList.get(i).get_overview());
                        bundle.putString("id", retrievedValuesList.get(i).getID());

                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
                        ft.addToBackStack("TopRatedFragment");
                        ft.replace(R.id.containerTablet, fragment);
                        ft.commit();
                    }

                    else{
                        intent.putExtra("title", retrievedValuesList.get(i).get_title());
                        intent.putExtra("rating", retrievedValuesList.get(i).get_rating());
                        intent.putExtra("release_date", retrievedValuesList.get(i).get_release_date());
                        intent.putExtra("poster", retrievedValuesList.get(i).get_poster_link().toString());
                        intent.putExtra("background", retrievedValuesList.get(i).getBackground_link().toString());
                        intent.putExtra("synopsis", retrievedValuesList.get(i).get_overview());
                        intent.putExtra("id", retrievedValuesList.get(i).getID());
                        getActivity().startActivity(intent);
                    }
                }
            });
        }
    }

}
