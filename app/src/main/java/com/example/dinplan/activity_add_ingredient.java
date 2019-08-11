package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class activity_add_ingredient extends AppCompatActivity {

    Ingredient currentIngredient;
    String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText nameText = findViewById(R.id.etxt_IngName);
        final EditText amountText = findViewById(R.id.etxt_amount);
        final EditText unitText = findViewById(R.id.etxt_unit);
        // Button btnDel = findViewById(R.id.btn_delIng);
        TextView upperTxt = findViewById(R.id.txt_ing_main);

        //check if this is an edit
        if (getIntent().getExtras() != null) {
            currentIngredient = (Ingredient) getIntent().getExtras().get("Ingredient");
            nameText.setText(currentIngredient.getName());
            amountText.setText(Float.toString(currentIngredient.getAmount()));
            unitText.setText(currentIngredient.getUnit());
            oldName = currentIngredient.getName();

            upperTxt.setText("Edit ingredient");

        /*    btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete by passing oldName data in intent
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("oldName", oldName);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }); */
        } else {
            currentIngredient = new Ingredient();
            //  btnDel.setVisibility(View.GONE);
        }

        Button btnAddIng = findViewById(R.id.btn_addIng);
        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return data inserted
                //check if all fields filled in
                if (!(nameText.getText().toString().equals("") || amountText.getText().toString().equals("") || unitText.getText().toString().equals("")))
                {
                    Intent returnIntent = new Intent();
                    currentIngredient.setName(nameText.getText().toString());
                    currentIngredient.setAmount(Float.parseFloat(amountText.getText().toString()));
                    currentIngredient.setUnit(unitText.getText().toString());
                    returnIntent.putExtra("Ingredient", currentIngredient);
                    //check if this was an edit
                    if (oldName != null) {
                        returnIntent.putExtra("oldName", oldName);
                    }

                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.option_delete_ing:
                //delete by passing oldName data in intent
                Intent returnIntent = new Intent();
                returnIntent.putExtra("oldName", oldName);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ingredient_settings, menu);
        return true;
    }
}
