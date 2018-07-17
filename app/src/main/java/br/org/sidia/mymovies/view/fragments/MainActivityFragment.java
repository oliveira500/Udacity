package br.org.sidia.mymovies.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.interfaces.AsyncTaskDelegate;
import br.org.sidia.mymovies.model.Movie;
import br.org.sidia.mymovies.services.MovieService;
import br.org.sidia.mymovies.utils.NetworkUtils;
import br.org.sidia.mymovies.view.activities.DetailActivity;
import br.org.sidia.mymovies.view.view.adapters.MovieAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AsyncTaskDelegate{

/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }*/

    public MovieAdapter mMovieAdapter;

    private GridView mGridView;
    private ProgressBar mLoadingBar;


    private final String TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = view.findViewById(R.id.gv_posters);
        mLoadingBar = view.findViewById(R.id.pb_loading);

        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        mGridView.setAdapter(mMovieAdapter);

        Log.v(TAG, mMovieAdapter.toString());

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Movie.MOVIE_KEY, mMovieAdapter.getItem(position));
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateList() {

        if (NetworkUtils.hasInternetConnection(getContext())) {
            MovieService downloadMovieData = new MovieService(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = preferences.getString(getString(R.string.pref_key),
                    getString(R.string.pref_sort_popular));
            downloadMovieData.execute(sortBy);
        } else {
            View view = getActivity().findViewById(R.id.container);
            Snackbar alertSnackbar = Snackbar.make(view, getString(R.string.no_internet_alert), Snackbar.LENGTH_LONG);
            alertSnackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateList();
                }
            });
            alertSnackbar.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPreStart() {
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinish(Object output) {
        mLoadingBar.setVisibility(View.INVISIBLE);
        if (output != null){
            mMovieAdapter.clear();
            ArrayList<Movie> movieList = (ArrayList<Movie>) output;
            mMovieAdapter.addAll(movieList);
        }
    }
}
