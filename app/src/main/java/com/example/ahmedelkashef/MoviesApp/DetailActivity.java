package com.example.ahmedelkashef.MoviesApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by ahmedelkashef on 8/18/2016.
 */
public class DetailActivity extends AppCompatActivity {

    MovieContext movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle b = new Bundle();
        Intent intent = getIntent();
        movie = (MovieContext) intent.getSerializableExtra("MovieKey");
        b.putSerializable("MovieKey", movie);
        fragment.setArguments(b);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.Detail_container, fragment)
                .commit();
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
}
