package com.example.dinplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class RecyclerViewAdapterWeekly extends RecyclerView.Adapter<RecyclerViewAdapterWeekly.ViewHolder> {

    //variables
    DataController dataCont;
    private Context mContext;
    Calendar curCal;
    private String todayDateString;
    private ArrayList<String> selectedDates = new ArrayList<>();
    private String[] currentDates = new String[7];
    private Menu curMenu;
    private boolean selecting = false;

    //constructor
    public RecyclerViewAdapterWeekly(DataController dataCont, Context mContext, Calendar cal, String todayDateString) {
        this.dataCont = dataCont;
        this.mContext = mContext;
        this.curCal = (Calendar) cal.clone();
        this.todayDateString = todayDateString;


        //set calendar to start of week
        if (curCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            curCal.add(Calendar.DATE, -6);
        } else {
            curCal.add(Calendar.DATE, -(curCal.get(Calendar.DAY_OF_WEEK) - curCal.getFirstDayOfWeek()));
        }
    }

    public void giveMenu(Menu curMenu) {
        this.curMenu = curMenu;
    }

    public void updateCal(Calendar cal) {
        this.curCal = (Calendar) cal.clone();
        //set calendar to start of week
        if (curCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            curCal.add(Calendar.DATE, -6);
        } else {
            curCal.add(Calendar.DATE, -(curCal.get(Calendar.DAY_OF_WEEK) - curCal.getFirstDayOfWeek()));
        }
        notifyDataSetChanged();
    }

    //idk
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_week, parent, false); //ADJUST
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //when something new is added?
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get date of position
        Calendar tempCal = (Calendar) curCal.clone();
        tempCal.add(Calendar.DATE, position);

        //get day name of current date
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        String dayName = formatter.format(tempCal.getTime());

        //Get month name
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM");
        String monthName = monthFormatter.format(tempCal.getTime());

        //set the texts
        final String dateString = String.format("%d-%d-%d", tempCal.get(Calendar.DAY_OF_MONTH), tempCal.get(Calendar.MONTH) + 1, tempCal.get(Calendar.YEAR));
        currentDates[position] = dateString;
        holder.dayDate.setText(dayName + " " + tempCal.get(Calendar.DAY_OF_MONTH) + " " + monthName);
        if (dataCont.findPlan(dateString) != null) {
            holder.nameMeal.setText(dataCont.findPlan(dateString).getPlannedMeal().getName());
        } else {
            holder.nameMeal.setText("Null");
        }

        //current day red
        if (dateString.equals(todayDateString)) {
            holder.dayDate.setTextColor(Color.RED);
            holder.nameMeal.setTextColor(Color.RED);
        } else {
            holder.dayDate.setTextColor(Color.BLACK);
            holder.nameMeal.setTextColor(Color.BLACK);
        }

        //selection image visibility
        if (dateInList(dateString)) {
            holder.dayDate.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.nameMeal.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if we are not in selection mode
                if (selecting == false) {
                    //show meal screen to select a meal
                    ArrayList<String> tempList = new ArrayList<>();
                    tempList.add(dateString);

                    Intent myIntent = new Intent(mContext, activity_meal_list.class);
                    myIntent.putExtra("dateList", tempList);
                    ((Activity) mContext).startActivityForResult(myIntent, 1);
                } else { //if we are selecting
                    handleSelection(dateString);
                }
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handleSelection(dateString);
                return true;
            }
        });

        //update menu when all days have been updated
        if (position == getItemCount() - 1) {
            setMenuEnables();
        }

    }

    //returns the amount of items
    @Override
    public int getItemCount() {
        return 7;
    }

    //who even knows
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameMeal;
        TextView dayDate;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameMeal = itemView.findViewById(R.id.txt_meal_name_week);
            dayDate = itemView.findViewById(R.id.txt_day_of_week);
            parentLayout = itemView.findViewById(R.id.parent_layout_week);
        }
    }

    //check if a date string is in the list of selected dates, returns true if it is
    public boolean dateInList(String dateStr) {
        for (int i = 0; i < selectedDates.size(); i++) {
            if (selectedDates.get(i).equals(dateStr)) {
                return true;
            }
        }
        return false;
    }

    //remove the string from the list of selected dates
    public void removeFromList(String dateStr) {
        for (int i = 0; i < selectedDates.size(); i++) {
            if (selectedDates.get(i).equals(dateStr)) {
                selectedDates.remove(i);
            }
        }
    }

    //deals with selection of a plan
    public void handleSelection(String dateString) {
        if (dateInList(dateString)) {
            removeFromList(dateString);
        } else {
            selectedDates.add(dateString);
        }
        setMenuEnables();
        notifyDataSetChanged();
    }


    public ArrayList<String> getSelectedDates() {
        return selectedDates;
    }

    public boolean getSelecting() {
        return selecting;
    }

    public String[] getCurrentDates() {
        return currentDates;
    }

    //sets the action bar stuff properly based on current status
    public void setMenuEnables() {
        if (curMenu != null) {
            //get all items
            MenuItem clearItem = curMenu.findItem(R.id.option_clear_selected);
            MenuItem planItem = curMenu.findItem(R.id.option_plan_selected);
            MenuItem groceryListItem = curMenu.findItem(R.id.option_grocery_list);

            MenuItem stopSelectionItem = curMenu.findItem(R.id.option_stop_selection);

            MenuItem selectAllItem = curMenu.findItem(R.id.option_check_box);
            CheckBox selectAllBox = (CheckBox) selectAllItem.getActionView();

            if (selectedDates.size() == 0) { //if something is selected
                selecting = false;

                //disable options of menu
                clearItem.setEnabled(false);
                planItem.setEnabled(false);
                groceryListItem.setEnabled(false);

                stopSelectionItem.setEnabled(false);
                stopSelectionItem.setVisible(false);
                selectAllItem.setEnabled(false);
                selectAllItem.setVisible(false);

                //change action bar title
                ((Activity) mContext).setTitle("Planning");
            } else {
                selecting = true;

                //enable options of menu
                clearItem.setEnabled(true);
                planItem.setEnabled(true);
                groceryListItem.setEnabled(true);

                stopSelectionItem.setEnabled(true);
                stopSelectionItem.setVisible(true);
                selectAllItem.setEnabled(true);
                selectAllItem.setVisible(true);

                //deal checkbox state
                int count = 0;
                for (int i = 0; i < currentDates.length; i++) {
                    if (dateInList(currentDates[i])) {
                        count++;
                    }
                }

                if (count == 7) {
                    selectAllBox.setChecked(true);
                    selectAllItem.setTitle("Deselect all");
                } else {
                    selectAllBox.setChecked(false);
                    selectAllItem.setTitle("Select all");
                }


                //change action bar title
                ((Activity) mContext).setTitle(selectedDates.size() + " selected");
            }
        }
    }
}
