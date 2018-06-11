package com.example.group3.csci3130_group3_project;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;
import android.support.annotation.NonNull;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void submitName(View view) {
        EditText nameEntered = findViewById(R.id.nameHolder);
        User u = new User(nameEntered.getText().toString());
        if(mAuth.getCurrentUser() != null)
            mDatabase.child(mAuth.getUid()).child("UserData").setValue(u);
    }

    public void addLoc(View view) {
        EditText lon = findViewById(R.id.longHolder);
        EditText lat = findViewById(R.id.latHolder);
        Location l = new Location( (long) Integer.parseInt( lon.getText().toString() ),(long) Integer.parseInt(lat.getText().toString() ),"Classroom");
        if(mAuth.getCurrentUser() != null)
            mDatabase.child(mAuth.getUid()).child("FavoriteLocations").push().setValue(l);
    }

    public void createUser(View view) {
        EditText emailEntered = findViewById(R.id.emailHolder);
        EditText passEntered = findViewById(R.id.passwordHolder);
        mAuth.createUserWithEmailAndPassword(emailEntered.getText().toString(), passEntered.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void signInUser(View view) {
        EditText emailEntered = findViewById(R.id.emailHolder);
        EditText passEntered = findViewById(R.id.passwordHolder);
        mAuth.signInWithEmailAndPassword(emailEntered.getText().toString(), passEntered.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user){
        TextView who = findViewById(R.id.currentUser);
        if(user==null){
            who.setText("Not Signed In");
        }
        else{

        }
    }
}
