package com.example.dinplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterMeal extends RecyclerView.Adapter<RecyclerViewAdapterMeal.ViewHolder>{

    //variables
    private ArrayList<Meal> mealArrayList = new ArrayList<>();
    private Context mContext;
    private String dateString;
    private activity_meal_list parAct;

    //constructor
    public RecyclerViewAdapterMeal(ArrayList<Meal> mealArrayList, Context mContext, String dateString, activity_meal_list parAct) {
        this.mealArrayList = mealArrayList;
        this.mContext = mContext;
        this.dateString = dateString;
        this.parAct = parAct;
    }

    //idk
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_meals, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //when something new is added?
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Meal currentMeal = mealArrayList.get(position);
        holder.name.setText(currentMeal.getName());
        holder.ingCount.setText(Integer.toString(currentMeal.getIngredients().size()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(mContext, currentMeal.getName(), Toast.LENGTH_SHORT).show();

                //if we are not selecting
                if (dateString == null) {
                    //show meal screen
                    Intent myIntent = new Intent(mContext, activity_add_meal.class);
                    myIntent.putExtra("Meal", currentMeal);
                    mContext.startActivity(myIntent);
                } else {
                    //go back with the planned meal
                    parAct.addPlan(dateString, currentMeal);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Meal", currentMeal);
                    ((Activity) mContext).setResult(Activity.RESULT_OK, returnIntent);
                    ((Activity) mContext).finish();
                }
            }
        });
    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return mealArrayList.size();
    }

    //who even knows
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView ingCount;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //fix this
            name = itemView.findViewById(R.id.txt_meal_name);
            ingCount = itemView.findViewById(R.id.txt_meal_ing_count);
            parentLayout = itemView.findViewById(R.id.parent_layout_meals);
        }
    }
}
