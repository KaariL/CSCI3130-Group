package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

public class Favorites extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_favorites);
    }

    public void goWithAddress(View view){
        Intent intent = new Intent(this, MainActivity.class);
        String HERE = "1333 South Park Street";
        intent.putExtra("Address", HERE);
        startActivity(intent);
    }
    public void goWithLatLong(View view){
        Intent intent = new Intent(this, MainActivity.class);
        LatLng HERE = new LatLng(100,100);
        intent.putExtra("LatLong", HERE);
        startActivity(intent);
    }

}
