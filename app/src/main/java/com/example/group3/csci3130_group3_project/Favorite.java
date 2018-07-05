package com.example.group3.csci3130_group3_project;

import com.google.android.gms.maps.model.LatLng;

public class Favorite {
    private String mName;
    private double mLongitude;
    private double mLatitude;
    private LatLng mCoordinates;
    public Favorite(){}
    public Favorite(String name, LatLng location){
        mName = name;
        mCoordinates = location;
        mLatitude = location.latitude;
        mLongitude = location.longitude;

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
    private void setLocation(LatLng location){
        mCoordinates = location;
        mLongitude = location.longitude;
        mLatitude = location.latitude;
    }


}
