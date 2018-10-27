package br.org.sidia.baking.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.org.sidia.baking.Model.Ingredient;
import br.org.sidia.baking.R;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder>{

    private Ingredient[] ingredients;
    private Context context;

    public IngredientAdapter(Context context){
        this.context = context;
    }

    public interface IngredientAdapterClickHandler{
        void onClick(Ingredient ingredient);
    }

    @NonNull
    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_list_item, parent, false);

        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterViewHolder holder, int position) {
        holder.txt_ingredient.setText(ingredients[position].getIngredient());
        holder.txt_measure.setText(ingredients[position].getMeasure());
        holder.txt_quantity.setText(String.valueOf(ingredients[position].getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (ingredients == null)
            return 0;

        return ingredients.length;
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txt_ingredient;
        private TextView txt_measure;
        private TextView txt_quantity;

        public IngredientAdapterViewHolder(View itemView) {
            super(itemView);
            txt_ingredient = itemView.findViewById(R.id.txt_ingredient);
            txt_measure = itemView.findViewById(R.id.txt_measure);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            Ingredient ingredient = ingredients[id];
        }
    }

    public void setIngredients(Ingredient[] ingredients){
        this.ingredients = ingredients;
    }

}
