package com.example.dinplan;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Meal implements Serializable {

    private String name;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Recipe recipe;
    private String id;


    public Meal() {
        id = UUID.randomUUID().toString();
    }

    public Meal(String name, ArrayList<Ingredient> ingredients, Recipe recipe) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        id = UUID.randomUUID().toString();
    }

    public Meal(String name, ArrayList<Ingredient> ingredients, String id) {
        this.name = name;
        this.ingredients = ingredients;
        this.id = id;
    }

    public Meal(String name, ArrayList<Ingredient> ingredients, Recipe recipe, String id) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.id = id;
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

    //add an ingredient to the list for the view if the name is available
    public void addIngredient(Ingredient ing) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getName().equals(ing.getName())) {
                ingredients.set(i, ing);
                return;
            }
        }
        ingredients.add(ing);
    }

    //returns true if ing is already in the ingredients list
    public Boolean ingInList(Ingredient ing) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getName().toLowerCase().equals(ing.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    //remove the ingredient with name from the list
    public void removeIngredient(String name) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getName().equals(name)) {
                ingredients.remove(i);
                return;
            }
        }
    }

    public String getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe tempRec) {
        this.recipe = tempRec;
    }

}
