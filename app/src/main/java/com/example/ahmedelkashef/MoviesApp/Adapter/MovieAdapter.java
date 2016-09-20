package com.example.ahmedelkashef.MoviesApp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedelkashef.MoviesApp.MovieContext;
import com.example.ahmedelkashef.MoviesApp.MovieFragment;
import com.example.ahmedelkashef.MoviesApp.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmedelkashef on 8/15/2016.
 */
public class MovieAdapter extends ArrayAdapter<MovieContext> {

    public MovieAdapter(Activity context, List<MovieContext> Movie) {
        super(context, 0, Movie);
    }
    @Override


    public View getView(int position, View convertView, ViewGroup parent) {

        MovieContext movies = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movies, parent, false);
        }

        final ImageView imgView = (ImageView) convertView.findViewById(R.id.imageViewItem);
        final String IMAGE_URL = "http://image.tmdb.org/t/p/w185" + movies.getImageURL();

        if(MovieFragment.isNetworkAvailable(this.getContext())) {
            Picasso.with(getContext()).load(IMAGE_URL)
                    .into(imgView);
        }
        else

            Picasso.with(getContext())
                    .load(IMAGE_URL)
                    .error(android.R.drawable.stat_notify_error)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imgView);


        TextView titleview = (TextView) convertView.findViewById(R.id.textViewItem);
        titleview.setText(movies.getTitle());
        return convertView;
    }

}
