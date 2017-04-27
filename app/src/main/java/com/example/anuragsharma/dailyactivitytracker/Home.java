package com.example.anuragsharma.dailyactivitytracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.TimeZone;

public class Home extends AppCompatActivity {
    private DateDbHelper dateDbHelper = new DateDbHelper(this);
    private ArrayList<Date> mDatesList = new ArrayList<>();
    private DateAdapter mAdapter;
    //private HashSet<Date> set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        final ListView mlistView = (ListView) findViewById(R.id.list_date);

        final ArrayList<Date> temp = dateDbHelper.queryAllDateEntries();
        //set = new HashSet<>(temp);
        //mAdapter.notifyDataSetChanged();
        if(temp.size()!=0)
        {
            TextView t = (TextView) findViewById(R.id.emptyTextView);
                    t.setVisibility(View.GONE);
            Collections.reverse(temp);
            mDatesList.addAll(temp);
            //HashSet<Date> set = new HashSet<>(mDatesList);
            mAdapter = new DateAdapter(Home.this,mDatesList);
            mlistView.setAdapter(mAdapter);
        }

        Button b = (Button) findViewById(R.id.button_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) Home.this.getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        Home.this.getCurrentFocus().getWindowToken(), 0);

                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                int syear = cal.get(Calendar.YEAR);
                int smonth = cal.get(Calendar.MONTH);
                int sdayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Home.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TextView t = (TextView) findViewById(R.id.emptyTextView);
                        t.setVisibility(View.GONE);
                        String monthName = "_";
                        //month selection
                        if(month == 0)
                            monthName = "January";
                        else if(month == 1)
                            monthName = "February";
                        else if(month == 2)
                            monthName = "March";
                        else if(month == 3)
                            monthName = "April";
                        else if(month == 4)
                            monthName = "May";
                        else if(month == 5)
                            monthName = "June";
                        else if(month == 6)
                            monthName = "July";
                        else if(month == 7)
                            monthName = "August";
                        else if(month == 8)
                            monthName = "September";
                        else if(month == 9)
                            monthName = "October";
                        else if(month == 10)
                            monthName = "November";
                        else if(month == 11)
                            monthName = "December";

                            Date date = new Date(dayOfMonth + " " + monthName + " " + year);
                        dateDbHelper.insertDate(date);
                        mDatesList.clear();
                        ArrayList<Date> temp = dateDbHelper.queryAllDateEntries();
                        Collections.reverse(temp);
                        mDatesList.addAll(temp);
                        mAdapter = new DateAdapter(Home.this,mDatesList);
                        mlistView.setAdapter(mAdapter);
                        /*
                        if(!set.contains(date))
                        {
                            //mAdapter.clear();
                            mDatesList.add(0,date);
                            mAdapter = new DateAdapter(Home.this,mDatesList);
                            mlistView.setAdapter(mAdapter);
                        }
                        */

                    }
                }, syear, smonth, sdayOfMonth);
                datePickerDialog.show();
            }
        });

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date date = mAdapter.getItem(position);
                Intent intent = new Intent(Home.this,ActivityList.class);
                intent.putExtra("date_id",date.getDate());
                startActivityForResult(intent,0);
            }
        });

        //Selection Delete
        mlistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mlistView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = mlistView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                mAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = mAdapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Date selecteditem = mAdapter
                                        .getItem(selected.keyAt(i));
                                ArrayList<Activity> activityArrayList = dateDbHelper.queryAllActivityEntries(selecteditem.getDate());
                                if(activityArrayList.size()>0)
                                {
                                    for(int j = 0;j<activityArrayList.size();j++)
                                        dateDbHelper.deleteActivity(activityArrayList.get(j));
                                }
                                dateDbHelper.deleteDate(selecteditem);
                                // Remove selected items following the ids
                                mDatesList.remove(selecteditem);
                                mAdapter.remove(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mAdapter.removeSelection();
            }
        });

        EditText etSearch = (EditText) findViewById(R.id.inputSearch);
        // Add Text Change Listener to EditText
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                if(mAdapter!=null)
                {
                    int textlength = s.length();
                    ArrayList<Date> tempArrayList = new ArrayList<>();
                    //Toast.makeText(Home.this,mDatesList.get(0).getDate(),Toast.LENGTH_SHORT).show();
                    ArrayList<Date> date = dateDbHelper.queryAllDateEntries();
                    Collections.reverse(date);
                    for(Date c: date){
                        if (textlength <= c.getDate().length()) {
                            if (c.getDate().toLowerCase().contains(s.toString().toLowerCase())) {
                                tempArrayList.add(c);
                            }
                        }
                    }
                    mAdapter = new DateAdapter(Home.this, tempArrayList);
                    mlistView.setAdapter(mAdapter);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
