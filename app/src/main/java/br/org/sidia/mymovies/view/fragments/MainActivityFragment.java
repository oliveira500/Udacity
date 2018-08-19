package br.org.sidia.mymovies.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.List;

import br.org.sidia.mymovies.DataBase.MoviesContract;
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

    public MovieAdapter mMovieAdapter;
    private GridView mGridView;
    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Movie> movieList;
    private String mFilterRequest;
    private Context mContext;


    private final String TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        mView = layoutInflater.inflate(R.layout.fragment_main, viewGroup, false);
        mGridView = mView.findViewById(R.id.gv_posters);
        movieList = new ArrayList<>();
        mContext = getContext();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MoviesContract.MOVIE_KEY)){
            mFilterRequest = bundle.getString(MoviesContract.MOVIE_KEY, getString(R.string.pref_sort_upcoming));
        } else {
            mFilterRequest = getString(R.string.pref_sort_upcoming);
        }

        initialize();
        return mView;
    }

    private void initialize() {
        movieList = new ArrayList<>();
        mSwipeRefreshLayout = mView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mMovieAdapter = new MovieAdapter(getActivity(), movieList);
        mGridView.setAdapter(mMovieAdapter);

        Log.v(TAG, mMovieAdapter.toString());

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(MoviesContract.MOVIE_KEY, mMovieAdapter.getItem(position));
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //handling swipe refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mMovieAdapter.notifyDataSetChanged();
                        mGridView.smoothScrollToPosition(0);
                        updateList();
                    }
                }, 2000);
            }
        });
    }

    public void updateList() {

        if (NetworkUtils.checkConnection(mContext)) {
            MovieService movieService = new MovieService(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = preferences.getString(getString(R.string.pref_key),
                    mFilterRequest);
            movieService.execute(sortBy);
        } else {
            Snackbar alertSnackbar = Snackbar.make(mView, getString(R.string.no_internet_alert), Snackbar.LENGTH_LONG);
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
    public void onPreStart() {
    }

    @Override
    public void onFinish(Object output) {
        if (output != null){
            mMovieAdapter.clear();
            ArrayList<Movie> movieList = (ArrayList<Movie>) output;
            mMovieAdapter.addAll(movieList);
        }
    }
}
