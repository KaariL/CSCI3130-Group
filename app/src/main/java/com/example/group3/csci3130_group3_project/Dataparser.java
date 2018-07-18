package com.example.group3.csci3130_group3_project;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dataparser
{
    private HashMap<String,String> getPlace(JSONObject googlePlaseJason)
    {
        HashMap<String, String> googlePlaseMap=new HashMap<>();
        String plaseName="-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longtitude="";
        String reference="";
        try {
            if(!googlePlaseJason.isNull("name"))
            {

                    plaseName=googlePlaseJason.getString("name");
            }
            if (!googlePlaseJason.isNull("vicinity"))
            {
                vicinity=googlePlaseJason.getString("vicinity");
            }
            latitude=googlePlaseJason.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longtitude=googlePlaseJason.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference=googlePlaseJason.getString("reference");

            googlePlaseMap.put("place_name",plaseName);
            googlePlaseMap.put("vicinity",vicinity);
            googlePlaseMap.put("lat",latitude);
            googlePlaseMap.put("lng",longtitude);
            googlePlaseMap.put("reference",reference);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlaseMap;
    }

    private List<HashMap<String,String>> getPlace(JSONArray jsonArray)
    {
        int count=jsonArray.length();
        List<HashMap<String,String>> plaseList=new ArrayList<>();
        HashMap<String,String> plasemap=null;
    }
}
