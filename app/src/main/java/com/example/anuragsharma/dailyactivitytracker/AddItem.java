package com.example.anuragsharma.dailyactivitytracker;

import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by anuragsharma on 25/12/16.
 */

public class AddItem extends AppCompatActivity {
    private DateDbHelper dateDbHelper = new DateDbHelper(this);
    boolean isButtonOnePressed = false;
    boolean isButtonTwoPressed = false;
    private String category = "Other";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        //Categories
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // Start Time
        final Button start = (Button) findViewById(R.id.editText_start);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) AddItem.this.getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        AddItem.this.getCurrentFocus().getWindowToken(), 0);
                // TODO Auto-generated method stub
                java.util.Calendar mcurrentTime = java.util.Calendar.getInstance();
                int hour = mcurrentTime.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(java.util.Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = "AM";

                        } else {
                            AM_PM = "PM";
                            selectedHour -= 12;
                        }
                        isButtonOnePressed = true;
                        if(selectedHour == 0)
                            selectedHour = 12;
                        String hourString = selectedHour+"";
                        if(selectedHour < 10)
                            hourString = "0" + selectedHour ;

                        String minString = selectedMinute+"";
                        if(selectedMinute < 10)
                            minString = "0" + selectedMinute ;

                        start.setText( hourString + ":" + minString + " " + AM_PM);
                    }
                }, hour, minute, false);//Not 24 hour time
                mTimePicker.show();

            }
        });

        // End time
        final Button end = (Button) findViewById(R.id.editText_end);
        end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) AddItem.this.getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        AddItem.this.getCurrentFocus().getWindowToken(), 0);
                // TODO Auto-generated method stub
                java.util.Calendar mcurrentTime = java.util.Calendar.getInstance();
                int hour = mcurrentTime.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(java.util.Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = "AM";

                        } else {
                            AM_PM = "PM";
                            selectedHour -= 12;
                        }
                        if(selectedHour == 0)
                            selectedHour = 12;
                        isButtonTwoPressed = true;

                        String hourString = selectedHour+"";
                        if(selectedHour < 10)
                            hourString = "0" + selectedHour ;

                        String minString = selectedMinute+"";
                        if(selectedMinute < 10)
                            minString = "0" + selectedMinute ;

                        end.setText( hourString + ":" + minString + " " + AM_PM);
                    }
                }, hour, minute, false);//Not 24 hour time
                mTimePicker.show();

            }
        });

        //Submit
        Button submit = (Button) findViewById(R.id.button_submit);
        //final EditText name = (EditText) findViewById(R.id.editText_name);
        // Get a reference to the AutoCompleteTextView in the layout
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_activity);
        // Get the string array
        String[] countries = getResources().getStringArray(R.array.activities_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> auto_adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(auto_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) AddItem.this.getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        AddItem.this.getCurrentFocus().getWindowToken(), 0);
                if(!textView.getText().toString().isEmpty() && isButtonOnePressed && isButtonTwoPressed)
                {

                        Bundle extras = getIntent().getExtras();
                        String date = extras.getString("date_id");
                        Activity activity = new Activity(textView.getText().toString(),start.getText().toString(),
                                end.getText().toString(),category);
                    int flag = 0;

                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                        //Toast.makeText(AddItem.this,"tim " + activity.getEndTime(),Toast.LENGTH_SHORT).show();
                        java.util.Date date1 = simpleDateFormat.parse(activity.getStartTime());
                        java.util.Date date2 = simpleDateFormat.parse(activity.getEndTime());
                        long difference = date2.getTime() - date1.getTime();

                        if(difference >=0)
                        {
                            flag = 0;
                        }
                        else
                        {
                            flag = 1;
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    if(flag == 0)
                    {
                        dateDbHelper.insertActivity(activity,date);
                        Toast.makeText(AddItem.this,"Submitted",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(AddItem.this,"Please enter proper details",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(AddItem.this,"Please Fill the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
