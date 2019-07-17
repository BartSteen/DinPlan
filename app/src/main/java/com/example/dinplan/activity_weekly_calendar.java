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
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class activity_weekly_calendar extends AppCompatActivity {

    Calendar curCal;
    DataController dataCont;
    private String todayDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_calendar);

        dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();

        curCal = Calendar.getInstance();
        Date currentDate = new Date();
        curCal.setTime(currentDate);

        todayDateString = String.format("%d-%d-%d", curCal.get(Calendar.DAY_OF_MONTH), curCal.get(Calendar.MONTH) + 1, curCal.get(Calendar.YEAR));

        final TextView headerTxt = findViewById(R.id.txt_weekly_head);
        headerTxt.setText("Week " + curCal.get(Calendar.WEEK_OF_YEAR));

        initRecyclerView();

        //button for going to previous week
        Button btnLeft = findViewById(R.id.btn_weekly_left);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curCal.add(Calendar.DATE,  -7);
                initRecyclerView();
                headerTxt.setText("Week " + curCal.get(Calendar.WEEK_OF_YEAR));
            }
        });

        //button for going to next week
        Button btnRight = findViewById(R.id.btn_weekly_right);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curCal.add(Calendar.DATE, 7);
                initRecyclerView();
                headerTxt.setText("Week " + curCal.get(Calendar.WEEK_OF_YEAR));
            }
        });

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_weekly);
        RecyclerViewAdapterWeekly adapter = new RecyclerViewAdapterWeekly(dataCont,this, curCal, todayDateString);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){
                //check if this is a replacement
                dataCont.loadPlan();
                initRecyclerView();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
