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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;


public class activity_meal_list extends AppCompatActivity {


    private DataController dataCont;
    private ArrayList<String> dateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();

        Button btnAddMeal = findViewById(R.id.btn_add_meal);
        //see if we have extras
        if (getIntent().getExtras() != null) {

            //if we are planning a meal
            if (getIntent().getExtras().containsKey("dateList")) {
                dateList = (ArrayList<String>) getIntent().getExtras().get("dateList");

                TextView topText = findViewById(R.id.txt_meal_list);
                if (dateList.size() == 1) {
                    topText.setText("Plan for: " + dateList.get(0));
                } else {
                    topText.setText("Plan for " + dateList.size() + " meals");
                }
                //hide add meal button
                btnAddMeal.setVisibility(View.GONE);
            }

        }

        initRecyclerView();

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
        RecyclerViewAdapterMeal adapter = new RecyclerViewAdapterMeal(this, dateList, dataCont);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRecyclerViewQuery(ArrayList<Meal> queryList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
        RecyclerViewAdapterMeal adapter = new RecyclerViewAdapterMeal(this, dateList, dataCont, queryList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //for the arrow in the top left corner
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_meal, menu);

        MenuItem searchItem = menu.findItem(R.id.option_search);

        SearchView curSearchView = (SearchView) searchItem.getActionView();
        curSearchView.setFocusable(false);
        curSearchView.setQueryHint("Search");
        curSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println(s);
                ArrayList<Meal> queryList = dataCont.getMealArrayList();
                String strQuery = s.toLowerCase();
                ArrayList<Meal> resultList = new ArrayList<>();

                //search in the list
                for (int i = 0; i < queryList.size(); i++) {
                    if(queryList.get(i).getName().toLowerCase().contains(strQuery)) {
                        resultList.add(queryList.get(i));
                    }
                }

                initRecyclerViewQuery(resultList);
                return true;
            }
        });
        return true;
    }


    //triggers when an ingredient is added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){

                //check if this has an oldId, so if it is an edit or removal
                if (data.getExtras().containsKey("oldId")) {
                    dataCont.removeMealFromList((String) data.getExtras().get("oldId"));
                }
                //check if this is an edit or add
                if (data.getExtras().containsKey("Meal")) {
                    Meal newMeal = (Meal) data.getSerializableExtra("Meal");
                    dataCont.addMealToList(newMeal);
                }

                //save changes
                dataCont.saveMealList();

                //update recyclerview
                //RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
                //recyclerView.getAdapter().notifyDataSetChanged();
                initRecyclerView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
