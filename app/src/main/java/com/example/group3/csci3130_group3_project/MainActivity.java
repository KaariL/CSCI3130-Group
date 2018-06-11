package com.example.group3.csci3130_group3_project;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.logOutButton).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        if(currentUser!=null){
            findViewById(R.id.logOutButton).setVisibility(View.VISIBLE);
        }
    }


    public void goLogIn(View view) {
        finish();
        startActivity(new Intent(this,LogIn.class));
    }

    public void updateUI(FirebaseUser u){
        if(u==null){
            findViewById(R.id.logInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.logOutButton).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.logInButton).setVisibility(View.GONE);
            TextView NH = findViewById(R.id.nameHolder);
            NH.setText("Someone is Signed In!");
        }
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }
}
