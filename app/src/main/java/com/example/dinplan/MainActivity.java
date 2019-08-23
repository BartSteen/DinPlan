package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //for leaving on double press
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button for going directly to the meal list
        Button btnMealList = findViewById(R.id.btn_mealList);
        btnMealList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_meal_list.class);
                startActivity(myIntent);
            }
        });

        /*//button for going to the calendar
        Button btnCalendar = findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_calendar.class);
                startActivity(myIntent);
            }
        }); */

        //button for going to the calendar
        Button btnWeekCal = findViewById(R.id.btn_weekly_calendar);
        btnWeekCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_weekly_calendar.class);
                startActivity(myIntent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        //if pressed twice back out of app
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        //else give message and start delay
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click back again to leave", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onResume() {
        super.onResume();

        //handle settings the today meal text

        //get the date
        Integer dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Integer monthNumber = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Integer yearNumber = Calendar.getInstance().get(Calendar.YEAR);
        String dateString = String.format("%d-%d-%d",dayOfMonth, monthNumber, yearNumber);

        //load data
        DataController dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();

        //set the text to corresponding plan
        TextView planText = findViewById(R.id.txt_today_plan);
        if (dataCont.findPlan(dateString) != null) {
            planText.setText(dataCont.findPlan(dateString).getPlannedMeal().getName());
        } else {
            planText.setText("You have not planned a meal yet");
        }
    }


}
