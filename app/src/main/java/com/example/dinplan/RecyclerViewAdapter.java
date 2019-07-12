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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    //variables
    private ArrayList<Ingredient> mIngredients = new ArrayList<>();
    private Context mContext;

    //constructor
    public RecyclerViewAdapter(ArrayList<Ingredient> mIngredients, Context mContext) {
        this.mIngredients = mIngredients;
        this.mContext = mContext;
    }

    //idk
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //when something new is added?
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Ingredient currentIng = mIngredients.get(position);
        holder.name.setText(currentIng.getName());
        holder.amount.setText(Float.toString(currentIng.getAmount()));
        holder.unit.setText(currentIng.getUnit());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show ingredient screen
                Intent myIntent = new Intent(mContext, activity_add_ingredient.class);;
                myIntent.putExtra("Ingredient", currentIng);
                ((Activity) mContext).startActivityForResult(myIntent, 1);
            }
        });
    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    //who even knows
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView amount;
        TextView unit;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            amount = itemView.findViewById(R.id.txt_amount);
            unit = itemView.findViewById(R.id.txt_unit);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
