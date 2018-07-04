package com.example.group3.csci3130_group3_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Favorites extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_favorites);
    }

}
