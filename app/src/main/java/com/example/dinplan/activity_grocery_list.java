package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class activity_grocery_list extends AppCompatActivity {

    private DataController dataCont;
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataCont = new DataController(getBaseContext());
        dataCont.loadMealList();
        dataCont.loadPlan();

        //get the dateList
        ArrayList<String> dateList = new ArrayList<>();
        if (getIntent().getExtras().containsKey("dateList")) {
            dateList = (ArrayList<String>) getIntent().getExtras().get("dateList");
        }
        //transform to mealList
        ArrayList<Meal> mealList = getMealsFromDates(dateList);
        ingredientList = getIngredientsFromMeals(mealList);

        initRecyclerView();

        //button event for adding item
        Button btnAddItem = findViewById(R.id.btn_add_grocery_item);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), activity_add_ingredient.class);
                startActivityForResult(myIntent, 1);
            }
        });

    }

    //returns arraylist of meals that belong to the dates in dateList
    private ArrayList<Meal> getMealsFromDates(ArrayList<String> dateList) {
        ArrayList<Meal> mealArrayList = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            if (dataCont.findPlan(dateList.get(i)) != null) {
                mealArrayList.add(dataCont.findPlan(dateList.get(i)).getPlannedMeal());
            }
        }
        return mealArrayList;
    }

    private ArrayList<Ingredient> getIngredientsFromMeals(ArrayList<Meal> mealList) {
        ArrayList<Ingredient> ingredientList = new ArrayList<>();

        //loop for meals
        for (int i = 0; i < mealList.size(); i++) {
            Meal curMeal = mealList.get(i);
            //loop for ingredients of meal
            ingredientLoop:
            for (int j = 0; j < curMeal.getIngredients().size(); j++) {
                //add to list
                Ingredient curIng = curMeal.getIngredients().get(j);
                //loop for ingredientsList to check for duplicates
                for (int k = 0; k < ingredientList.size(); k++) {
                    //if name and unit are the same add the amount
                    if (ingredientList.get(k).getName().toLowerCase().equals(curIng.getName().toLowerCase())) {
                        if (ingredientList.get(k).getUnit().toLowerCase().equals(curIng.getUnit().toLowerCase())) {
                            //add up the two amounts and put it in a new ingredient, replacing the old ingredient in the list
                            ingredientList.set(k, new Ingredient(curIng.getName(), curIng.getAmount() + ingredientList.get(k).getAmount(), curIng.getUnit()));
                            //go to next ingredient
                            continue ingredientLoop;
                        }
                    }
                }
                //this is only reached if the ingredient is not already in the list
                ingredientList.add(curIng);
            }
        }

        return ingredientList;
    }

    //removes an item with name nameStr from the arraylist
    private void removeItemFromList(String nameStr) {
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getName().toLowerCase().equals(nameStr.toLowerCase())) {
                ingredientList.remove(i);
                return;
            }
        }
    }

    //add an item to the list
    public void addItemToList(Ingredient ing) {
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getName().toLowerCase().equals(ing.getName().toLowerCase())) {
                //ask what the user wants to do
                boolean sameUnit = ingredientList.get(i).getUnit().toLowerCase().equals(ing.getUnit().toLowerCase());
                confirmPopUp(sameUnit, i, ing);
                return;
            }
        }
        ingredientList.add(ing);
    }

    //shows merge/override pop up and deals with it
    private void confirmPopUp(Boolean equalUnits, final int indexDup, final Ingredient ing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("An item with this name already exists");

        //buttons
        builder.setPositiveButton("Replace", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //replace it in the list
                ingredientList.set(indexDup, ing);

                RecyclerView recyclerView = findViewById(R.id.recycler_grocery_list);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        //if units are the same allow for merge
        if (equalUnits) {
            builder.setNeutralButton("Merge", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //add up the two amounts
                    ingredientList.get(indexDup).setAmount(ingredientList.get(indexDup).getAmount() + ing.getAmount());

                    RecyclerView recyclerView = findViewById(R.id.recycler_grocery_list);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
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


    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_grocery_list);

        //adds a divider between items
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(getDrawable(R.drawable.ic_divider));
        recyclerView.addItemDecoration(itemDecor);

        RecyclerViewAdapterIngredient adapter = new RecyclerViewAdapterIngredient(ingredientList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.option_share_grocery:
                //see if there is anything to share
                if (ingredientList.size() == 0) {
                    Toast.makeText(this, "List is empty", Toast.LENGTH_SHORT).show();
                } else {
                    //get text to share
                    String shareText = "";
                    for (int i = 0; i < ingredientList.size(); i++) {
                        Ingredient tempIng = ingredientList.get(i);
                        shareText += "-" + tempIng.getName() + ": " + tempIng.getAmount() + " " + tempIng.getUnit() + "\n";
                    }

                    //share stuff
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share_grocery, menu);

        return true;
    }

    //triggers when an item is added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            //if successful
            if (resultCode == Activity.RESULT_OK) {
                //check if this is a replacement
                if (data.getExtras().containsKey("oldName")) {
                    removeItemFromList((String) data.getExtras().get("oldName"));
                }
                //check if this is adding an ingredient (rather than delete)
                if (data.getExtras().containsKey("Ingredient")) {
                    Ingredient newIngredient = (Ingredient) data.getSerializableExtra("Ingredient");
                    addItemToList(newIngredient);
                }

                RecyclerView recyclerView = findViewById(R.id.recycler_grocery_list);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}


