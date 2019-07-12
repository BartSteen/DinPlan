package com.example.dinplan;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable {

    private String name;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private String recipe;

    public Meal() {
        //place
    }

    public Meal(String name, ArrayList<Ingredient> ingredients, String recipe) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe() {
        return recipe;
    }
}
