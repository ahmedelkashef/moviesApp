package com.example.ahmedelkashef.MoviesApp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ahmedelkashef.MoviesApp.Adapter.ReviewAdapter;
import com.example.ahmedelkashef.MoviesApp.Adapter.TrailerAdapter;
import com.example.ahmedelkashef.MoviesApp.data.MovieDatabaseHelper;
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
import java.util.Arrays;

/**
 * Created by ahmedelkashef on 8/18/2016.
 */

public class MovieDetailFragment extends Fragment {

    MovieContext movie;
    MovieDatabaseHelper dbFav;
    ListView TrailerList ;
    ListView ReviewsList ;
    TrailerAdapter Ta;
    ReviewAdapter Ra;
    MovieTrailer[] resultArr;
    MovieReview[] ReviewsResult;
    @Override
    public void onStart() {
        super.onStart();
        FetchMovieTrailer fetchMovieTrailer = new FetchMovieTrailer();
        fetchMovieTrailer.execute();
        FetchMovieReviews fetchMovieReviews = new FetchMovieReviews();
        fetchMovieReviews.execute();

    }

    @Nullable
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbFav = new MovieDatabaseHelper(getContext());
        setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle x = getArguments();
        if (x != null) {
            movie = (MovieContext) x.getSerializable("MovieKey");
        }


        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView imgView = (ImageView) rootView.findViewById(R.id.imageView);
        TextView title = (TextView) rootView.findViewById(R.id.title_textview);
        TextView overview = (TextView) rootView.findViewById(R.id.movie_overview_txtview);
        TextView date = (TextView) rootView.findViewById(R.id.movie_date);
        TextView rate = (TextView) rootView.findViewById(R.id.movie_rate);
        final ImageButton imgBtn = (ImageButton) rootView.findViewById(R.id.movie_favourite);
        TrailerList = (ListView) rootView.findViewById(R.id.trailer_list);
        ReviewsList = (ListView) rootView.findViewById(R.id.reviews_list);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185" + movie.getImageURL()).into(imgView);
        title.setText(movie.getTitle().toString());
        overview.setText(movie.getOverview().toString());
        date.setText(movie.getReleaseDate().toString());
        rate.setText(movie.getRate().toString() + "/10");

        if (movie.getIsmoviefavaourite().equals("NO")) {
            imgBtn.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            imgBtn.setImageResource(android.R.drawable.btn_star_big_on);
        }

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.getIsmoviefavaourite().equals("NO")) {
                    movie.setIsmoviefavaourite("YES");
                        dbFav.insert(movie);
                    imgBtn.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    movie.setIsmoviefavaourite("NO");
                    dbFav.delete(movie);
                    imgBtn.setImageResource(android.R.drawable.btn_star_big_off);
                }
            }
        });

      TrailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              MovieTrailer movieTrailer = new MovieTrailer();
              movieTrailer = resultArr[i];
              String url = "https://www." + movieTrailer.getSite() + ".com/watch?v=" + movieTrailer.getKey();
              Intent intent = new Intent(Intent.ACTION_VIEW);
              intent.setData(Uri.parse(url));
              startActivity(intent);
          }
      });
        return rootView;
    }

    public class FetchMovieTrailer extends AsyncTask<Void, Void, MovieTrailer[]> {

        private final String LOG_TAG = FetchMovieTrailer.class.getSimpleName();
        private MovieTrailer[] getTrailerFromJson(String TrailersJsonStr)
                throws JSONException {

            final String ID = "id";
            final String KEY = "key";
            final String NAME = "name";
            final String SITE = "site";

            JSONObject TrailerJson = new JSONObject(TrailersJsonStr);
            JSONArray TrailerArray = TrailerJson.getJSONArray("results");

           resultArr = new MovieTrailer[TrailerArray.length()];

            for (int i = 0; i < TrailerArray.length(); ++i) {

                JSONObject SingleMovieJsn = TrailerArray.getJSONObject(i);

                MovieTrailer mt = new MovieTrailer();
                mt.setId(SingleMovieJsn.getString(ID));
                mt.setKey(SingleMovieJsn.getString(KEY));
                mt.setName(SingleMovieJsn.getString(NAME));
                mt.setSite(SingleMovieJsn.getString(SITE));

                resultArr[i] = mt;
            }
            return resultArr;
        }


        @Override
        protected void onPostExecute(MovieTrailer[] movieTrailers) {
            Ta = new TrailerAdapter(getActivity() , Arrays.asList(movieTrailers));
            TrailerList.setAdapter(Ta);
        }

        @Override
        protected MovieTrailer[] doInBackground(Void ... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String MoviesJsonStr = null;
            String API_KEY = "674437857f8bd7add004a68e85f81896";
            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String KEY_PARAM = "api_key";
                final String ID_PARAM = Integer.toString(movie.getID());
                final String VIDEOS_PARAM = "videos";

                Uri BuiltUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(ID_PARAM)
                        .appendEncodedPath(VIDEOS_PARAM)
                        .appendQueryParameter(KEY_PARAM, API_KEY)
                        .build();
                URL url = new URL(BuiltUri.toString());
                Log.v("uri=", BuiltUri.toString());
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
                return getTrailerFromJson(MoviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class FetchMovieReviews extends AsyncTask<Void, Void,MovieReview[]>
    {
        private final String LOG_TAG = FetchMovieReviews.class.getSimpleName();
        private MovieReview[] getReviewsFromJson(String ReviewsJsonStr)
                throws JSONException {

            final String ID = "id";
            final String AUTHOR = "author";
            final String CONTENT = "content";
            final String URL = "url";

            JSONObject ReviewJson = new JSONObject(ReviewsJsonStr);
            JSONArray ReviewArray = ReviewJson.getJSONArray("results");

             ReviewsResult = new MovieReview[ReviewArray.length()];

            for (int i = 0; i < ReviewArray.length(); ++i) {

                JSONObject SingleMovieJsn = ReviewArray.getJSONObject(i);

                MovieReview mr = new MovieReview();
                mr.setId(SingleMovieJsn.getString(ID));
                mr.setAuthor(SingleMovieJsn.getString(AUTHOR));
                mr.setContent(SingleMovieJsn.getString(CONTENT));
                mr.setURL(SingleMovieJsn.getString(URL));

                ReviewsResult[i] = mr;
            }
            return ReviewsResult;
        }
        @Override
        protected void onPostExecute(MovieReview[] movieReviews) {
            Ra = new ReviewAdapter(getActivity(),Arrays.asList(movieReviews));
            ReviewsList.setAdapter(Ra);
        }

        @Override
        protected MovieReview[] doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String MoviesJsonStr = null;

            //Press HERE API_KEY
            String API_KEY = "674437857f8bd7add004a68e85f81896";

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String KEY_PARAM = "api_key";
               final String ID_PARAM = Integer.toString(movie.getID());
                final String REVIEWS_PARAM = "reviews";

                Uri BuiltUri = null;

                    BuiltUri = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(ID_PARAM)
                            .appendEncodedPath(REVIEWS_PARAM)
                            .appendQueryParameter(KEY_PARAM, API_KEY)
                            .build();

                URL url = new URL(BuiltUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
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
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getReviewsFromJson(MoviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
