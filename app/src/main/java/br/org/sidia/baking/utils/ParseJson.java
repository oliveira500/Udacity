package br.org.sidia.baking.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.org.sidia.baking.model.Ingredient;
import br.org.sidia.baking.model.Recipe;
import br.org.sidia.baking.model.Step;

public class ParseJson {

    public static Recipe[] getRecipeJson(String json) throws JSONException{

        JSONArray root = new JSONArray(json);
        Recipe[] recipes = new Recipe[root.length()];
        Log.d("teste", String.valueOf(recipes.length));

        for (int i=0; i<recipes.length; i++){

            JSONObject recipe = root.getJSONObject(i);

            int id = recipe.getInt(Attributes.ID);
            String name = recipe.getString(Attributes.NAME);

            JSONArray ingredients_result = recipe.getJSONArray(Attributes.INGREDIENTS);
            Ingredient[] ingredients = new Ingredient[ingredients_result.length()];

            for (int j=0; j<ingredients.length; j++){

                JSONObject ingredient_total = ingredients_result.getJSONObject(j);

                int quantity = ingredient_total.getInt(Attributes.QUANTITY);
                String measure = ingredient_total.getString(Attributes.MEASURE);
                String ingredient = ingredient_total.getString(Attributes.INGREDIENT);

                ingredients[j] = new Ingredient(quantity, measure, ingredient);

            }

            JSONArray steps_result = recipe.getJSONArray(Attributes.STEPS);
            Step[] steps = new Step[steps_result.length()];

            for (int j=0; j<steps.length; j++){

                JSONObject step_total = steps_result.getJSONObject(j);

                int id_step = step_total.getInt(Attributes.ID);
                String shortDescription = step_total.getString(Attributes.SHORTDESCRIPTION);
                String descrption = step_total.getString(Attributes.DESCRIPTION);
                String videoURl = step_total.getString(Attributes.VIDEOURL);
                String thumbnailURL = step_total.getString(Attributes.THUMBNAILURL);

                steps[j] = new Step(id_step, shortDescription, descrption, videoURl, thumbnailURL);

            }

            int servings = recipe.getInt(Attributes.SERVINGS);
            String image = recipe.getString(Attributes.IMAGE);

            recipes[i] = new Recipe(id, name, ingredients, steps, servings, image);

        }

        return recipes;
    }

}
