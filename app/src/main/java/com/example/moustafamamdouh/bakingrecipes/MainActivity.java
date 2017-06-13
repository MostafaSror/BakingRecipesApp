package com.example.moustafamamdouh.bakingrecipes;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.moustafamamdouh.bakingrecipes.Services.FetchInternetDataService;
import com.example.moustafamamdouh.bakingrecipes.storage.DBContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdaptor.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    ContentValues[] cvRecipes;
    ContentValues[] cvIngredients;
    ContentValues[] cvSteps;

    RecyclerView recipesCardsView;
    RecyclerViewAdaptor recipesAdaptor;

    private MyBroadcastReceiver myBroadcastReceiver;

    private static final int SHOW_RECIPES_FROM_DB_LOADER = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesCardsView = (RecyclerView) findViewById(R.id.recipes_cards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recipesCardsView.setLayoutManager(layoutManager);

        recipesAdaptor = new RecyclerViewAdaptor(this,this);
        recipesCardsView.setAdapter(recipesAdaptor);

        myBroadcastReceiver = new MyBroadcastReceiver();

        //register BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(FetchInternetDataService.ACTION_MyIntentService);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void onListItemClick(ContentValues contentValues) {
        Intent intent = new Intent(this,RecipeDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT, contentValues.getAsInteger(DBContract.RecipesEntries.COLUMN_RECIPE_NO));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                DBContract.RecipesEntries.CONTENT_URI,
                null,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        recipesAdaptor.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipesAdaptor.changeCursor(null);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonData = intent.getStringExtra(FetchInternetDataService.EXTRA_KEY_OUT);

            try {
                getDataFromJson(jsonData);

                if ( cvRecipes.length > 0 ) {
                    getContentResolver().bulkInsert(DBContract.RecipesEntries.CONTENT_URI, cvRecipes);
                }
                if ( cvIngredients.length > 0 ) {
                    getContentResolver().bulkInsert(DBContract.IngredientsEntries.CONTENT_URI, cvIngredients);
                }
                if ( cvSteps.length > 0){
                    getContentResolver().bulkInsert(DBContract.StepsEntries.CONTENT_URI, cvSteps);
                }
                showRecipes();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() {
        Intent intent = new Intent(this, FetchInternetDataService.class);
        startService(intent);
    }
    private void showRecipes(){
        Bundle queryBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ContentValues[]> queryLoader = loaderManager.getLoader(SHOW_RECIPES_FROM_DB_LOADER);

        if (queryLoader == null){
            loaderManager.initLoader(SHOW_RECIPES_FROM_DB_LOADER,queryBundle,this);
        }else {
            loaderManager.restartLoader(SHOW_RECIPES_FROM_DB_LOADER,queryBundle,this);
        }
    }
    public void getDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RECIPE_NO = "id";
        final String OWM_RECIPE_NAME = "name";
        final String OWM_RECIPE_INGREDIENTS = "ingredients";
        final String OWM_RECIPE_STEPS = "steps";
        final String OWM_RECIPE_CAPACITY = "servings";

        final String OWM_INGREDIENT_QUANTITY = "quantity";
        final String OWM_INGREDIENT_MEASURE = "measure";
        final String OWM_INGREDIENT_NAME = "ingredient";

        final String OWM_STEP_NO = "id";
        final String OWM_STEP_SHORTDESC = "shortDescription";
        final String OWM_STEP_DESC = "description";
        final String OWM_STEP_VIDEOURL = "videoURL";
        final String OWM_STEP_THUMBNAIL = "thumbnailURL";

        JSONArray recipes = new JSONArray(movieJsonStr);
        Vector<ContentValues> vRecipes = new Vector<ContentValues>(recipes.length());
        Vector<ContentValues> vIngredients = new Vector<ContentValues>(recipes.length());
        Vector<ContentValues> vSteps = new Vector<ContentValues>(recipes.length());


        for (int i = 0; i < recipes.length(); i++) {

            JSONObject recipeObject = recipes.getJSONObject(i);
            ContentValues recipeValues = new ContentValues();

            int recipeNO = recipeObject.getInt(OWM_RECIPE_NO);
            int servings = recipeObject.getInt(OWM_RECIPE_CAPACITY);
            String name = recipeObject.getString(OWM_RECIPE_NAME);
            JSONArray ingredients = recipeObject.getJSONArray(OWM_RECIPE_INGREDIENTS);
            JSONArray steps = recipeObject.getJSONArray(OWM_RECIPE_STEPS);

            for (int y = 0; y < ingredients.length(); y++){
                JSONObject ingredientObject = ingredients.getJSONObject(y);
                ContentValues ingredientValues = new ContentValues();

                int quantity = ingredientObject.getInt(OWM_INGREDIENT_QUANTITY);
                String measure = ingredientObject.getString(OWM_INGREDIENT_MEASURE);
                String ingredient_name = ingredientObject.getString(OWM_INGREDIENT_NAME);

                ingredientValues.put(DBContract.IngredientsEntries.COLUMN_INGREDIENT, ingredient_name);
                ingredientValues.put(DBContract.IngredientsEntries.COLUMN_MEASURE, measure);
                ingredientValues.put(DBContract.IngredientsEntries.COLUMN_QUANTITY, quantity);
                ingredientValues.put(DBContract.IngredientsEntries.COLUMN_RECIPE_ID, recipeNO);
                vIngredients.add(ingredientValues);
            }
            for (int z = 0; z < steps.length(); z++){
                JSONObject stepsObject = steps.getJSONObject(z);
                ContentValues stepsValues = new ContentValues();

                int step_no = stepsObject.getInt(OWM_STEP_NO);
                String short_desc = stepsObject.getString(OWM_STEP_SHORTDESC);
                String desc = stepsObject.getString(OWM_STEP_DESC);
                String video_url = stepsObject.getString(OWM_STEP_VIDEOURL);
                String thumbnail_url = stepsObject.getString(OWM_STEP_THUMBNAIL);

                stepsValues.put(DBContract.StepsEntries.COLUMN_STEP_NO, step_no);
                stepsValues.put(DBContract.StepsEntries.COLUMN_DESCRIPTION, desc);
                stepsValues.put(DBContract.StepsEntries.COLUMN_SHORT_DESCRIPTION, short_desc);
                stepsValues.put(DBContract.StepsEntries.COLUMN_RECIPE_ID, recipeNO);
                stepsValues.put(DBContract.StepsEntries.COLUMN_THUMBNAILURL, thumbnail_url);
                stepsValues.put(DBContract.StepsEntries.COLUMN_VIDEOURL, video_url);
                vSteps.add(stepsValues);
            }
            recipeValues.put(DBContract.RecipesEntries.COLUMN_RECIPE_NO, recipeNO);
            recipeValues.put(DBContract.RecipesEntries.COLUMN_CAPACITY, servings);
            recipeValues.put(DBContract.RecipesEntries.COLUMN_RECIPE_NAME, name);
            vRecipes.add(recipeValues);
        }

        cvRecipes = new ContentValues[vRecipes.size()];
        cvIngredients = new ContentValues[vIngredients.size()];
        cvSteps = new ContentValues[vSteps.size()];
        vRecipes.toArray(cvRecipes);
        vIngredients.toArray(cvIngredients);
        vSteps.toArray(cvSteps);
        return ;
    }
}
