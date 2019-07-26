package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();

        //current date setting up
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
               // initRecyclerView();
                RecyclerView recyclerView = findViewById(R.id.recycler_weekly);
                RecyclerViewAdapterWeekly adapterWeekly = (RecyclerViewAdapterWeekly) recyclerView.getAdapter();
                adapterWeekly.updateCal(curCal);
                headerTxt.setText("Week " + curCal.get(Calendar.WEEK_OF_YEAR));
            }
        });

        //button for going to next week
        Button btnRight = findViewById(R.id.btn_weekly_right);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curCal.add(Calendar.DATE, 7);
                RecyclerView recyclerView = findViewById(R.id.recycler_weekly);
                RecyclerViewAdapterWeekly adapterWeekly = (RecyclerViewAdapterWeekly) recyclerView.getAdapter();
                adapterWeekly.updateCal(curCal);
                //initRecyclerView();
                headerTxt.setText("Week " + curCal.get(Calendar.WEEK_OF_YEAR));
            }
        });

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_weekly);
        RecyclerViewAdapterWeekly adapter = new RecyclerViewAdapterWeekly(dataCont,this, curCal, todayDateString);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            //disable scrolling
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    //back button in action bar
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.option_plan_selected:
                //get list of selected days
                RecyclerView recyclerViewP = findViewById(R.id.recycler_weekly);
                RecyclerViewAdapterWeekly adapterWeeklyP = (RecyclerViewAdapterWeekly) recyclerViewP.getAdapter();
                ArrayList<String> selectedDaysListP = adapterWeeklyP.getSelectedDates();

                //go to planning screen
                Intent myIntent = new Intent(getBaseContext(), activity_meal_list.class);
                myIntent.putExtra("dateList", selectedDaysListP);
                startActivityForResult(myIntent, 1);
                return true;
            case R.id.option_clear_selected:
                //get list of selected days
                RecyclerView recyclerView = findViewById(R.id.recycler_weekly);
                RecyclerViewAdapterWeekly adapterWeekly = (RecyclerViewAdapterWeekly) recyclerView.getAdapter();
                ArrayList<String> selectedDaysList = adapterWeekly.getSelectedDates();

                //remove all those plans
                for (int i = 0; i < selectedDaysList.size(); i++ ) {
                    dataCont.removePlan(selectedDaysList.get(i));
                }
                dataCont.savePlan();
                initRecyclerView();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar_settings, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){
                //load plan on returning from selecting a meal
                dataCont.loadPlan();
                initRecyclerView();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
