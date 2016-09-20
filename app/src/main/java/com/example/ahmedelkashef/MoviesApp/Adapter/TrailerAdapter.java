package com.example.ahmedelkashef.MoviesApp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ahmedelkashef.MoviesApp.MovieTrailer;
import com.example.ahmedelkashef.MoviesApp.R;

import java.util.List;

/**
 * Created by ahmedelkashef on 9/2/2016.
 */
public class TrailerAdapter extends ArrayAdapter<MovieTrailer> {

    public TrailerAdapter(Activity context, List<MovieTrailer> moviesTrailer) {
        super(context, 0, moviesTrailer);
    }
    @Override


    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MovieTrailer moviesTrailer = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_layout, parent, false);
        }

        TextView Trailerview = (TextView) convertView.findViewById(R.id.textView_trailer);
        Trailerview.setText(moviesTrailer.getName());
        return convertView;
    }

}
