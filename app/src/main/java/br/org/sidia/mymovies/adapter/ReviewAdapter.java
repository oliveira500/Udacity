package br.org.sidia.mymovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Review> reviewList;
    Context mContext;
    onRecyclerClick onRecyclerClick;

    public ReviewAdapter(List<Review> reviewList, Context context, onRecyclerClick onRecyclerClick) {
        this.reviewList = reviewList;
        this.mContext = context;
        this.onRecyclerClick = onRecyclerClick;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder item = (ItemHolder) holder;
            Review movies = reviewList.get(position);
            item.textTitle.setText(movies.getAuthor());
            item.textContent.setText(movies.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public interface onRecyclerClick {
        void clickItem(Review pos);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle;
        TextView textContent;

        public ItemHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.txt_author);
            textContent = itemView.findViewById(R.id.txt_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecyclerClick.clickItem(reviewList.get(getAdapterPosition()));
        }
    }
}
