package br.org.sidia.mymovies.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.adapter.ReviewAdapter;
import br.org.sidia.mymovies.adapter.TrailerAdapter;
import br.org.sidia.mymovies.db.MovieContract;
import br.org.sidia.mymovies.db.MovieDbHelper;
import br.org.sidia.mymovies.model.Movie;
import br.org.sidia.mymovies.model.Review;
import br.org.sidia.mymovies.model.Trailer;
import br.org.sidia.mymovies.utils.Constants;
import br.org.sidia.mymovies.utils.Utils;
import static br.org.sidia.mymovies.utils.Utils.convertData;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.onRecyclerClick, ReviewAdapter.onRecyclerClick {

    TextView title, year, rated, description;
    ImageView cover;
    RecyclerView recyclerViewTrailers, recyclerViewReviews;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    CoordinatorLayout coordinatorLayout;
    Movie movie;
    LinearLayout viewVideos, viewReviews;
    CheckBox checkBox;
    boolean isChanged = false;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        title = findViewById(R.id.tx_title);
        year =  findViewById(R.id.tx_year);
        rated = findViewById(R.id.tx_rated);
        description = findViewById(R.id.tx_descri);
        cover = findViewById(R.id.img_cover);
        viewVideos = findViewById(R.id.view_videos);
        viewReviews = findViewById(R.id.view_reviews);
        checkBox = findViewById(R.id.rating_video);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        movie = bundle.getParcelable(Constants.ITEM_MOVIE);

        title.setText(movie.getTitle());
        year.setText(convertData(movie.getReleaseDate()));
        rated.setText(movie.getVoteAverage() + getString(R.string.remark));
        description.setText(movie.getOverview());
        Picasso.get().load(Constants.URL_IMAGE_BASE + movie.getPosterPath()).into(cover);

        recyclerViewTrailers = (RecyclerView) findViewById(R.id.rv_videos);
        recyclerViewReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        recyclerViewTrailers.setHasFixedSize(true);
        recyclerViewReviews.setHasFixedSize(true);
        recyclerViewTrailers.setNestedScrollingEnabled(false);
        recyclerViewReviews.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManagerReview = new LinearLayoutManager(this);
        recyclerViewTrailers.setLayoutManager(linearLayoutManagerTrailer);
        recyclerViewReviews.setLayoutManager(linearLayoutManagerReview);
        loadTrailers(movie);
        loadReviews(movie);

        checkBox.setChecked(isFavorites(movie.getId()));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addFavorites(movie);
                    isChanged = true;
                } else {
                    removeFav(movie.getId());
                    isChanged = true;
                }
            }
        });
    }


    private boolean addFavorites(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MoviesEntry.COLUMN_ID, movie.getId());
        contentValues.put(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        Uri uri = getContentResolver().insert(MovieContract.MoviesEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            isFavorites(movie.getId());
            return true;
        }

        return false;

    }

    public void removeFav(int id) {

        String stringId = Integer.toString(id);
        Uri uri = MovieContract.MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        getContentResolver().delete(uri, null, null);
    }

    public boolean isFavorites(int id) {

        Cursor cursor = getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI,
                null,
                MovieContract.MoviesEntry.COLUMN_ID + "=" + id,
                null,
                null);

        if (cursor.getCount() > 0) {
            movie.setIdStorage(cursor.getColumnIndex(MovieContract.MoviesEntry._ID));
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void loadTrailers(final Movie movie) {

        new AsyncTask<String, String, List<Trailer>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Trailer> doInBackground(String... params) {
                return Utils.getTrailersResult(params[0]);
            }

            @Override
            protected void onPostExecute(List<Trailer> result) {
                super.onPostExecute(result);

                if (result != null) {

                    trailerAdapter = new TrailerAdapter(result, DetailActivity.this, DetailActivity.this);
                    recyclerViewTrailers.setAdapter(trailerAdapter);

                    if (trailerAdapter.getItemCount() == 0) {
                        viewVideos.setVisibility(View.GONE);
                    }
                } else {
                    viewVideos.setVisibility(View.GONE);
                }
            }
        }.execute(Constants.RELATED_VIDEOS(String.valueOf(movie.getId())));

    }

    @SuppressLint("StaticFieldLeak")
    public void loadReviews(final Movie movie) {

        new AsyncTask<String, String, List<Review>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Review> doInBackground(String... params) {
                return Utils.getReviewResult(params[0]);
            }

            @Override
            protected void onPostExecute(List<Review> result) {
                super.onPostExecute(result);

                if (result != null) {

                    reviewAdapter = new ReviewAdapter(result, DetailActivity.this, DetailActivity.this);
                    recyclerViewReviews.setAdapter(reviewAdapter);

                    if (reviewAdapter.getItemCount() == 0) {
                        viewReviews.setVisibility(View.GONE);
                    }

                } else {
                    viewReviews.setVisibility(View.GONE);
                }
            }
        }.execute(Constants.USERS_REVIEWS(String.valueOf(movie.getId())));

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isChanged) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return false;
        } else {
            finish();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return;
        } else {
            finish();
        }

        super.onBackPressed();

    }

    @Override
    public void clickItem(Trailer trailer) {
        if (trailer != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.API_YOUTUBE + trailer.getKey())));
        }
    }

    @Override
    public void clickItem(Review review) {
        if (review != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
        }
    }
}
