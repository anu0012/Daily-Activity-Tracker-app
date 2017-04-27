package com.example.anuragsharma.dailyactivitytracker;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsharma on 27/12/16.
 */

public class ActivityAdapter extends ArrayAdapter<Activity>{

    private SparseBooleanArray mSelectedItemsIds;
    List<Activity> activityList;

    public ActivityAdapter(Context context, ArrayList<Activity> activityArrayList){
        super(context,0,activityArrayList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.activityList = activityArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
        }

        final Activity activity = getItem(position);

        TextView t = (TextView) listView.findViewById(R.id.textView_activity_name);
        t.setText(activity.getActivityName());

        TextView t2 = (TextView) listView.findViewById(R.id.textView_category);
        t2.setText(activity.getCategory());

        return listView;
    }

    @Override
    public void remove(Activity object) {
        activityList.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
