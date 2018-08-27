package br.org.sidia.mymovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;
import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.model.Movie;
import br.org.sidia.mymovies.utils.Constants;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Movie> result;
    Context mContext;
    onRecyclerClick onRecyclerClick;

    public MovieAdapter(List<Movie> movies, Context mContext, onRecyclerClick onRecyclerClick) {
        this.result = movies;
        this.mContext = mContext;
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder item = (ItemHolder) holder;
            Movie movie = result.get(position);
            Picasso.get().load(Constants.URL_IMAGE_BASE + movie.getPosterPath()).into(item.imageMovie);
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public interface onRecyclerClick {
        void clickItem(Movie pos);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageMovie;

        public ItemHolder(View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.image_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerClick.clickItem(result.get(getAdapterPosition()));
        }
    }
}
