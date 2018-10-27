package br.org.sidia.baking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.org.sidia.baking.Adapter.RecipeAdapter;
import br.org.sidia.baking.Model.Recipe;

public class MasterRecipeFragment extends Fragment{

    private RecipeAdapter.RecipeAdapterOnClickHandler recipeAdapterOnClickHandler;

    private Recipe[] recipes;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public MasterRecipeFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = view.findViewById(R.id.rv_recipes);

        recyclerView.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(getContext(), recipeAdapterOnClickHandler);
        recipeAdapter.setRecipes(recipes);

        recyclerView.setAdapter(recipeAdapter);

        return view;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }

    public void setRecipeAdapterOnClickHandler(RecipeAdapter.RecipeAdapterOnClickHandler recipeAdapterOnClickHandler){
        this.recipeAdapterOnClickHandler = recipeAdapterOnClickHandler;
    }

    public void setRecipesAdapter(Recipe[] recipesAdapter){
        this.recipes = recipesAdapter;
    }

}
