package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;


public class activity_calendar extends AppCompatActivity {

    Integer dayOfMonth;
    Integer monthNumber;
    Integer yearNumber;
    Integer dayOfWeek;
    DataController dataCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();

        dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        monthNumber = Calendar.getInstance().get(Calendar.MONTH);
        yearNumber = Calendar.getInstance().get(Calendar.YEAR);
        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        initRecyclerView();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_week_list);
        RecyclerViewAdapterWeek adapter = new RecyclerViewAdapterWeek(dataCont,this);
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
                RecyclerView recyclerView = findViewById(R.id.recycler_week_list);
                recyclerView.getAdapter().notifyDataSetChanged();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
