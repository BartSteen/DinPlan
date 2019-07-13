package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

   Integer dayOfMonth;
   Integer monthNumber;
   Integer yearNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Add button  event for adding meal
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_add_meal.class);
                startActivity(myIntent);
            }
        });

        //button for going directly to the meal list
        Button btnMealList = findViewById(R.id.btn_mealList);
        btnMealList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_meal_list.class);
                startActivity(myIntent);
            }
        });

        //button for going to the calendar
        Button btnCalendar = findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_calendar.class);
                startActivity(myIntent);
            }
        });


        //add date stuff

         dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
         monthNumber = Calendar.getInstance().get(Calendar.MONTH);
         yearNumber = Calendar.getInstance().get(Calendar.YEAR);

       // TextView dateTxt = findViewById(R.id.txt_date);
       // dateTxt.setText(String.format("%d-%d-%d", dayOfMonth, monthNumber + 1, yearNumber));



    }

    //set the screen to go back to
    @Override
    public void onBackPressed()
    {
        //Just do nothing for now
    }


}
