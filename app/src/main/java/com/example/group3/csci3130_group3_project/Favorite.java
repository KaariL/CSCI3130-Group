package com.example.group3.csci3130_group3_project;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Favorite implements Serializable {
    private String mId;
    private String mName;
    private double mLongitude;
    private double mLatitude;

    public Favorite(){}
    public Favorite(String id, String name, LatLng location){
        mId = id;
        mName = name;
        mLatitude = location.latitude;
        mLongitude = location.longitude;

    }
    public Favorite(String id, String name, double latitude, double longitude){
        mId = id;
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getName(){
        return mName;
    }
    public void setName(String s){
        mName = s;
    }


    public void setLocation(LatLng location){
        mLongitude = location.longitude;
        mLatitude = location.latitude;
    }

 /*   public LatLng getmCoordinates() {
        LatLng mCoordinates = new LatLng(mLatitude, mLongitude);
        return mCoordinates;
    } */
    public void setmLongitude(double longitude){
        mLongitude = longitude;
    }
    public double getmLongitude(){
        return this.mLongitude;
    }
    public void setmLatitude(double latitude){
        mLongitude = latitude;
    }
    public double getmLatitude(){
        return this.mLatitude;
    }

    public String getId(){ return mId; }
    public void setId(String id){
        mId = id;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", mId);
        result.put("name", mName);
        result.put("latitude", mLatitude);
        result.put("longitude", mLongitude);
        return result;
    }

}
