package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class activity_meal_list extends AppCompatActivity {


    public String dateString;
    private DataController dataCont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);


        dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();
        //get meal just added
        if (getIntent().getExtras() != null) {

            //check if this is a replacement
            if (getIntent().getExtras().containsKey("oldName")) {
                dataCont.removeMealFromList((String) getIntent().getExtras().get("oldName"));
            }
            //check if this is adding an meal (rather than delete)
            if (getIntent().getExtras().containsKey("Meal")) {
                Meal newMeal = (Meal) getIntent().getSerializableExtra("Meal");
                dataCont.addMealToList(newMeal);
            }

            //if we are planning a meal
            if (getIntent().getExtras().containsKey("date")) {
                dateString = (String) getIntent().getExtras().get("date");

                TextView topText = findViewById(R.id.txt_meal_list);
                topText.setText("Plan for: " + dateString);
            }

            dataCont.saveMealList();
        }

        initRecyclerView();

        RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
        RecyclerViewAdapterMeal adapter = new RecyclerViewAdapterMeal(this, dateString, dataCont);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //set the screen to go back to
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
