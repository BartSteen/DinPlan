package com.example.dinplan;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//This class handles all the data storing, loading and changing
public class DataController {

    private ArrayList<Meal> mealArrayList = new ArrayList<>();
    final String fileName = "MealList.txt";
    final String fileNamePlan = "MealPlan.txt";
    HashMap<String, Meal> plannedDaysMap = new HashMap<>();
    Context curContext;

    public DataController(Context curContext) {
        this.curContext = curContext;
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
            outputStream = curContext.openFileOutput(fileName, Context.MODE_PRIVATE);
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
            FileInputStream inputStream = curContext.openFileInput(fileName);
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
            outputStream = curContext.openFileOutput(fileNamePlan, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //reads a file from internal storage and loads it into arraylist, needs to have run loadMeal first!
    public void loadPlan() {
        try {
            //Read the file and load it into a string
            FileInputStream inputStream = curContext.openFileInput(fileNamePlan);
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

    //Adds a planned meal to the hashmap
    public void addPlan(String datePlanned, Meal mealPlanned) {
        plannedDaysMap.put(datePlanned, mealPlanned);
    }

    public void removePlan(String dateString) {
        plannedDaysMap.remove(dateString);
    }

    //returns the hashmap
    public HashMap<String, Meal> getPlannedDaysMap() {
        return plannedDaysMap;
    }

    //returns the arraylist with the meals
    public ArrayList<Meal> getMealArrayList() {
        return mealArrayList;
    }

    //add a meal to the list for the view or replace if it already exists
    public void addMealToList(Meal mealToAdd) {
        for (int i = 0; i < mealArrayList.size(); i++) {
            if (mealArrayList.get(i).getName().equals(mealToAdd.getName())) {
                mealArrayList.set(i, mealToAdd);
                return;
            }
        }
        mealArrayList.add(mealToAdd);
    }

    //remove a meal from the list with the given name
    public void removeMealFromList(String name) {
        for (int i = 0; i < mealArrayList.size(); i++) {
            if (mealArrayList.get(i).getName().equals(name)) {
                mealArrayList.remove(i);
                return;
            }
        }
    }
}
