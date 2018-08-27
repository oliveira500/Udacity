package br.org.sidia.mymovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.model.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Trailer> trailerList;
    Context mContext;
    onRecyclerClick onRecyclerClick;

    public TrailerAdapter(List<Trailer> trailerList, Context mContext, onRecyclerClick onRecyclerClick) {
        this.trailerList = trailerList;
        this.mContext = mContext;
        this.onRecyclerClick = onRecyclerClick;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder item = (ItemHolder) holder;
            Trailer movies = trailerList.get(position);
            item.textTitle.setText(movies.getName());
        }
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public interface onRecyclerClick {
        void clickItem(Trailer pos);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageMovie;
        TextView textTitle;

        public ItemHolder(View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.image_movie);
            textTitle =  itemView.findViewById(R.id.txt_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecyclerClick.clickItem(trailerList.get(getAdapterPosition()));
        }
    }
}
