package com.example.dinplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
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
        //Button btnDelMeal = findViewById(R.id.btn_del_meal);
        TextView upperTxt = findViewById(R.id.txt_meal_main);

        //check if this is an edit
        if (getIntent().getExtras() != null) {
            //set up the current meal to adjust
            currentMeal = (Meal) getIntent().getExtras().get("Meal");
            mealNameEtxt.setText(currentMeal.getName());
            edit = true;
            upperTxt.setText("Edit Meal");

           /* //set action for delete button
            btnDelMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete by adding "oldId" as an extra
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("oldId", currentMeal.getId());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }); */
        } else {
            currentMeal = new Meal();
            //get rid of delete button
            // btnDelMeal.setVisibility(View.GONE);
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
                    currentMeal.setName(mealNameEtxt.getText().toString().trim());

                    //add empty recipe if there is none yet
                    if (currentMeal.getRecipe() == null) {
                        currentMeal.setRecipe(new Recipe());
                    }

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Meal", currentMeal);

                    //if this is an edit add oldId as extra so the meal replaces itself in the list
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

        //adds a divider between items
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(getDrawable(R.drawable.ic_divider));
        recyclerView.addItemDecoration(itemDecor);

        RecyclerViewAdapterIngredient adapter = new RecyclerViewAdapterIngredient(currentMeal.getIngredients(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.option_delete:
                //show pop up
                confirmPopUp();
                return true;
            case R.id.option_add_recipe:
                Intent myIntent = new Intent(getBaseContext(), activity_recipe.class);
                //add recipe if it exists
                if (currentMeal.getRecipe() != null) {
                    myIntent.putExtra("Recipe", currentMeal.getRecipe());
                }
                startActivityForResult(myIntent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //shows confirmation pop up
    private void confirmPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Meal");
        builder.setMessage("Are you sure you want to delete this meal?");

        //buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                returnRemove();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //doNothing
            }
        });

        //show pop up
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //end activity and give orders ro remove current meal
    private void returnRemove() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("oldId", currentMeal.getId());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    //shows pop up when there is a duplicate, returns boolean for what is pressed
    private void duplicatePopUp(final Ingredient ing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("An ingredient with that name already exists");
        builder.setMessage("Do you want to replace it or cancel?");


        //buttons
        builder.setPositiveButton("Replace", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //add the ingredient
                currentMeal.addIngredient(ing);
                RecyclerView recyclerView = findViewById(R.id.ingredientView);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //doNothing
            }
        });

        //show pop up
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meal_settings, menu);
        return true;
    }

    //triggers when an ingredient is added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if(resultCode == Activity.RESULT_OK){
                //check if this is a replacement
                if (data.getExtras().containsKey("oldName")) {
                    currentMeal.removeIngredient((String) data.getExtras().get("oldName"));
                }
                //check if this is adding an ingredient (rather than delete)
                if (data.getExtras().containsKey("Ingredient")) {
                    Ingredient newIngredient = (Ingredient) data.getSerializableExtra("Ingredient");

                    //action based on whether or not it is a duplicate
                    if (currentMeal.ingInList(newIngredient)) {
                        //let user choose what to do
                        duplicatePopUp(newIngredient);
                    } else {
                        //just add it freely
                        currentMeal.addIngredient(newIngredient);
                    }
                }

                //if it contains a recipe add it to the meal
                if (data.getExtras().containsKey("Recipe")) {
                    Recipe tempRec = (Recipe) data.getExtras().get("Recipe");
                    currentMeal.setRecipe(tempRec);
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
