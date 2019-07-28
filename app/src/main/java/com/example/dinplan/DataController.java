package com.example.dinplan;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

//This class handles all the data storing, loading and altering
public class DataController {

    private ArrayList<Meal> mealArrayList = new ArrayList<>();
    final String fileName = "MealList.txt";
    final String fileNamePlan = "MealPlan.txt";
    ArrayList<MealPlan> plannedDaysList = new ArrayList<>();
    Context curContext;

    public DataController(Context curContext) {
        this.curContext = curContext;
    }


    //Saves the arraylist with the meals in a file in internal storage
    public void saveMealList() {
        String fileContent = "";
        FileOutputStream outputStream;

        //add all the meal data to fileContent
        for (Meal mealCur : mealArrayList) {
            fileContent += mealCur.getName() + "\n";
            fileContent += mealCur.getId() + "\n";
            //save all ingredients data seperated by a ; and all ingredients seperated by |
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

            //clean the arraylist
            mealArrayList.clear();

            //Transform the string into an ArrayList of meals using a scanner
            Scanner scn = new Scanner(fullString);
            while (scn.hasNext()) {
                String mealName = scn.nextLine();
                //remove all not visible characters
                mealName.replaceAll("\\s", "");

                String mealId = scn.nextLine();
                mealId.replaceAll("\\s", "");

                //temperary arraylist to store the ingredients
                ArrayList<Ingredient> ingList = new ArrayList<>();
                //ingredients are stored on one line separated by a | char
                String ingredientsString = scn.nextLine();
                String[] ingredientsSeparate = ingredientsString.split("\\|");

                //loop over all ingredients
                for (String ingString : ingredientsSeparate) {
                    //within each ingredient the variables are separated by ;
                    String[] sepComponents = ingString.split(";");
                    if (sepComponents.length == 3) {
                        String ingName = sepComponents[0];
                        Float ingAmount = Float.valueOf(sepComponents[1]);
                        String ingUnit = sepComponents[2];
                        //add ingredient gained from string to the list
                        ingList.add(new Ingredient(ingName, ingAmount, ingUnit));
                    }
                }

                //add the meal to the list
                mealArrayList.add(new Meal(mealName, ingList,  mealId));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Saves the planned meals data
    public void savePlan() {
        String fileContent = "";
        FileOutputStream outputStream;

        //add all planned meals to fileContent each plan on a separate line with datestring and mealname seperated by ;
        //for meal only the unique id is stored
        for (MealPlan mp : plannedDaysList) {
            fileContent += mp.getDateString() + ";" + mp.getPlannedMeal().getId() + "\n";
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

            //prepare plan list
            plannedDaysList.clear();

            //scan over the string, every line contains a dateString and a meal name seperated by ;
            Scanner scn = new Scanner(fullString);
            while (scn.hasNext()) {
                String planLine = scn.nextLine();
                String[] planComp = planLine.split(";");
                //planComp[0] = dateString, planComp[1] = meal id
                //check if there is a meal with such an id
                if (findMeal(planComp[1]) != null) {
                    addPlan(new MealPlan(planComp[0], findMeal(planComp[1])));
                } else {
                    System.out.println("IT IS NULL");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Adds a planned meal to the list or replace the meal if it already exists
    public void addPlan(MealPlan mp) {
        if (findPlan(mp.getDateString()) == null) {
            plannedDaysList.add(mp);
        } else {
            findPlan(mp.getDateString()).setPlannedMeal(mp.getPlannedMeal());
        }
    }

    //remove a planned meal from the arrayList based on date (string)
    public void removePlan(String dateString) {
        for (int i = 0; i < plannedDaysList.size(); i++) {
            if (plannedDaysList.get(i).getDateString().equals(dateString)) {
                plannedDaysList.remove(i);
                return;
            }
        }
    }

    //find a plan with the corresponding date
    public MealPlan findPlan(String dateString) {
        for (int i = 0; i < plannedDaysList.size(); i++) {
            if (plannedDaysList.get(i).getDateString().equals(dateString)) {
                return plannedDaysList.get(i);
            }
        }
        return null;
    }

    //returns the arraylist of plans
    public ArrayList<MealPlan> getPlannedDaysList() {
        return plannedDaysList;
    }

    //returns the arraylist with the meals
    public ArrayList<Meal> getMealArrayList() {
        return mealArrayList;
    }

    //find the meal based on id and returns it, if it doesn't exist return null
    public Meal findMeal(String id) {
        for (Meal curMeal : mealArrayList) {
            if (id.equals(curMeal.getId())) {
                return curMeal;
            }
        }
        return null;
    }

    //add a meal to the list giving it an unique name recursively
    public void addMealToList(Meal mealToAdd) {
        for (int i = 0; i < mealArrayList.size(); i++) {
            if (mealArrayList.get(i).getName().equals(mealToAdd.getName())) {
                mealToAdd.setName(mealToAdd.getName() + i);
                addMealToList(mealToAdd);
                return;
            }
        }
        mealArrayList.add(mealToAdd);
    }

    //remove a meal from the list with the given id
    public void removeMealFromList(String id) {
        for (int i = 0; i < mealArrayList.size(); i++) {
            if (mealArrayList.get(i).getId().equals(id)) {
                mealArrayList.remove(i);
                return;
            }
        }
    }
}
