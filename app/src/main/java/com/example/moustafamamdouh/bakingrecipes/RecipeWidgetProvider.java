package com.example.moustafamamdouh.bakingrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.moustafamamdouh.bakingrecipes.Services.UpdateWidgetRecipesService;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, int recipeNo) {

        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT,recipeNo);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setTextViewText(R.id.appwidget_text, recipeName);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        UpdateWidgetRecipesService.changeRecipeOnWidget(context);
    }
    public static void updateRecipesWidget(Context context, AppWidgetManager appWidgetManager,
                                           String recipeName, int recipe_no, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId , recipeName , recipe_no);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        UpdateWidgetRecipesService.changeRecipeOnWidget(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

