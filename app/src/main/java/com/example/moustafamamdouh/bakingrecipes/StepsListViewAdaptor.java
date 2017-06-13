package com.example.moustafamamdouh.bakingrecipes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moustafamamdouh.bakingrecipes.storage.DBContract;

/**
 * Created by Moustafa.Mamdouh on 6/10/2017.
 */

public class StepsListViewAdaptor extends CursorAdapter {

    public StepsListViewAdaptor(Context context, Cursor c, int flags) {

        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.baking_step_layout, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor.getColumnCount() == 7) {
            TextView bakingStep = (TextView) view.findViewById(R.id.baking_step_text_item);
            int tempInt = cursor.getInt(cursor.getColumnIndex(DBContract.StepsEntries.COLUMN_STEP_NO));
            String no = Integer.toString(tempInt);
            String temp = no + ". " +
                    cursor.getString(cursor.getColumnIndex(DBContract.StepsEntries.COLUMN_SHORT_DESCRIPTION));
            bakingStep.setText(temp);
        }else if (cursor.getColumnCount() == 5){
            TextView bakingIngredient = (TextView) view.findViewById(R.id.baking_step_text_item);
            int tempInt = cursor.getInt(cursor.getColumnIndex(DBContract.IngredientsEntries.COLUMN_QUANTITY));
            String no = Integer.toString(tempInt);
            String measure = cursor.getString(cursor.getColumnIndex(DBContract.IngredientsEntries.COLUMN_MEASURE));
            String ingredient = cursor.getString(cursor.getColumnIndex(DBContract.IngredientsEntries.COLUMN_INGREDIENT));
            String temp = no + " " + measure + " " + ingredient ;
            bakingIngredient.setText(temp);
        }
    }
}
