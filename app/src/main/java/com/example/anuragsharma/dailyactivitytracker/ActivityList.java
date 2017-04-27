package com.example.anuragsharma.dailyactivitytracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * Created by anuragsharma on 27/12/16.
 */

public class ActivityList extends AppCompatActivity {

    private ListView mListView;
    private DateDbHelper mDbHelper = new DateDbHelper(this);
    private ArrayList<Activity> mActivityList = new ArrayList<>();
    private ActivityAdapter mAdapter;
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        //show list according to activityResult of Home.class
        mListView = (ListView) findViewById(R.id.activity_list);

        Bundle extras = getIntent().getExtras();
        date = extras.getString("date_id");

            ArrayList<Activity> temp = mDbHelper.queryAllActivityEntries(date);
        //Toast.makeText(ActivityList.this,mDbHelper.queryAllActivityEntries()+"",Toast.LENGTH_SHORT).show();

        if(temp.size()!=0)
        {
            TextView t = (TextView) findViewById(R.id.emptyTextView2);
            t.setVisibility(View.GONE);
            Collections.reverse(temp);
            mActivityList.addAll(temp);
            mAdapter = new ActivityAdapter(ActivityList.this,mActivityList);
            mListView.setAdapter(mAdapter);
        }

        Button b = (Button) findViewById(R.id.button_activity_add);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityList.this,AddItem.class);
                intent.putExtra("date_id",date);
                startActivityForResult(intent,0);
            }
        });

        Button stat = (Button) findViewById(R.id.button_activity_stat);

        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Activity> activityArrayList = mDbHelper.queryAllActivityEntries(date);
                long totalHealthTime = 0,totalWorkTime = 0,totalOtherTime = 0;
                if(activityArrayList.size()!=0)
                {

                    for(int i=0;i<activityArrayList.size();i++)
                    {
                        Activity activity = activityArrayList.get(i);
                        if(activity.getCategory().equals("Health"))
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                            try{
                                Date date1 = simpleDateFormat.parse(activity.getStartTime());
                                Date date2 = simpleDateFormat.parse(activity.getEndTime());
                                long difference = date2.getTime() - date1.getTime();
                                totalHealthTime += difference;
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else if(activity.getCategory().equals("Work"))
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                            try{
                                Date date1 = simpleDateFormat.parse(activity.getStartTime());
                                Date date2 = simpleDateFormat.parse(activity.getEndTime());
                                long difference = date2.getTime() - date1.getTime();
                                totalWorkTime += difference;
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else if(activity.getCategory().equals("Other"))
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                            try{
                                Date date1 = simpleDateFormat.parse(activity.getStartTime());
                                Date date2 = simpleDateFormat.parse(activity.getEndTime());
                                long difference = date2.getTime() - date1.getTime();
                                totalOtherTime += difference;
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //Here Dialog Populates

                int sadhanaHour =  (int) (totalHealthTime/ (1000 * 60 * 60));
                int sadhnaMin = (int) (totalHealthTime / (1000 * 60) ) % 60;
                int serviceHour =  (int) (totalWorkTime/ (1000 * 60 * 60));
                int serviceMin = (int) (totalWorkTime / (1000 * 60) ) % 60;
                int bodyHour =  (int) (totalOtherTime/ (1000 * 60 * 60));
                int bodyMin = (int) (totalOtherTime / (1000 * 60) ) % 60;
                String arr[] = {"Total Health Time: "+sadhanaHour + " h " + sadhnaMin + " min "
                        ,"Total Work Time: "+serviceHour + " h " + serviceMin + " min "
                        ,"Total Other Time: "+bodyHour + " h " + bodyMin + " min "};
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityList.this);
                builder.setTitle("Statistics")
                        .setItems(arr, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            };
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = mAdapter.getItem(position);

                //Calculate Total Time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                String totalTime = "-";
                int hours=0,min=0,days=0;
                try
                {
                    Date date1 = simpleDateFormat.parse(activity.getStartTime());
                    Date date2 = simpleDateFormat.parse(activity.getEndTime());
                    long difference = date2.getTime() - date1.getTime();
                     //days = (int) (difference / (1000*60*60*24));
                     //hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                     //min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                    //hours = (hours < 0 ? -hours : hours);
                    hours = (int) (difference/ (1000 * 60 * 60));
                    min = (int) (difference / (1000 * 60) ) % 60 ;

                }
                catch (Exception e){
                    Log.e("Error",e.getMessage());
                }

                //Here Dialog Populates
                String arr[] = {"Category: "+activity.getCategory(),"Start Time: "+
                        activity.getStartTime(),"End Time: "+activity.getEndTime(),"Total Time: "+ hours
                        + " h " + min + " min "};
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityList.this);
                builder.setTitle(activity.getActivityName())
                        .setItems(arr, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            };
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = mListView.getCheckedItemCount();
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
                                Activity selecteditem = mAdapter
                                        .getItem(selected.keyAt(i));
                                mDbHelper.deleteActivity(selecteditem);
                                // Remove selected items following the ids
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

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mAdapter!=null)
        mAdapter.clear();
        ArrayList<Activity> temp = mDbHelper.queryAllActivityEntries(date);
        //Toast.makeText(ActivityList.this,mDbHelper.queryAllActivityEntries()+"",Toast.LENGTH_SHORT).show();

        if(temp.size()!=0)
        {
            TextView t = (TextView) findViewById(R.id.emptyTextView2);
            t.setVisibility(View.GONE);
            Collections.reverse(temp);
            mActivityList.addAll(temp);
            mAdapter = new ActivityAdapter(ActivityList.this,mActivityList);
            mListView.setAdapter(mAdapter);
        }
    }
}
