package br.org.sidia.mymovies.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.StringDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.adapter.MovieAdapter;
import br.org.sidia.mymovies.db.MovieContract;
import br.org.sidia.mymovies.model.Movie;
import br.org.sidia.mymovies.utils.Constants;
import br.org.sidia.mymovies.utils.Utils;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainActivity extends AppCompatActivity implements MovieAdapter.onRecyclerClick {

    public static final String MOST_POPULAR = "most";
    public static final String TOP_RATED = "top";
    public static final String FAVORITE = "fav";
    private static final int REQUEST_CODE = 182;
    RecyclerView recyclerView;
    MovieAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    List<Movie> movies = new ArrayList<>();
    @StateMovie
    String state = MOST_POPULAR;
    GridLayoutManager grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list_items);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(grid);
        adapter = new MovieAdapter(movies, MainActivity.this, MainActivity.this);
        recyclerView.setAdapter(adapter);
        loadMovies(Constants.MOST_POPULAR);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INSTANCE_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle saved) {
        super.onRestoreInstanceState(saved);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Parcelable savedState = saved.getParcelable(Constants.INSTANCE_STATE);
                grid.onRestoreInstanceState(savedState);
            }
        }, Constants.DELAY_INSTANCE);
    }

    public void loadMoviesFavorites() {
        adapter.clear();

        Cursor cursor = getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                adapter.add(new Movie(
                        0,
                        cursor.getInt(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_ORIGINAL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_POSTER_PATH)),
                        null,
                        cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE))
                ));
            }
        }

        if (adapter.getItemCount() == 0) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.erro_list_favorites), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.yes), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setCurrentState(MOST_POPULAR);
                            loadMovies(Constants.MOST_POPULAR);
                        }
                    });

            snackbar.show();
        } else {
            updateTitle();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void loadMovies(String order) {

        adapter.clear();

        new AsyncTask<String, String, List<Movie>>() {

            ProgressBar pb;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pb = (ProgressBar) findViewById(R.id.pd);
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<Movie> doInBackground(String... params) {
                return Utils.getPageResult(params[0]);
            }

            @Override
            protected void onPostExecute(List<Movie> list) {
                super.onPostExecute(list);
                pb.setVisibility(View.GONE);
                if (list != null) {
                    adapter.add(list);
                    updateTitle();
                } else if (!Utils.checkConnection(getApplicationContext())){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.no_internet_alert), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.yes), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setCurrentState(MOST_POPULAR);
                                    loadMovies(Constants.MOST_POPULAR);
                                }
                            });

                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.erro_list_load), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.yes), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setCurrentState(MOST_POPULAR);
                                    loadMovies(Constants.MOST_POPULAR);
                                }
                            });

                    snackbar.show();
                }
            }
        }.execute(order);
    }

    private void updateTitle() {
        if (state == MOST_POPULAR){
            setTitle(R.string.menu_most_popular);
        } else if (state == TOP_RATED){
            setTitle(R.string.menu_top_rated);
        } else {
            setTitle(R.string.menu_favorites);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mostpopular) {
            setCurrentState(MOST_POPULAR);
            loadMovies(Constants.MOST_POPULAR);
        } else if (item.getItemId() == R.id.bestrate) {
            setCurrentState(TOP_RATED);
            loadMovies(Constants.TOP_RATED);
        } else if (item.getItemId() == R.id.favorite) {
            setCurrentState(FAVORITE);
            loadMoviesFavorites();
            setTitle(R.string.menu_favorites);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickItem(Movie movie) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(Constants.ITEM_MOVIE, movie);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (getCurrentState() == MOST_POPULAR) {
                loadMovies(Constants.MOST_POPULAR);
            } else if (getCurrentState() == TOP_RATED) {
                loadMovies(Constants.TOP_RATED);
            } else {
                loadMoviesFavorites();
            }
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @StateMovie
    public String getCurrentState() {
        return state;
    }

    public void setCurrentState(@StateMovie String state) {
        this.state = state;
    }

    @Retention(SOURCE)
    @StringDef({
            MOST_POPULAR,
            TOP_RATED,
            FAVORITE
    })
    public @interface StateMovie {
    }
}
