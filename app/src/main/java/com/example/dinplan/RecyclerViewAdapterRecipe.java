package com.example.dinplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterRecipe extends RecyclerView.Adapter<RecyclerViewAdapterRecipe.ViewHolder> {

    //variables
    private Recipe recipe;
    private Context mContext;

    //constructor
    public RecyclerViewAdapterRecipe(Recipe recipe, Context mContext) {
        this.recipe = recipe;
        this.mContext = mContext;
    }

    //idk
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_recipe, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //when something new is added?
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String stepString = recipe.getRecipeList().get(position);
        holder.stepText.setText(stepString);
        holder.stepCountTxt.setText(Integer.toString(position + 1));

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //toggle visibility of text and edittext
                if (holder.stepText.getVisibility() == View.VISIBLE) {
                    holder.stepText.setVisibility(View.INVISIBLE);
                    holder.stepEtext.setText(recipe.getRecipeList().get(position));
                    holder.stepEtext.setVisibility(View.VISIBLE);
                } else {
                    holder.stepText.setVisibility(View.VISIBLE);
                    recipe.getRecipeList().set(position, holder.stepEtext.getText().toString());
                    holder.stepText.setText(recipe.getRecipeList().get(position));
                    holder.stepEtext.setVisibility(View.INVISIBLE);
                }

                return true;
            }
        });
    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return recipe.getRecipeList().size();
    }

    //who even knows
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stepText;
        EditText stepEtext;
        TextView stepCountTxt;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stepText = itemView.findViewById(R.id.txt_recipe);
            stepEtext = itemView.findViewById(R.id.etxt_recipe);
            stepCountTxt = itemView.findViewById(R.id.txt_stepCount);
            parentLayout = itemView.findViewById(R.id.parent_layout_recipe);
        }
    }
}
