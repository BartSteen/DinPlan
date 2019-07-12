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


        //add date stuff

         dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
         monthNumber = Calendar.getInstance().get(Calendar.MONTH);
         yearNumber = Calendar.getInstance().get(Calendar.YEAR);

        TextView dateTxt = findViewById(R.id.txt_date);
        dateTxt.setText(String.format("%d-%d-%d", dayOfMonth, monthNumber + 1, yearNumber));

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

    //set the screen to go back to
    @Override
    public void onBackPressed()
    {
        //Just do nothing for now
    }

    //triggers when an ingredient is added
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
