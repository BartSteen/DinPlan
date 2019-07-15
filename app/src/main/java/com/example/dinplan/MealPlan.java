package com.example.dinplan;

public class MealPlan {

    private String dateString;
    private Meal plannedMeal;

    public MealPlan(String dateString, Meal plannedMeal) {
        this.dateString = dateString;
        this.plannedMeal = plannedMeal;
    }

    public String getDateString() {
        return dateString;
    }

    public Meal getPlannedMeal() {
        return plannedMeal;
    }

    public void setPlannedMeal(Meal mealToPlan) {
        plannedMeal = mealToPlan;
    }
}
