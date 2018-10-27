package br.org.sidia.baking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.org.sidia.baking.R;

import br.org.sidia.baking.Model.Recipe;
import br.org.sidia.baking.Utils.Network;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder>{

    private Recipe[] recipes;
    private Context context;
    private RecipeAdapterOnClickHandler recipeAdapterOnClickHandler;

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler recipeAdapterOnClickHandler){
        this.context = context;
        this.recipeAdapterOnClickHandler = recipeAdapterOnClickHandler;

    }

    public interface RecipeAdapterOnClickHandler{
        void onClick(Recipe recipe);
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        holder.txtCardName.setText(recipes[position].getName());
        holder.txtCardServing.setText(String.valueOf(recipes[position].getServings()));

        if (!recipes[position].getImage().isEmpty())
            Network.loadImageMovie(context, recipes[position].getImage(), holder.img_recipe);
    }

    @Override
    public int getItemCount() {
        if (recipes == null)
            return 0;

        return recipes.length;
    }



    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtCardName;
        private TextView txtCardServing;
        private ImageView img_recipe;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            txtCardName = itemView.findViewById(R.id.txt_card_name);
            txtCardServing = itemView.findViewById(R.id.txt_card_serving);
            img_recipe = itemView.findViewById(R.id.img_recipe);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            Recipe recipe = recipes[id];
            recipeAdapterOnClickHandler.onClick(recipe);
        }
    }

    public void setRecipes(Recipe[] recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    // Load the movie poster according the path
//    public static void loadImageMovie(Context context, String pathImage, ImageView imageView){
//        Picasso.with(context).load(pathImage).into(imageView);
//    }

}
