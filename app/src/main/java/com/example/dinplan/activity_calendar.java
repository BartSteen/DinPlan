package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


public class activity_calendar extends AppCompatActivity {

    Integer dayOfMonth;
    Integer monthNumber;
    Integer yearNumber;
    Integer dayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        monthNumber = Calendar.getInstance().get(Calendar.MONTH);
        yearNumber = Calendar.getInstance().get(Calendar.YEAR);
        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        //button for planning a meal for today
        Button btnPlanMeal = findViewById(R.id.btn_plan_meal);
        btnPlanMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_meal_list.class);
                myIntent.putExtra("date", String.format("%d-%d-%d", dayOfMonth, monthNumber + 1, yearNumber) );
                startActivityForResult(myIntent, 1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){
                //check if this is a replacement
                if (data.getExtras().containsKey("Meal")) {
                    Meal plannedMeal = (Meal) data.getExtras().get("Meal");
                    TextView dateTxt = findViewById(R.id.txt_date);
                    dateTxt.setText(String.format("%d-%d-%d", dayOfMonth, monthNumber + 1, yearNumber) + " : " + plannedMeal.getName());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
