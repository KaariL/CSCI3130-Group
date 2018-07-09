package com.example.group3.csci3130_group3_project;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class Favorite {
    private String mId;
    private String mName;
    private double mLongitude;
    private double mLatitude;
    private LatLng mCoordinates;
    public Favorite(){}
    public Favorite(String id, String name, LatLng location){
        mId = id;
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

    public double[] getLocation(){
        double[] location = {mLongitude, mLatitude};
        return location;
    }
    public void setLocation(LatLng location){
        mCoordinates = location;
        mLongitude = location.longitude;
        mLatitude = location.latitude;
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
