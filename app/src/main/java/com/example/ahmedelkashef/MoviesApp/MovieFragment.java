package com.example.ahmedelkashef.MoviesApp;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ahmedelkashef.MoviesApp.Adapter.MovieAdapter;
import com.example.ahmedelkashef.MoviesApp.data.MovieDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by ahmedelkashef on 8/15/2016.
 */
public  class MovieFragment extends Fragment {

    MovieContext[] Arr;
    MovieAdapter Ma;
    GridView MovieGridview;
    MovieDatabaseHelper db ;
    MovieCallback mc;


    public MovieFragment() {

    }

    public void onStart() {
        super.onStart();

        FetchMovieDataTask fetchMovieDataTask=  new FetchMovieDataTask();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String  Selection = preferences.getString(getString(R.string.pref_movie_key),
                getString(R.string.pref_value_popular));

        if(!Selection.equals(getString(R.string.pref_value_favorites)) && isNetworkAvailable(getContext())) {
            fetchMovieDataTask.execute(Selection);
        }
        else if (Selection.equals(getString(R.string.pref_value_favorites)) && isNetworkAvailable(getContext())) {
            Cursor c =  db.getFavoriteData();
            FetchFromDatabase(c);
        }

    }
    public void setCallback(MovieCallback Mc){
        mc = Mc;
    }
    public interface MovieCallback {
        public void onItemSelected(MovieContext movie);

    }
    public void  FetchFromDatabase(Cursor c){

        MovieContext[] mArr = new MovieContext[c.getCount()];

        int i = 0;
        while (c.moveToNext()) {
            mArr[i] = new MovieContext();
            mArr[i].setID(Integer.parseInt(c.getString(0)));
            mArr[i].setTitle(c.getString(1));
            mArr[i].setImageURL(c.getString(2));
            mArr[i].setOverview(c.getString(3));
            mArr[i].setReleaseDate(c.getString(4));
            mArr[i].setRate(c.getString(5));
            mArr[i].setIsmoviefavaourite(c.getString(6));
            i++;
        }
        Ma = new MovieAdapter(getActivity(), Arrays.asList(mArr));
        Arr = mArr;
        MovieGridview.setAdapter(Ma);
    }
    public static  boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        if(isNetworkAvailable(getContext())){
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            MovieGridview = (GridView) rootView.findViewById(R.id.movies_gridview);


            MovieGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieContext SelecetedMovie = Arr[i];
                    ((MovieCallback) getActivity()).onItemSelected(SelecetedMovie);

                }
            });

        }
        else {
            rootView  = inflater.inflate(R.layout.network_unavailable, container, false);
        }
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db  = new MovieDatabaseHelper(getContext());
        setHasOptionsMenu(true);

    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, MovieContext[]> {

        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();

        private MovieContext[] getMovieDataFromJson(String MoviesJsonStr)
                throws JSONException {

            final String POSTER_PATH = "poster_path";
            final String OVERVIEW = "overview";
            final String TITLE = "title";
            final String RELEASE_DATE = "release_date";
            final String RATE = "vote_average";
            final String ID = "id";

            JSONObject MoviesJson = new JSONObject(MoviesJsonStr);
            JSONArray MoviesArray = MoviesJson.getJSONArray("results");

            Arr = new MovieContext[MoviesArray.length()];

            for (int i = 0; i < MoviesArray.length(); ++i) {

                JSONObject SingleMovieJson = MoviesArray.getJSONObject(i);

                MovieContext m = new MovieContext();
                        m.setID(SingleMovieJson.getInt(ID));
                        m.setTitle(SingleMovieJson.getString(TITLE));
                        m.setImageURL(SingleMovieJson.getString(POSTER_PATH));
                        m.setOverview(SingleMovieJson.getString(OVERVIEW));
                        m.setReleaseDate(SingleMovieJson.getString(RELEASE_DATE));
                        m.setRate(SingleMovieJson.getString(RATE));
                        m.setIsmoviefavaourite("NO");
                Arr[i] = m;
            }
            return Arr;
        }

        @Override
        protected MovieContext[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String MoviesJsonStr = null;

            //Press HERE API_KEY
            String API_KEY = "674437857f8bd7add004a68e85f81896";
            try {

                final String BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String KEY_PARAM = "api_key";

                Uri BuiltUri = Uri.parse(BASE_URL).buildUpon()
                        .appendEncodedPath(params[0])
                        .appendQueryParameter(KEY_PARAM, API_KEY)
                        .build();

                URL url = new URL(BuiltUri.toString());
                Log.v("uri=",BuiltUri.toString());
                // Create the request to theMovieDb, and open the connectio
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();

                // Read the input stream into a String
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
                    return null;
                }

                MoviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "JSON String = " + MoviesJsonStr);
            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {
                return getMovieDataFromJson(MoviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieContext[] movies) {

            Ma = new MovieAdapter(getActivity(), Arrays.asList(movies));
            MovieGridview.setAdapter(Ma);

        }

    }

}
