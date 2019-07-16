package com.example.dinplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
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
    private Context mContext;
    private String dateString;
    private DataController dataCont;

    //constructor
    public RecyclerViewAdapterMeal(Context mContext, String dateString, DataController dataCont) {
        this.mContext = mContext;
        this.dateString = dateString;
        this.dataCont = dataCont;
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
        final Meal currentMeal = dataCont.getMealArrayList().get(position);
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
                    dataCont.addPlan(new MealPlan(dateString, currentMeal));
                    dataCont.savePlan();

                    Intent returnIntent = new Intent();
                    ((Activity) mContext).setResult(Activity.RESULT_OK, returnIntent);
                    ((Activity) mContext).finish();
                }
            }
        });
    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return dataCont.getMealArrayList().size();
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
