package com.example.dinplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class activity_add_meal extends AppCompatActivity {

    private ArrayList<Ingredient> ingredientsList = new ArrayList<>();
    private Meal currentMeal;
    String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        final EditText mealNameEtxt = findViewById(R.id.etxt_mealName);
        Button btnDelMeal = findViewById(R.id.btn_del_meal);
        TextView upperTxt = findViewById(R.id.txt_meal_main);

        //check if this is an edit
        if (getIntent().getExtras() != null) {
            //set up the current meal to adjust
            currentMeal = (Meal) getIntent().getExtras().get("Meal");
            mealNameEtxt.setText(currentMeal.getName());
            ingredientsList = currentMeal.getIngredients();

            oldName = currentMeal.getName();

            upperTxt.setText("Edit Meal");

            //set action for delete button
            btnDelMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete by passing oldName data in intent
                    Intent myIntent = new Intent(getBaseContext(), activity_meal_list.class);
                    myIntent.putExtra("oldName", oldName);
                    startActivity(myIntent);
                }
            });
        } else {
            currentMeal = new Meal();
            btnDelMeal.setVisibility(View.GONE);
        }

        initRecyclerView();

        //button event for adding ingredient
        Button btnIngredient = findViewById(R.id.btn_addIngredient);
        btnIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_add_ingredient.class);
                startActivityForResult(myIntent, 1);
            }
        });

        //button event for adding meal
        Button btnAddMeal = findViewById(R.id.btn_add_meal);
        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if it has a name
                if (!mealNameEtxt.getText().toString().equals("")) {
                    currentMeal.setName(mealNameEtxt.getText().toString());
                    currentMeal.setIngredients(ingredientsList);

                    Intent myIntent = new Intent(getBaseContext(), activity_meal_list.class);
                    myIntent.putExtra("Meal", currentMeal);

                    //if this was an edit
                    if (oldName != null) {
                        myIntent.putExtra("oldName", oldName);
                    }

                    startActivity(myIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Please give this meal a name", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //add an ingredient to the list for the view if the name is available
    private void addIngToList(Ingredient ing) {
        for (int i = 0; i < ingredientsList.size(); i++) {
            if (ingredientsList.get(i).getName().equals(ing.getName())) {
                ingredientsList.set(i, ing);
                return;
            }
        }
        ingredientsList.add(ing);
    }

    //remove the ingredient with name from the list
    private void removeFromList(String name) {
        for (int i = 0; i < ingredientsList.size(); i++) {
            if (ingredientsList.get(i).getName().equals(name)) {
                ingredientsList.remove(i);
                return;
            }
        }
    }

    //initialize recycler view
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ingredientView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(ingredientsList, this);//(mNames, mAmounts, mUnits, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //triggers when an ingredient is added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){
                //check if this is a replacement
                if (data.getExtras().containsKey("oldName")) {
                    removeFromList((String) data.getExtras().get("oldName"));
                }
                //check if this is adding an ingredient (rather than delete)
                if(data.getExtras().containsKey("Ingredient")) {
                    Ingredient newIngredient = (Ingredient) data.getSerializableExtra("Ingredient");
                    addIngToList(newIngredient);
                }

                RecyclerView recyclerView = findViewById(R.id.ingredientView);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
