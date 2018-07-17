package br.org.sidia.mymovies.view.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.model.Movie;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, List<Movie> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
        }

        Movie movie = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_poster);

        Picasso.get()
                .load(movie.getPoster())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        return convertView;
    }
}
