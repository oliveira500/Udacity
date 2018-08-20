package br.org.sidia.mymovies.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.org.sidia.mymovies.database.MoviesContract;
import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.model.Movie;

public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOG = DetailFragment.class.getSimpleName();
    private Movie mMovie;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(MoviesContract.MOVIE_KEY)){
            mMovie = intent.getParcelableExtra(MoviesContract.MOVIE_KEY);
        }

        Log.v(LOG, mMovie.toString());

        TextView detailTitle = rootView.findViewById(R.id.detail_title);
        TextView detailReleaseDate = rootView.findViewById(R.id.detail_releaseDate);
        TextView detailRating = rootView.findViewById(R.id.detail_rating);
        TextView detailOverview = rootView.findViewById(R.id.detail_overview);
        ImageView detailPoster = rootView.findViewById(R.id.detail_poster);

        detailTitle.setText(mMovie.getOriginalTitle());
        detailReleaseDate.setText(mMovie.getReleaseDate());
        detailRating.setText(mMovie.getRating());
        detailOverview.setText(mMovie.getOverview());
        Picasso.get()
                .load(mMovie.getPoster())
                .placeholder(R.drawable.placeholder)
                .into(detailPoster);
        return rootView;
    }
}
