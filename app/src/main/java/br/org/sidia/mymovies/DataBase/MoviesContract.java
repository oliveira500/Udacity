package br.org.sidia.mymovies.DataBase;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public static final String AUTHORITY = "fabioliveira.mymovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "mymovies";
    public static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
    public static final String MAX_RATING = "/10";
    public static final String MOVIE_KEY = "movie_key";

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";

    }


}
