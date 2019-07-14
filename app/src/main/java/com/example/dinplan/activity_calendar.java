package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;


public class activity_calendar extends AppCompatActivity {

    Integer dayOfMonth;
    Integer monthNumber;
    Integer yearNumber;
    Integer dayOfWeek;
    HashMap<String, Meal> planMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        monthNumber = Calendar.getInstance().get(Calendar.MONTH);
        yearNumber = Calendar.getInstance().get(Calendar.YEAR);
        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if(getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("planMap")) {
                planMap = (HashMap<String, Meal>) getIntent().getExtras().get("planMap");
            }
        }

        initRecyclerView();

        //button for planning a meal for today
        Button btnPlanMeal = findViewById(R.id.btn_plan_meal);
        btnPlanMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_meal_list.class);
                Random rand = new Random();
                myIntent.putExtra("date", String.format("%d-%d-%d", dayOfMonth + rand.nextInt(10), monthNumber + rand.nextInt(5), yearNumber + rand.nextInt(100)) );
                startActivityForResult(myIntent, 1);
            }
        });

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_week_list);
        RecyclerViewAdapterWeek adapter = new RecyclerViewAdapterWeek(planMap,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){
                //check if this is a replacement
                if (data.getExtras().containsKey("planMap")) {
                    planMap = (HashMap<String, Meal>) data.getExtras().get("planMap");
                    initRecyclerView();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
