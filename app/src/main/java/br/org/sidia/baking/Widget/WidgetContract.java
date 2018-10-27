package br.org.sidia.baking.Widget;

import android.net.Uri;
import android.provider.BaseColumns;

public class WidgetContract {

    public static final String AUTHORITY = "com.example.android.baking";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_INGREDIENT = "ingredient";

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String TABLE_NAME = "ingredient";
//        public static final String INGREDIENT_ID = "ingredient_id";
        public static final String INGREDIENT_NAME = "ingredient_name";
        public static final String INGREDIENT_MEASURE = "ingredient_measure";
        public static final String INGREDIENT_QUANTITY = "ingredient_quantity";

    }

}
