package com.example.dinplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;


public class RecyclerViewAdapterWeekly extends RecyclerView.Adapter<RecyclerViewAdapterWeekly.ViewHolder>{

    //variables
    DataController dataCont;
    private Context mContext;
    Calendar curCal;
    private String todayDateString;

    //constructor
    public RecyclerViewAdapterWeekly(DataController dataCont, Context mContext, Calendar cal, String todayDateString) {
        this.dataCont =  dataCont;
        this.mContext = mContext;
        this.curCal = (Calendar) cal.clone();
        this.todayDateString = todayDateString;


        //set calendar to start of week
        if (curCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            curCal.add(Calendar.DATE, -6);
        } else {
            curCal.add(Calendar.DATE, -(curCal.get(Calendar.DAY_OF_WEEK) - curCal.getFirstDayOfWeek()));
        }
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
        //get date of position
        Calendar tempCal = (Calendar) curCal.clone();
        tempCal.add(Calendar.DATE, position);

        final String dateString = String.format("%d-%d-%d", tempCal.get(Calendar.DAY_OF_MONTH), tempCal.get(Calendar.MONTH) + 1, tempCal.get(Calendar.YEAR));
        holder.dayDate.setText(dateString);
        if (dataCont.findPlan(dateString) != null) {
            holder.nameMeal.setText(dataCont.findPlan(dateString).getPlannedMeal().getName());
        } else {
            holder.nameMeal.setText("Null");
        }

        if (dateString.equals(todayDateString)) {
            //make current day red
            holder.dayDate.setTextColor(Color.RED);
            holder.nameMeal.setTextColor(Color.RED);
        } else {
            holder.dayDate.setTextColor(Color.BLACK);
            holder.nameMeal.setTextColor(Color.BLACK);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show meal screen to select a meal
                Intent myIntent = new Intent(mContext, activity_meal_list.class);
                myIntent.putExtra("date", dateString);
                ((Activity) mContext).startActivityForResult(myIntent, 1);
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //remove the plan
                if (dataCont.findPlan(dateString) != null) {
                    Toast.makeText(mContext, "Removed " + dataCont.findPlan(dateString).getPlannedMeal().getName() +  " on " + dateString, Toast.LENGTH_SHORT).show();
                    dataCont.removePlan(dateString);
                    dataCont.savePlan();
                    notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return 7;
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
