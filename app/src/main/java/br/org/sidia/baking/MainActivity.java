package br.org.sidia.baking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import br.org.sidia.baking.adapter.RecipeAdapter;
import br.org.sidia.baking.model.Recipe;
import br.org.sidia.baking.utils.Network;
import br.org.sidia.baking.utils.ParseJson;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Recipe[]>{

    private boolean isTablet;
    private MasterRecipeFragment masterRecipeFragment;
    private static final int LOADER_ID = 01;

    private TextView txt_noInternet;
    private ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_noInternet = findViewById(R.id.txt_noInternet_main);
        pb_loading = findViewById(R.id.pb_loading);

        masterRecipeFragment = new MasterRecipeFragment();
        masterRecipeFragment.setRecipeAdapterOnClickHandler(this);

        loadData();

    }


    @Override
    public void onClick(Recipe recipe) {
        String KEY_INTENT = "recipe_key";
        String KEY_TABLET = "tablet_key";
        Intent intent = new Intent(this, RecipeStepActivity.class);
        intent.putExtra(KEY_INTENT, recipe);
        intent.putExtra(KEY_TABLET, isTablet);
        startActivity(intent);
    }

    public void loadData(){
        if (Network.verifyConnection(this)){

            txt_noInternet.setVisibility(View.GONE);
            pb_loading.setVisibility(View.VISIBLE);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Recipe[]> movieLoader = loaderManager.getLoader(LOADER_ID);

            if (movieLoader == null)
                loaderManager.initLoader(LOADER_ID, null, this);
            else
                loaderManager.restartLoader(LOADER_ID, null, this);

        } else {

            txt_noInternet.setVisibility(View.VISIBLE);
            pb_loading.setVisibility(View.GONE);

        }
    }

    @Override
    public Loader<Recipe[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Recipe[]>(this) {

            Recipe[] recipeJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (recipeJson != null)
                    deliverResult(recipeJson);
                else
                    forceLoad();
            }

            @Override
            public Recipe[] loadInBackground() {

                URL recipe_url = Network.buildUrl();

                try{

                    String recipeResponseURL = Network.getResponseFromHttpUrl(recipe_url);

                    if (recipeResponseURL != null){
                        Recipe[] recipes = ParseJson.getRecipeJson(recipeResponseURL);
                        return recipes;
                    }

                } catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Recipe[]> loader, Recipe[] data) {

        if (data != null) {

            txt_noInternet.setVisibility(View.GONE);
            pb_loading.setVisibility(View.GONE);

            FragmentManager fragmentManager = getSupportFragmentManager();
            masterRecipeFragment.setRecipesAdapter(data);

            if (findViewById(R.id.container_main_tablet) != null) {
                isTablet = true;
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
                masterRecipeFragment.setLayoutManager(gridLayoutManager);

                if (masterRecipeFragment.isAdded())
                    fragmentManager.beginTransaction().replace(R.id.container_main_tablet, masterRecipeFragment).commit();
                else
                    fragmentManager.beginTransaction().add(R.id.container_main_tablet, masterRecipeFragment).commit();

            } else {
                isTablet = false;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                masterRecipeFragment.setLayoutManager(linearLayoutManager);

                if (masterRecipeFragment.isAdded())
                    fragmentManager.beginTransaction().replace(R.id.container_main, masterRecipeFragment).commit();
                else
                    fragmentManager.beginTransaction().add(R.id.container_main, masterRecipeFragment).commit();
            }

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Recipe[]> loader) {

    }


}
