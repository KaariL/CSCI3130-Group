package com.example.group3.csci3130_group3_project;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        accountButton = (Button)findViewById(R.id.accountButton);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    public void accountFunc(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            FirebaseAuth.getInstance().signOut();
            updateUI(null);
        }
        else {
            finish();
            startActivity(new Intent(this, LogIn.class));
        }
    }

    public void updateUI(FirebaseUser u){
        TextView NH = findViewById(R.id.nameHolder);
        if(u==null){
            accountButton.setText("Sign In");
            NH.setText("Sign In Below");
        }
        else{
            accountButton.setText("Logout");
            NH.setText("Someone is Signed In!");
        }
    }
}
