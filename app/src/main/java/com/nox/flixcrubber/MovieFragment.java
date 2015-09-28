package com.nox.flixcrubber;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    private ImageAdapter mImageAdapter;
    private GridView gridView;

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mImageAdapter = new ImageAdapter(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void updateMovies() {
        FetchMovieInfo movieInfo = new FetchMovieInfo();
        movieInfo.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMovieInfo extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = MovieFragment.class.getSimpleName();
        private static final String MOVIE_DB_KEY = "TheMovieDB API Key goes here";

        private String[] getMovieDataFromJson(String movieJsonString) throws JSONException{
            JSONObject movieJson = new JSONObject(movieJsonString);
            JSONArray resultsArray = movieJson.getJSONArray("results");
            String imageBase = "http://image.tmdb.org/t/p/w342";

            ArrayList<String> posterPathList = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject movieDetails = resultsArray.getJSONObject(i);
                String posterPath = movieDetails.getString("poster_path");
                posterPathList.add(imageBase + posterPath);
            }
            String[] posterPathArray = new String[posterPathList.size()];
            posterPathArray = posterPathList.toArray(posterPathArray);
            return posterPathArray;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonString;

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, "popularity.desc")
                        .appendQueryParameter(API_KEY, MOVIE_DB_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                if(buffer.length() == 0) {
                    return null;
                }
                moviesJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream.", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviesJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error handling JSON received from server.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null) {
                ImageAdapter imageAdapter = new ImageAdapter(getActivity());
                imageAdapter.setmThumbPaths(strings);
                gridView.invalidateViews();
                gridView.setAdapter(imageAdapter);
            }
        }
    }
}