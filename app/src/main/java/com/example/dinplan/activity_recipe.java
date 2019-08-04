package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class activity_recipe extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check if there already is a recipe
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("Recipe")) {
                recipe = (Recipe) getIntent().getExtras().get("Recipe");
            }
        } else {
            recipe = new Recipe();
        }

        initRecyclerView();

        //add step to list button
        Button btnAddStep = findViewById(R.id.btn_add_step);
        btnAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipe.addStep("-");

                //update list
                RecyclerView recyclerView = findViewById(R.id.recycler_recipe);
                RecyclerViewAdapterRecipe adapter = (RecyclerViewAdapterRecipe) recyclerView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });

        //save recipe button
        Button btnSaveRec = findViewById(R.id.btn_save_recipe);
        btnSaveRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return with recipe
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Recipe", recipe);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    //same but with a list of meals that should be shown
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_recipe);
        RecyclerViewAdapterRecipe adapter = new RecyclerViewAdapterRecipe(recipe, getBaseContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
