package com.example.moustafamamdouh.bakingrecipes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moustafamamdouh.bakingrecipes.storage.DBContract;

public class RecipeDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Button ingredientsButton;
    Button bakingStepsButton;
    ListView bakingStepsList;
    StepsListViewAdaptor stepsListViewAdaptor;
    StepsListViewAdaptor ingredientsListViewAdaptor;
    ListView IngredientsList;

    private final static int SHOW_BAKING_STEPS_LOADER = 15 ;
    private final static int SHOW_INGREDIENTS_LOADER = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        final int recipe_no = getIntent().getIntExtra(Intent.EXTRA_INTENT,0);

        stepsListViewAdaptor = new StepsListViewAdaptor(this, null, 0);
        ingredientsListViewAdaptor = new StepsListViewAdaptor(this, null, 0);

        bakingStepsList = (ListView) findViewById(R.id.recipe_steps_list_view);
        bakingStepsList.setAdapter(stepsListViewAdaptor);

        IngredientsList = (ListView) findViewById(R.id.recipe_ingredients_list_view);
        IngredientsList.setAdapter(ingredientsListViewAdaptor);

        ingredientsButton = (Button) findViewById(R.id.Ingredients_button);
        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IngredientsList.setVisibility(View.VISIBLE);
                bakingStepsList.setVisibility(View.INVISIBLE);
                showSteps(recipe_no,SHOW_INGREDIENTS_LOADER);
            }
        });
        bakingStepsButton = (Button) findViewById(R.id.Steps_button);
        bakingStepsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IngredientsList.setVisibility(View.INVISIBLE);
                bakingStepsList.setVisibility(View.VISIBLE);
                showSteps(recipe_no , SHOW_BAKING_STEPS_LOADER);
            }
        });
        bakingStepsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    if(cursor.getString(cursor.getColumnIndex(DBContract.StepsEntries.COLUMN_VIDEOURL)) != null){
                        Intent intent = new Intent(RecipeDetailsActivity.this , PlayerActivity.class);
                        intent.putExtra(DBContract.StepsEntries.COLUMN_VIDEOURL,
                                cursor.getString(cursor.getColumnIndex(DBContract.StepsEntries.COLUMN_VIDEOURL)));
                        startActivity(intent);
                    }else if(cursor.getString(cursor.getColumnIndex(DBContract.StepsEntries.COLUMN_THUMBNAILURL)) != null){
                        Intent intent = new Intent(RecipeDetailsActivity.this , PlayerActivity.class);
                        intent.putExtra(DBContract.StepsEntries.COLUMN_VIDEOURL,
                                cursor.getString(cursor.getColumnIndex(DBContract.StepsEntries.COLUMN_THUMBNAILURL)));
                        startActivity(intent);
                    }else{
                        Toast.makeText(RecipeDetailsActivity.this, "This step has no video", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        showSteps(recipe_no, SHOW_BAKING_STEPS_LOADER);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String temp = Integer.toString(args.getInt(DBContract.StepsEntries.COLUMN_RECIPE_ID));
        if(id == SHOW_BAKING_STEPS_LOADER) {
            return new CursorLoader(this,
                    DBContract.StepsEntries.CONTENT_URI,
                    null,
                    DBContract.StepsEntries.COLUMN_RECIPE_ID + "=?",
                    new String[]{temp},
                    null);
        }else {
            return new CursorLoader(this,
                    DBContract.IngredientsEntries.CONTENT_URI,
                    null,
                    DBContract.IngredientsEntries.COLUMN_RECIPE_ID + "=?",
                    new String[]{temp},
                    null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == SHOW_BAKING_STEPS_LOADER)
            stepsListViewAdaptor.changeCursor(data);
        else
            ingredientsListViewAdaptor.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == SHOW_BAKING_STEPS_LOADER)
            stepsListViewAdaptor.changeCursor(null);
        else
            ingredientsListViewAdaptor.changeCursor(null);
    }

    private void showSteps( int recipeNo , int loader_id ){
        Bundle args = new Bundle();
        args.putInt(DBContract.StepsEntries.COLUMN_RECIPE_ID,recipeNo);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ContentValues[]> queryStepsLoader = loaderManager.getLoader(SHOW_BAKING_STEPS_LOADER);
        Loader<ContentValues[]> queryIngredientsLoader = loaderManager.getLoader(SHOW_INGREDIENTS_LOADER);

        if(loader_id == SHOW_BAKING_STEPS_LOADER){
            if (queryStepsLoader == null){
                loaderManager.initLoader(SHOW_BAKING_STEPS_LOADER,args,this);
            }else {
                loaderManager.restartLoader(SHOW_BAKING_STEPS_LOADER,args,this);
            }
        } else {
            if (queryIngredientsLoader == null){
                loaderManager.initLoader(SHOW_INGREDIENTS_LOADER, args,this).forceLoad();
            }else {
                loaderManager.restartLoader(SHOW_INGREDIENTS_LOADER, args,this).forceLoad();
            }
        }
    }
}
