package com.example.moustafamamdouh.bakingrecipes.Services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.moustafamamdouh.bakingrecipes.RecipeWidgetProvider;
import com.example.moustafamamdouh.bakingrecipes.storage.DBContract;

/**
 * Created by Moustafa.Mamdouh on 6/12/2017.
 */

public class UpdateWidgetRecipesService extends IntentService {

    int x = 0;

    public UpdateWidgetRecipesService() {
        super("UpdateWidgetRecipesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Cursor cursor = getContentResolver().query(DBContract.RecipesEntries.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (x >= cursor.getCount()) {
            x = 0 ;
        }
        cursor.moveToPosition(x);
        String temp = cursor.getString(cursor.getColumnIndex(DBContract.RecipesEntries.COLUMN_RECIPE_NAME));
        int tempInt = cursor.getInt(cursor.getColumnIndex(DBContract.RecipesEntries.COLUMN_RECIPE_NO));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipesWidget(this, appWidgetManager , temp, tempInt , appWidgetIds );

        x++;
    }

    public static void changeRecipeOnWidget(Context context){
        Intent intent = new Intent(context , UpdateWidgetRecipesService.class);
        context.startService(intent);
    }
}
