package com.example.moustafamamdouh.bakingrecipes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moustafamamdouh.bakingrecipes.storage.DBContract;

/**
 * Created by mostafa-pc on 5/20/2017.
 */

public class RecyclerViewAdaptor extends CursorAdaptor<RecyclerViewAdaptor.ItemsViewHolder>{

    final private RecyclerViewAdaptor.ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(ContentValues contentValues);
    }

    public RecyclerViewAdaptor(Context context, RecyclerViewAdaptor.ListItemClickListener listener){
        super(context);
        mOnClickListener = listener;
    }


    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_recipe_card, parent , false);
        ItemsViewHolder viewHolder = new ItemsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder viewHolder, Cursor cursor) {
        viewHolder.bind(cursor);
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mRecipeName;
        TextView mRecipeWeight;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            mRecipeName = (TextView) itemView.findViewById(R.id.recipe_name_text_view);
            mRecipeWeight = (TextView) itemView.findViewById(R.id.recipe_members_text_view);
            itemView.setOnClickListener(this);
        }

        void bind(Cursor Recipes) {

            String recipe_name = Recipes.getString(Recipes.getColumnIndex(DBContract.RecipesEntries.COLUMN_RECIPE_NAME));
            int servings = Recipes.getInt(Recipes.getColumnIndex(DBContract.RecipesEntries.COLUMN_CAPACITY));
            mRecipeName.setText(recipe_name);
            mRecipeWeight.setText(Integer.toString(servings));
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mCursor.moveToPosition(clickedPosition);
            ContentValues cv = new ContentValues();
            cv.put(DBContract.RecipesEntries.COLUMN_RECIPE_NO,mCursor.getInt(mCursor.getColumnIndex(DBContract.RecipesEntries.COLUMN_RECIPE_NO)));
            cv.put(DBContract.RecipesEntries.COLUMN_RECIPE_NAME,mCursor.getString(mCursor.getColumnIndex(DBContract.RecipesEntries.COLUMN_RECIPE_NAME)));
            cv.put(DBContract.RecipesEntries.COLUMN_CAPACITY,mCursor.getString(mCursor.getColumnIndex(DBContract.RecipesEntries.COLUMN_CAPACITY)));
            mOnClickListener.onListItemClick(cv);
        }
    }
}
