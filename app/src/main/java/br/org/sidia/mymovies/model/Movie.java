package br.org.sidia.mymovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String movieId;
    private String originalTitle;
    private String poster;
    private String overview;
    private String rating;
    private String releaseDate;

    private final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
    private final String MAX_RATING = "/10";
    public static final String MOVIE_KEY = "30819113303356478821d6c6963d9b66";
    public static final String FILTER = "filter_request";

    public Movie(String movieId, String originalTitle, String poster,
                 String overview, String rating, String releaseDate) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.poster = BASE_POSTER_PATH + poster;
        this.overview = overview;
        this.rating = rating + MAX_RATING;
        this.releaseDate = releaseDate;

    }

    private Movie(Parcel input) {

        movieId = input.readString();
        originalTitle = input.readString();
        poster = input.readString();
        overview = input.readString();
        rating = input.readString();
        releaseDate = input.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(originalTitle);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
