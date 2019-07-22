package com.example.dinplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class activity_add_meal extends AppCompatActivity {

    private Meal currentMeal;
    private Boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText mealNameEtxt = findViewById(R.id.etxt_mealName);
        Button btnDelMeal = findViewById(R.id.btn_del_meal);
        TextView upperTxt = findViewById(R.id.txt_meal_main);

        //check if this is an edit
        if (getIntent().getExtras() != null) {
            //set up the current meal to adjust
            currentMeal = (Meal) getIntent().getExtras().get("Meal");
            mealNameEtxt.setText(currentMeal.getName());
            edit = true;
            upperTxt.setText("Edit Meal");

            //set action for delete button
            btnDelMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete by adding "oldId" as an extra
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("oldId", currentMeal.getId());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        } else {
            currentMeal = new Meal();
            //get rid of delete button
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

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Meal", currentMeal);

                    if (edit) {
                        returnIntent.putExtra("oldId", currentMeal.getId());
                    }

                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Please give this meal a name", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //initialize recycler view
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ingredientView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(currentMeal.getIngredients(), this);//(mNames, mAmounts, mUnits, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //triggers when an ingredient is added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful:
            if(resultCode == Activity.RESULT_OK){
                //check if this is a replacement
                if (data.getExtras().containsKey("oldName")) {
                    currentMeal.removeIngredient((String) data.getExtras().get("oldName"));
                }
                //check if this is adding an ingredient (rather than delete)
                if(data.getExtras().containsKey("Ingredient")) {
                    Ingredient newIngredient = (Ingredient) data.getSerializableExtra("Ingredient");
                    currentMeal.addIngredient(newIngredient);
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
