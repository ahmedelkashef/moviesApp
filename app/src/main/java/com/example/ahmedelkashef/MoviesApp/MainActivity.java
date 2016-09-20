package com.example.ahmedelkashef.MoviesApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity  extends ActionBarActivity implements MovieFragment.MovieCallback{

boolean mTwoPane ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.Detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fragment_menu, menu);
        return true;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(MovieContext movie) {

        if (mTwoPane) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle b = new Bundle();
            b.putSerializable("MovieKey", movie);
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction().replace(R.id.Detail_container, fragment).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);


            Bundle b = new Bundle();
            b.putSerializable("MovieKey", movie);
            intent.putExtras(b);
            startActivity(intent);


        }
    }
}

