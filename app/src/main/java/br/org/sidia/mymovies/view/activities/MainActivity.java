package br.org.sidia.mymovies.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.model.Movie;
import br.org.sidia.mymovies.view.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.pref_sort_label_upcoming_movies));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        MainActivityFragment fragment = new MainActivityFragment();
        fragmentManager = getSupportFragmentManager();
        if (item.getItemId() == R.id.most_popular) {
            setTitle(getString(R.string.pref_sort_label_most_popular));
            bundle.putString(Movie.MOVIE_KEY, getString(R.string.pref_sort_popular));
        } else if (item.getItemId() == R.id.top_rated) {
            setTitle(getString(R.string.pref_sort_label_top_rated));
            bundle.putString(Movie.MOVIE_KEY, getString(R.string.pref_sort_top_rated));
        }
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();

        return super.onOptionsItemSelected(item);
    }

}
