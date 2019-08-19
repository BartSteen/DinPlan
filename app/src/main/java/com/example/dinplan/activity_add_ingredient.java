package com.example.dinplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class activity_add_ingredient extends AppCompatActivity {

    Ingredient currentIngredient;
    String oldName;
    String customString = "custom";

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

        //spinner stuff
        final Spinner spinner = findViewById(R.id.spn_unit);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unitStringArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //check if this is an edit
        if (getIntent().getExtras() != null) {
            currentIngredient = (Ingredient) getIntent().getExtras().get("Ingredient");
            nameText.setText(currentIngredient.getName());
            amountText.setText(Float.toString(currentIngredient.getAmount()));
            oldName = currentIngredient.getName();
            setSpinnerSelection(spinner, currentIngredient.getUnit());

            //if we have custom set etxt
            if (spinner.getSelectedItem().equals(customString)) {
                unitText.setText(currentIngredient.getUnit());
            } else {
                unitText.setVisibility(View.GONE);
            }

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


        //spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentIngredient.setUnit((String) adapterView.getItemAtPosition(i));
                //FIND A BETTER WAY TO CHANGE COLOUR **
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorLightText));

                //if it is custom show textfield
                if (adapterView.getItemAtPosition(i).equals(customString)) {
                    unitText.setVisibility(View.VISIBLE);
                } else {
                    unitText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnAddIng = findViewById(R.id.btn_addIng);
        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return data inserted
                //check if all fields filled in properly
                if ((!nameText.getText().toString().equals("") && !amountText.getText().toString().equals("")) && (unitText.getVisibility() == View.GONE || !unitText.getText().toString().equals("")))
                {
                    Intent returnIntent = new Intent();
                    currentIngredient.setName(nameText.getText().toString());
                    currentIngredient.setAmount(Float.parseFloat(amountText.getText().toString()));

                    //let the custom thingy be the unit in case that is selected
                    if (spinner.getSelectedItem().equals(customString)) {
                        currentIngredient.setUnit(unitText.getText().toString());
                    }
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

    //sets the spinner value to the string if it is in the list
    public void setSpinnerSelection(Spinner spin, String str) {
        for (int i = 0; i < spin.getCount(); i++) {
            if (spin.getItemAtPosition(i).toString().toLowerCase().equals(str.toLowerCase())) {
                spin.setSelection(i);
                return;
            }
        }
        //if it is not in there: custom
        spin.setSelection(spin.getCount() - 1);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.option_delete_ing:
                //show confirmation pop up
                confirmPopUp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //shows confirmation pop up
    private void confirmPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Ingredient");
        builder.setMessage("Are you sure you want to delete this ingredient?");

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

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //end activity and give orders ro remove current ingredient
    private void returnRemove() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("oldName", oldName);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ingredient_settings, menu);
        return true;
    }
}
