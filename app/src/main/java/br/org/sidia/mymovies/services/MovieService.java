package br.org.sidia.mymovies.services;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;

import br.org.sidia.mymovies.interfaces.AsyncTaskDelegate;
import br.org.sidia.mymovies.model.Movie;
import br.org.sidia.mymovies.utils.MovieJsonUtils;
import br.org.sidia.mymovies.utils.NetworkUtils;

public class MovieService extends AsyncTask<String, Void, ArrayList<Movie>> {

    //private final String TAG = MovieService.class.getSimpleName();
    private AsyncTaskDelegate delegatedTask;

    public MovieService(AsyncTaskDelegate responder) {
        this.delegatedTask = responder;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        delegatedTask.onPreStart();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        String orderPref = params[0];
        URL requestUrl = NetworkUtils.createAndUpdateUrl(orderPref);

        try {
            String jsonResponse =
                    NetworkUtils.getResponseFromHttpUrl(requestUrl);

            return MovieJsonUtils.getMovieDataFromJson(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movieList) {
        if (movieList != null) {
            delegatedTask.onFinish(movieList);
        }
    }
}
