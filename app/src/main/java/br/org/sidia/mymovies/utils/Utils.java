package br.org.sidia.mymovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import br.org.sidia.mymovies.model.Movie;
import br.org.sidia.mymovies.model.Review;
import br.org.sidia.mymovies.model.Trailer;

public class Utils {

    public static List<Movie> getPageResult(String urlMovie) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        StringBuilder result = new StringBuilder();
        List<Movie> movies = new ArrayList<>();

        try {
            url = new URL(urlMovie);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            try {
                JSONObject pageResult = new JSONObject(result.toString());
                JSONArray moviesResult = pageResult.getJSONArray(Constants.ApiKeysMovie.RESULTS);

                for (int i = 0; i < moviesResult.length(); i++) {
                    JSONObject movieJson = moviesResult.getJSONObject(i);
                    int id = movieJson.getInt(Constants.ApiKeysMovie.ID);
                    double vote = movieJson.getDouble(Constants.ApiKeysMovie.VOTE_AVERAGE);
                    String title = movieJson.getString(Constants.ApiKeysMovie.TITLE);
                    String original_title = movieJson.getString(Constants.ApiKeysMovie.ORIGINAL_TITLE);
                    String posterPath = movieJson.getString(Constants.ApiKeysMovie.POSTER_PATH);
                    String backdropPath = movieJson.getString(Constants.ApiKeysMovie.POSTER_PATH);
                    String overview = movieJson.getString(Constants.ApiKeysMovie.OVERVIEW);
                    String releaseDate = movieJson.getString(Constants.ApiKeysMovie.RELEASE_DATE);
                    movies.add(new Movie(-1, id, String.valueOf(vote), title, original_title, posterPath, backdropPath, overview, releaseDate));
                }

                return movies;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Trailer> getTrailersResult(String urlMovie) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        StringBuilder result = new StringBuilder();
        List<Trailer> resultMovies;

        try {
            url = new URL(urlMovie);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            try {
                JSONObject pageResult = new JSONObject(result.toString());
                resultMovies = new ArrayList<>();
                JSONArray moviesResult = pageResult.getJSONArray(Constants.ApiKeysTrailer.RESULTS);
                for (int i = 0; i < moviesResult.length(); i++) {
                    JSONObject trailerJson = moviesResult.getJSONObject(i);
                    String id = trailerJson.getString(Constants.ApiKeysTrailer.ID);
                    String key = trailerJson.getString(Constants.ApiKeysTrailer.KEY);
                    String name = trailerJson.getString(Constants.ApiKeysTrailer.NAME);
                    String site = trailerJson.getString(Constants.ApiKeysTrailer.SITE);
                    resultMovies.add(new Trailer(id, key, name, site));
                }

                return resultMovies;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Review> getReviewResult(String urlMovie) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        StringBuilder result = new StringBuilder();
        List<Review> resultReview;

        try {
            url = new URL(urlMovie);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            try {
                JSONObject pageResult = new JSONObject(result.toString());
                resultReview = new ArrayList<>();
                JSONArray moviesResult = pageResult.getJSONArray(Constants.ApiKeysReview.RESULTS);
                for (int i = 0; i < moviesResult.length(); i++) {
                    JSONObject trailerJson = moviesResult.getJSONObject(i);
                    String id = trailerJson.getString(Constants.ApiKeysReview.ID);
                    String author = trailerJson.getString(Constants.ApiKeysReview.AUTHOR);
                    String content = trailerJson.getString(Constants.ApiKeysReview.CONTENT);
                    String urlReview = trailerJson.getString(Constants.ApiKeysReview.URL);
                    resultReview.add(new Review(id, author, content, urlReview));
                }

                return resultReview;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertData(String data) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy");
        try {
            String reformattedStr = myFormat.format(fromUser.parse(data));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static boolean checkConnection(final Context context) {
        boolean isConected = false;

        final ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo mobileNetworkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        final NetworkInfo wifiNetworkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ((wifiNetworkInfo != null && wifiNetworkInfo.isAvailable() && wifiNetworkInfo
                .isConnected())
                || (mobileNetworkInfo != null
                && mobileNetworkInfo.isAvailable() && mobileNetworkInfo
                .isConnected())) {
            isConected = true;
        } else {

            isConected = false;
        }
        return isConected;
    }
}
