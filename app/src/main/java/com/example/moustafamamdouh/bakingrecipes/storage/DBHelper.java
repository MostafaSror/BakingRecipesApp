package com.example.moustafamamdouh.bakingrecipes.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Moustafa.Mamdouh on 6/9/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "recipes.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + DBContract.RecipesEntries.TABLE_NAME + " (" +
                DBContract.RecipesEntries._ID + " INTEGER PRIMARY KEY," +
                DBContract.RecipesEntries.COLUMN_RECIPE_NO + " INTEGER NOT NULL UNIQUE, " +
                DBContract.RecipesEntries.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                DBContract.RecipesEntries.COLUMN_CAPACITY + " INTEGER NOT NULL " +
                " );";
        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + DBContract.IngredientsEntries.TABLE_NAME + " (" +
                DBContract.IngredientsEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.IngredientsEntries.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                DBContract.IngredientsEntries.COLUMN_INGREDIENT + " TEXT NOT NULL, " +
                DBContract.IngredientsEntries.COLUMN_MEASURE + " TEXT NOT NULL, " +
                DBContract.IngredientsEntries.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + DBContract.IngredientsEntries.COLUMN_RECIPE_ID +
                ") REFERENCES " + DBContract.RecipesEntries.TABLE_NAME + "(" + DBContract.RecipesEntries.COLUMN_RECIPE_NO+ "), " +
                "UNIQUE (" + DBContract.IngredientsEntries.COLUMN_RECIPE_ID +
                "," + DBContract.IngredientsEntries.COLUMN_INGREDIENT + ") ON CONFLICT REPLACE" +
                " );";
        final String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " + DBContract.StepsEntries.TABLE_NAME + " (" +
                DBContract.StepsEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.StepsEntries.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                DBContract.StepsEntries.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                DBContract.StepsEntries.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                DBContract.StepsEntries.COLUMN_STEP_NO + " INTEGER NOT NULL, " +
                DBContract.StepsEntries.COLUMN_VIDEOURL + " TEXT, " +
                DBContract.StepsEntries.COLUMN_THUMBNAILURL + " TEXT, " +
                "FOREIGN KEY (" + DBContract.StepsEntries.COLUMN_RECIPE_ID +
                ") REFERENCES " + DBContract.RecipesEntries.TABLE_NAME + "(" + DBContract.RecipesEntries.COLUMN_RECIPE_NO + "), " +
                "UNIQUE (" + DBContract.StepsEntries.COLUMN_RECIPE_ID +
                "," + DBContract.StepsEntries.COLUMN_STEP_NO + ") ON CONFLICT REPLACE" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STEPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.RecipesEntries.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.IngredientsEntries.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.StepsEntries.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

