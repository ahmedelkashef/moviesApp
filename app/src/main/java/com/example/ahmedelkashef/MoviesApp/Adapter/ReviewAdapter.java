package com.example.ahmedelkashef.MoviesApp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ahmedelkashef.MoviesApp.MovieReview;
import com.example.ahmedelkashef.MoviesApp.R;

import java.util.List;

/**
 * Created by ahmedelkashef on 9/5/2016.
 */
public class ReviewAdapter extends ArrayAdapter<MovieReview> {

    public ReviewAdapter(Activity context, List<MovieReview> movieReview) {
        super(context, 0, movieReview);
        }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MovieReview movieReview = getItem(position);


        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_layout, parent, false);
        }

        TextView Author  = (TextView) convertView.findViewById(R.id.textView_r1);
        Author.setText(movieReview.getAuthor());

    TextView Content  = (TextView) convertView.findViewById(R.id.textView_r2);
    Content.setText(movieReview.getContent());
        return convertView;
        }

}
