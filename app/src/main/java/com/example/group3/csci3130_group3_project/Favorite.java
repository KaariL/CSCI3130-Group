package com.example.group3.csci3130_group3_project;

public class Favorite {
    private String mName;
    private double mLongitude;
    private double mLatitude;
    public Favorite(){}
    public Favorite(String name, double longit, double latit){
        mName = name;
        mLongitude = longit;
        mLatitude = latit;

    }

    public String getName(){
        return mName;
    }
    public void setName(String s){
        mName = s;
    }
    private double[] getLocation(){
        double[] location = {mLongitude, mLatitude};
        return location;
    }
    private void setLocation(float longitude, float latitude){
        mLongitude = longitude;
        mLatitude = latitude;
    }


}
