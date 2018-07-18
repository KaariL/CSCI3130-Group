package com.example.group3.csci3130_group3_project;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;

public class NearByCoffee extends AsyncTask<Object,String,String> {

    public String googleCoffeedata;
    public GoogleMap mMap;
    public String url;
    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];
        try {
            googleCoffeedata=UrlDownload.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleCoffeedata;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
