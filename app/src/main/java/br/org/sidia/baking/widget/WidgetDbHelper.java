package br.org.sidia.baking.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.org.sidia.baking.widget.WidgetContract.IngredientEntry;

public class WidgetDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "ingredient.db";
    private static final int DATABSE_VERSION = 2;

    public WidgetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_INGREDIENT_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IngredientEntry.INGREDIENT_NAME + " TEXT NOT NULL, " +
                IngredientEntry.INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                IngredientEntry.INGREDIENT_QUANTITY + " INTEGER NOT NULL)";

        db.execSQL(SQL_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(db);
    }
}
