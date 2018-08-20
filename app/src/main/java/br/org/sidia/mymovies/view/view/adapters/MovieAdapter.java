package br.org.sidia.mymovies.view.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> result;
    private Context context;
    private onRecyclerClick onRecyclerClick;
    private ImageView imageView;

    public interface onRecyclerClick {
        void clickItem(Movie possition);
    }
    public MovieAdapter(List<Movie> movies, Context context, onRecyclerClick onRecyclerClick) {
        this.result = movies;
        this.context = context;
        this.onRecyclerClick = onRecyclerClick;
        notifyDataSetChanged();
    }

    public void clear() {
        if (result != null) {
            result.clear();
            notifyDataSetChanged();
        }
    }

    public void add(Movie movie) {
        result.add(movie);
        notifyDataSetChanged();
    }

    public void add(List<Movie> movie) {
        result.addAll(movie);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        this.imageView = view.findViewById(R.id.iv_poster);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            MovieViewHolder item = (MovieViewHolder) holder;
            Movie movie = result.get(position);
            //Picasso.with(context).load(URL_IMAGE_BASE + movies.getPosterPath()).into(item.imageMovie);
            Picasso.get()
                    .load(movie.getPoster())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }


//    private final String TAG = MovieAdapter.class.getSimpleName();
//
//    public MovieAdapter(Context context, List<Movie> objects) {
//        super(context, 0, objects);
//    }
//
//
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if (convertView == null){
//            convertView = LayoutInflater.from(getContext())
//                    .inflate(R.layout.movie_list_item, parent, false);
//        }
//
//        Movie movie = getItem(position);
//
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_poster);
//
//        Picasso.get()
//                .load(movie.getPoster())
//                .placeholder(R.drawable.placeholder)
//                .into(imageView);
//
//        return convertView;
//    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerClick.clickItem(result.get(getAdapterPosition()));
        }
    }
}
