package br.org.sidia.baking;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.org.sidia.baking.adapter.StepAdapter;
import br.org.sidia.baking.model.Ingredient;
import br.org.sidia.baking.model.Recipe;
import br.org.sidia.baking.model.Step;
import br.org.sidia.baking.widget.WidgetContract;

public class RecipeStepActivity extends AppCompatActivity implements StepAdapter.StepAdapterOnClickHandler {

    private static final String KEY_TABLET = "tablet_key";
    private static final String KEY_INTENT = "recipe_key";

    private Recipe recipe;
    private boolean isTablet;
    private static final String FRAGMENT_STATUS = "frag_status";
    private static final String STATUS = "status";

    private MasterRecipeStepDetailFragment masterRecipeStepDetailFragment;
    MasterRecipeStepFragment masterRecipeStepFragment = new MasterRecipeStepFragment();
    private boolean statusRecipeDetailFragment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null){
            if (getIntent().hasExtra(KEY_INTENT) && getIntent().hasExtra(KEY_TABLET)){
                recipe = (Recipe) getIntent().getSerializableExtra(KEY_INTENT);
                isTablet = getIntent().getBooleanExtra(KEY_TABLET, false);

                actionBar.setTitle(recipe.getName());

                if (savedInstanceState != null) {
                    if (savedInstanceState.getBoolean(STATUS)) {
                        masterRecipeStepDetailFragment = (MasterRecipeStepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_STATUS);
                        createRecipeStepDetailFragment(masterRecipeStepDetailFragment);
                    } else {
                        masterRecipeStepFragment = (MasterRecipeStepFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_STATUS);
                        createRecipeStepFrament(masterRecipeStepFragment);
                    }

                } else {
                    createRecipeStepFrament(masterRecipeStepFragment);
                }
            }
        }

    }

    @Override
    public void onClick(Step step) {

        statusRecipeDetailFragment = true;
        masterRecipeStepDetailFragment = new MasterRecipeStepDetailFragment();
        masterRecipeStepDetailFragment.setStep(step);
        masterRecipeStepDetailFragment.setStatusClickIngredient(false);

        createRecipeStepDetailFragment(masterRecipeStepDetailFragment);
    }

    public void clickIngredients(View view){

        statusRecipeDetailFragment = true;
        masterRecipeStepDetailFragment = new MasterRecipeStepDetailFragment();
        masterRecipeStepDetailFragment.setIngredient(recipe.getIngredients());
        masterRecipeStepDetailFragment.setStatusClickIngredient(true);

        createRecipeStepDetailFragment(masterRecipeStepDetailFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ingredient_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            statusRecipeDetailFragment = false;
            onBackPressed();
            createRecipeStepFrament(masterRecipeStepFragment);
        }

        if (id == R.id.menu_info) {
            setIngredientsDatabase(recipe.getIngredients());
        }

        return super.onOptionsItemSelected(item);
    }

    public void setIngredientsDatabase(Ingredient[] ingredients){

        getContentResolver().delete(WidgetContract.IngredientEntry.CONTENT_URI, null, null);

        for (int i=0; i<ingredients.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WidgetContract.IngredientEntry.INGREDIENT_NAME, ingredients[i].getIngredient());
            contentValues.put(WidgetContract.IngredientEntry.INGREDIENT_MEASURE, ingredients[i].getMeasure());
            contentValues.put(WidgetContract.IngredientEntry.INGREDIENT_QUANTITY, ingredients[i].getQuantity());

            getContentResolver().insert(WidgetContract.IngredientEntry.CONTENT_URI, contentValues);

        }

        Toast.makeText(this, getString(R.string.toast_info_widget), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (statusRecipeDetailFragment) {
            getSupportFragmentManager().putFragment(outState, FRAGMENT_STATUS, masterRecipeStepDetailFragment);
        } else {
            getSupportFragmentManager().putFragment(outState, FRAGMENT_STATUS, masterRecipeStepFragment);
        }

        outState.putBoolean(STATUS, statusRecipeDetailFragment);
    }

    public void createRecipeStepFrament(MasterRecipeStepFragment masterRecipeStepFragment){
        statusRecipeDetailFragment = false;
        masterRecipeStepFragment.setStepAdapterOnClickHandler(this);
        masterRecipeStepFragment.setSteps(recipe.getSteps());
        masterRecipeStepFragment.setIngredients(recipe.getIngredients());

        if (masterRecipeStepFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_recipe_step, masterRecipeStepFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.container_recipe_step, masterRecipeStepFragment).commit();
        }
    }

    public void createRecipeStepDetailFragment(MasterRecipeStepDetailFragment masterRecipeStepDetail){

        if (isTablet){
            createRecipeStepFrament(masterRecipeStepFragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_recipe_step_detail_tablet, masterRecipeStepDetail).commit();
        }else {
            if (masterRecipeStepDetail.isAdded()) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_recipe_step, masterRecipeStepDetail).commit();
            } else
                getSupportFragmentManager().beginTransaction().add(R.id.container_recipe_step, masterRecipeStepDetail).addToBackStack(null).commit();
        }
    }
}
