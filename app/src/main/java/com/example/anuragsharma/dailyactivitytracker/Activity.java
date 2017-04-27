package com.example.anuragsharma.dailyactivitytracker;

/**
 * Created by anuragsharma on 27/12/16.
 */

public class Activity {

    private String mName,mCategory,mStartTime,mEndTime;

    public Activity(){}

    public Activity(String name,String startTime,String endTime,String category){
        mName = name;
        mCategory = category;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public String getActivityName(){return mName;}

    public String getCategory() { return mCategory;}

    public String getStartTime(){ return mStartTime;}

    public String getEndTime() { return mEndTime;}
}
