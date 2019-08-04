package com.example.dinplan;

import java.io.Serializable;
import java.util.ArrayList;

//recipe class
public class Recipe implements Serializable {

    private ArrayList<String> recipeList = new ArrayList<>();
    private int prepTimeMin;

    public Recipe() {
        //hold
    }

    //constructor
    public Recipe(ArrayList<String> recipeList, int prepTimeMin) {
        this.recipeList = recipeList;
        this.prepTimeMin = prepTimeMin;
    }

    public void addStep(String str) {
        recipeList.add(str);
    }

    public ArrayList<String> getRecipeList() {
        return recipeList;
    }

    public int getPrepTimeMin() {
        return prepTimeMin;
    }
}
