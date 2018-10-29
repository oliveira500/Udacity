package br.org.sidia.baking.Widget;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.org.sidia.baking.Widget.WidgetContract.IngredientEntry;

public class WidgetProvider extends ContentProvider{

    public static final int INGREDIENT = 100;
    public static final int INGREDIENT_ID = 101;

    private static final String TAG = WidgetProvider.class.getName();

    private WidgetDbHelper widgetDbHelper;
    private UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(WidgetContract.AUTHORITY, WidgetContract.PATH_INGREDIENT, INGREDIENT);
        uriMatcher.addURI(WidgetContract.AUTHORITY, WidgetContract.PATH_INGREDIENT + "/#", INGREDIENT_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        widgetDbHelper = new WidgetDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = widgetDbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor cursor;

        switch (match){
            case INGREDIENT:
                cursor = db.query(IngredientEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case INGREDIENT_ID:
                String id = uri.getPathSegments().get(1);
                cursor = db.query(IngredientEntry.TABLE_NAME, projection, "_id=?", new String[]{id}, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = widgetDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri uriMatch;

        switch (match){
            case INGREDIENT:
                long id = db.insert(IngredientEntry.TABLE_NAME, null, values);
                if (id > 0){
                    uriMatch = ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return uriMatch;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = widgetDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int id;

        switch (match){
            case INGREDIENT:
                id = db.delete(IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = widgetDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int ingredientUpdated;

        switch (match) {
            case INGREDIENT_ID:
                //update a single task by getting the id
                String id = uri.getPathSegments().get(1);
                ingredientUpdated = db.update(IngredientEntry.TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (ingredientUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ingredientUpdated;
    }
}
