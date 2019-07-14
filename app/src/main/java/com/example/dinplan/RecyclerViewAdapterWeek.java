package com.example.dinplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapterWeek extends RecyclerView.Adapter<RecyclerViewAdapterWeek.ViewHolder>{

    //variables
    private HashMap<String, Meal> planMap;
    private Context mContext;

    //constructor
    public RecyclerViewAdapterWeek(HashMap<String, Meal> planMap, Context mContext) {
        this.planMap = planMap;
        this.mContext = mContext;
    }

    //idk
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_week, parent, false); //ADJUST
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //when something new is added?
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String dateString = (String) planMap.keySet().toArray()[position];
        holder.dayDate.setText(dateString);
        holder.nameMeal.setText(planMap.get(dateString).getName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show ingredient screen
            //    Intent myIntent = new Intent(mContext, activity_add_ingredient.class);;
             //   myIntent.putExtra("Ingredient", currentIng);
             //   ((Activity) mContext).startActivityForResult(myIntent, 1);
            }
        });
    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return planMap.size();
    }

    //who even knows
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameMeal;
        TextView dayDate;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameMeal = itemView.findViewById(R.id.txt_meal_name_week);
            dayDate = itemView.findViewById(R.id.txt_day_of_week);
            parentLayout = itemView.findViewById(R.id.parent_layout_week);
        }
    }
}
