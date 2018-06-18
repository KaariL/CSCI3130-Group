package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends BaseActivity {
    //private Layout mainLayout;
    protected boolean userIsSignedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_main);
        ensureUserSignIn();
    }
    protected void onResume(Bundle savedInstanceState){
        ensureUserSignIn();
    }
    protected void onRestart(Bundle savedInstanceState){
        ensureUserSignIn();
    }
    private void ensureUserSignIn(){
        if (!this.userIsSignedIn){
            Intent intent = new Intent(this, CredentialActivity.class);
            startActivity(intent);
        }

    }
}
