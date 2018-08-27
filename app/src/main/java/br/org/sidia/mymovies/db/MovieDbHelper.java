package br.org.sidia.mymovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mymovies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE = "CREATE TABLE " + MovieContract.MoviesEntry.TABLE_NAME + " (" +
                MovieContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MoviesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT, " +
                MovieContract.MoviesEntry.COLUMN_TITLE + " TEXT, " +
                MovieContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
