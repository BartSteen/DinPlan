package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class activity_meal_list extends AppCompatActivity {


    public String dateString;
    private DataController dataCont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();

        Button btnAddMeal = findViewById(R.id.btn_add_meal);
        //get meal just added
        if (getIntent().getExtras() != null) {

           /* //check if this is a replacement
            if (getIntent().getExtras().containsKey("oldName")) {
                dataCont.removeMealFromList((String) getIntent().getExtras().get("oldName"));
            }
            //check if this is adding an meal (rather than delete)
            if (getIntent().getExtras().containsKey("Meal")) {
                Meal newMeal = (Meal) getIntent().getSerializableExtra("Meal");
                dataCont.addMealToList(newMeal);
            } */

            //if we are planning a meal
            if (getIntent().getExtras().containsKey("date")) {
                dateString = (String) getIntent().getExtras().get("date");

                TextView topText = findViewById(R.id.txt_meal_list);
                topText.setText("Plan for: " + dateString);
                btnAddMeal.setVisibility(View.GONE);
            }

           // dataCont.saveMealList();
        }

        initRecyclerView();

      //  RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
    //    recyclerView.getAdapter().notifyDataSetChanged();

        //Add button  event for adding meal

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_add_meal.class);
                startActivityForResult(myIntent, 1);
            }
        });


    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
        RecyclerViewAdapterMeal adapter = new RecyclerViewAdapterMeal(this, dateString, dataCont);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //THIS CAN GO BACK TO SAVED MEALS **
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //set the screen to go back to
  /*  @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/

    //triggers when an ingredient is added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){
                //check if this is a replacement
                if (data.getExtras().containsKey("oldName")) {
                    dataCont.removeMealFromList((String) data.getExtras().get("oldName"));
                }
                //check if this is adding an meal (rather than delete)
                if (data.getExtras().containsKey("Meal")) {
                    Meal newMeal = (Meal) data.getSerializableExtra("Meal");
                    dataCont.addMealToList(newMeal);
                }

                dataCont.saveMealList();

                RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
