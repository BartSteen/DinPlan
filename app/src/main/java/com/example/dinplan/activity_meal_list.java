package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class activity_meal_list extends AppCompatActivity {

    private ArrayList<Meal> mealArrayList = new ArrayList<>();
    final String fileName = "MealList.txt";
    final String fileNamePlan = "MealPlan.txt";
    public String dateString;
    HashMap<String, Meal> plannedDaysMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        loadMealList();
        loadPlan();
        //get meal just added
        if (getIntent().getExtras() != null) {

            //check if this is a replacement
            if (getIntent().getExtras().containsKey("oldName")) {
                removeMealFromList((String) getIntent().getExtras().get("oldName"));
            }
            //check if this is adding an meal (rather than delete)
            if (getIntent().getExtras().containsKey("Meal")) {
                Meal newMeal = (Meal) getIntent().getSerializableExtra("Meal");
                addMealToList(newMeal);
            }

            //if we are planning a meal
            if (getIntent().getExtras().containsKey("date")) {
                dateString = (String) getIntent().getExtras().get("date");

                TextView topText = findViewById(R.id.txt_meal_list);
                topText.setText("Plan for: " + dateString);
            }

            saveMealList();
        }

        initRecyclerView();

        //button for going to the calendar
        Button btnCalendar = findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_calendar.class);
                myIntent.putExtra("planMap", plannedDaysMap);
                startActivity(myIntent);
            }
        });
    }

    //add a meal to the list for the view or replace if it already exists
    private void addMealToList(Meal mealToAdd) {
        for (int i = 0; i < mealArrayList.size(); i++) {
            if (mealArrayList.get(i).getName().equals(mealToAdd.getName())) {
                mealArrayList.set(i, mealToAdd);
                return;
            }
        }
        mealArrayList.add(mealToAdd);
    }


    private void removeMealFromList(String name) {
        for (int i = 0; i < mealArrayList.size(); i++) {
            if (mealArrayList.get(i).getName().equals(name)) {
                mealArrayList.remove(i);
                return;
            }
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_meal_list);
        RecyclerViewAdapterMeal adapter = new RecyclerViewAdapterMeal(mealArrayList,this, dateString, activity_meal_list.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addPlan(String datePlanned, Meal mealPlanned) {
        plannedDaysMap.put(datePlanned, mealPlanned);
    }

    public HashMap<String, Meal> getPlannedDaysMap() {
        return plannedDaysMap;
    }

    //find the meal based on name and returns it, if it doesn't exist return null
    public Meal findMeal(String id) {
        for (Meal curMeal : mealArrayList) {
            if (id.equals(curMeal.getName())) {
                return curMeal;
            }
        }
        return null;
    }


    //Saves the arraylist with the meals in a file in internal storage
    public void saveMealList() {
        String fileContent = "";
        FileOutputStream outputStream;

        //add all the meal data to fileContent
        for (Meal mealCur : mealArrayList) {
            fileContent += mealCur.getName() + "\n";
            for (Ingredient ingCur : mealCur.getIngredients()) {
                fileContent += ingCur.getName() + ";" + ingCur.getAmount() + ";" + ingCur.getUnit() + "|";
            }
            fileContent += "\n";
            //Possible recipe attachment
        }

        //write the fileContent to a file in internal storage
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //reads a file from internal storage and loads it into arraylist
    public void loadMealList() {
        try {
            //Read the file and load it into a string
            FileInputStream inputStream = openFileInput(fileName);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String fullString = stringBuilder.toString();

            //Transform the string into an ArrayList of meals
            mealArrayList.clear();
            Scanner scn = new Scanner(fullString);
            while (scn.hasNext()) {
                //mealName
                String mealName = scn.nextLine();
                //remove all not visible characters
                mealName.replaceAll("\\s", "");

                //ingredient list
                ArrayList<Ingredient> ingList = new ArrayList<>();
                String ingredientsString = scn.nextLine();
                String[] ingredientsSeparate = ingredientsString.split("\\|");
                for (String ingString : ingredientsSeparate) {
                    String[] sepComponents = ingString.split(";");
                    String ingName = sepComponents[0];
                    Float ingAmount = Float.valueOf(sepComponents[1]);
                    String ingUnit = sepComponents[2];
                    ingList.add( new Ingredient(ingName, ingAmount, ingUnit));
                }

                //add the meal to the list
                mealArrayList.add(new Meal(mealName, ingList, ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Saves the planned meals data
    public void savePlan() {
        String fileContent = "";
        FileOutputStream outputStream;

        //add all planned meals to fileContent each plan on a separate line
        for (String dateS : plannedDaysMap.keySet()) {
            fileContent += dateS + ";" + plannedDaysMap.get(dateS).getName() + "\n";
        }

        //write the fileContent to a file in internal storage
        try {
            outputStream = openFileOutput(fileNamePlan, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //reads a file from internal storage and loads it into arraylist
    public void loadPlan() {
        try {
            //Read the file and load it into a string
            FileInputStream inputStream = openFileInput(fileNamePlan);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String fullString = stringBuilder.toString();

            plannedDaysMap.clear();
            Scanner scn = new Scanner(fullString);
            while (scn.hasNext()) {
                String planLine = scn.nextLine();
                String[] planComp = planLine.split(";");
                if (findMeal(planComp[1]) != null) {
                    addPlan(planComp[0], findMeal(planComp[1]));
                } else {
                    System.out.println("IT IS NULL");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //set the screen to go back to
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
