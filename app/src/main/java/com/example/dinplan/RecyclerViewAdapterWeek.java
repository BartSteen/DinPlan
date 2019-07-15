package com.example.dinplan;

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


public class RecyclerViewAdapterWeek extends RecyclerView.Adapter<RecyclerViewAdapterWeek.ViewHolder>{

    //variables
    DataController dataCont;
    private Context mContext;

    //constructor
    public RecyclerViewAdapterWeek(DataController dataCont, Context mContext) {
        this.dataCont =  dataCont;
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
        final String dateString = (String) dataCont.getPlannedDaysMap().keySet().toArray()[position];
        holder.dayDate.setText(dateString);
        holder.nameMeal.setText(dataCont.getPlannedDaysMap().get(dateString).getName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show meal screen
                Intent myIntent = new Intent(mContext, activity_add_meal.class);
                myIntent.putExtra("Meal", dataCont.getPlannedDaysMap().get(dateString));
                mContext.startActivity(myIntent);
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext, "Removed " + dateString, Toast.LENGTH_SHORT).show();
                dataCont.removePlan(dateString);
                dataCont.savePlan();
                notifyDataSetChanged();
                return true;
            }
        });
    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return dataCont.getPlannedDaysMap().size();
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
