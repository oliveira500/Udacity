package br.org.sidia.mymovies.utils;

import br.org.sidia.mymovies.BuildConfig;

public class Constants {

    public static final String API_KEY = BuildConfig.MOVIEDB_API_KEY;
    public static final String MOST_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    public static final String TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    public static final String URL_IMAGE_BASE = "http://image.tmdb.org/t/p/w185//";
    public static final String ITEM_MOVIE = "item_movie";
    public static final String API_YOUTUBE = "http://www.youtube.com/watch?v=";
    public static final String MOVIES_SAVED_INSTANCE_LIST_KEY = "array_of_movies";
    public static final String MOVIES_SAVED_INSTANCE_TITLE = "tile_list";

    public static String RELATED_VIDEOS(String videoId) {
        return "http://api.themoviedb.org/3/movie/" + videoId + "/videos?api_key=" + API_KEY;
    }

    public static String USERS_REVIEWS(String videoId) {
        return "http://api.themoviedb.org/3/movie/" + videoId + "/reviews?api_key=" + API_KEY;
    }

    public static class ApiKeysMovie {

        public static final String RESULTS = "results";
        public static final String ID = "id";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String TITLE = "title";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";

    }

    public static class ApiKeysTrailer {

        public static final String RESULTS = "results";
        public static final String ID = "id";
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String SITE = "site";

    }

    public static class ApiKeysReview {

        public static final String RESULTS = "results";
        public static final String ID = "id";
        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String URL = "url";

    }

    public static final int DELAY_INSTANCE = 200;
    public static final String INSTANCE_STATE = "state";

}
