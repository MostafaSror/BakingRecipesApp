package com.example.moustafamamdouh.bakingrecipes.storage;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Moustafa.Mamdouh on 6/9/2017.
 */

public class DBContract {

    // The "Content authority" is a name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.example.moustafamamdouh.bakingrecipes";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths
    public static final String PATH_RECIPES = "Recipes";
    public static final String PATH_INGREDIENTS = "Ingredients";
    public static final String PATH_BAKING_STEPS = "STEPS";



    //Inner class that defines the movies table
    public static final class RecipesEntries implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public static final String TABLE_NAME = "Recipes";

        public static final String COLUMN_RECIPE_NO = "movie_id";
        public static final String COLUMN_RECIPE_NAME = "title";
        public static final String COLUMN_CAPACITY = "overview";

    }

    public static final class IngredientsEntries implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public static final String TABLE_NAME = "Ingredients";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";

    }

    public static final class StepsEntries implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BAKING_STEPS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BAKING_STEPS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BAKING_STEPS;

        public static final String TABLE_NAME = "Steps";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_STEP_NO = "step_no";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEOURL = "videoURL";
        public static final String COLUMN_THUMBNAILURL = "thumbnailURL";

    }
}
