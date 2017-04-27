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
 * Created by anuragsharma on 26/12/16.
 */

public class DateAdapter extends ArrayAdapter<Date> {

    //private DateDbHelper mDbHelper = new DateDbHelper(getContext());
    private SparseBooleanArray mSelectedItemsIds;
    List<Date> dateList;

    public DateAdapter(Context context, ArrayList<Date> dateList){
        super(context,0,dateList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.dateList = dateList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.date_list_item, parent, false);
        }

        final Date date = getItem(position);

        TextView t = (TextView) listView.findViewById(R.id.textView_date);
        t.setText(date.getDate());

        return listView;
    }

    @Override
    public void remove(Date object) {
        dateList.remove(object);
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
