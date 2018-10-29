package br.org.sidia.baking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.org.sidia.baking.R;

import br.org.sidia.baking.model.Step;
import br.org.sidia.baking.utils.Network;
import br.org.sidia.baking.utils.ThumbnailRetriever;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder>{

    private Step[] steps;
    private Context context;
    private StepAdapterOnClickHandler stepAdapterOnClickHandler;

    public StepAdapter(Context context, StepAdapterOnClickHandler stepAdapterOnClickHandler){
        this.context = context;
        this.stepAdapterOnClickHandler = stepAdapterOnClickHandler;
    }

    public interface StepAdapterOnClickHandler{
        void onClick(Step step);
    }

    @Override
    public StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_step_list_item, parent, false);

        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapterViewHolder holder, int position) {
        holder.txt_card_shortDescription.setText(steps[position].getShortDescription());

        if (!steps[position].getThumbnailURL().isEmpty())
            getThumbnail(steps[position].getThumbnailURL(), holder.img_card_thumbnail);
//            loadThumb(steps[position].getThumbnailURL(), holder.img_card_thumbnail);
    }

    @Override
    public int getItemCount() {
        if (steps == null)
            return 0;

        return steps.length;
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txt_card_shortDescription;
        private ImageView img_card_thumbnail;
        private TextView txt_card_description;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            txt_card_shortDescription = itemView.findViewById(R.id.txt_card_shortDescription_step);
            img_card_thumbnail = itemView.findViewById(R.id.img_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            Step step = steps[id];
            stepAdapterOnClickHandler.onClick(step);
        }
    }

    public void setSteps(Step[] steps){
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void loadThumb(String path, ImageView imageView){
        if (!path.isEmpty()){
            Network.loadImageMovie(context, path, imageView);
        }
    }

    public static void getThumbnail(String path, ImageView imageView){
        try {
            Bitmap bitmap = ThumbnailRetriever.getThumbnail(path);

            if (bitmap != null){
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                imageView.setImageBitmap(bitmap);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


}
