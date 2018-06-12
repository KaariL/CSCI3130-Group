package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Log;
import android.support.annotation.NonNull;

public class LogIn extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private static final String TAG = "LogIn";
    private DatabaseReference mDatabase;
    private View intent;
    private EditText nameEntered;
    private EditText infoEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public void createUser(View view) {
        intent = view;
        EditText emailEntered = findViewById(R.id.emailHolder);
        EditText passEntered = findViewById(R.id.passwordHolder);
        nameEntered = findViewById(R.id.firstNameHolder);
        infoEntered = findViewById(R.id.firstInfoHolder);
        if(infoEntered.getText().toString() == null || nameEntered.getText().toString() == null){
            Toast.makeText(LogIn.this, "WHOOPS!! Some info is missing!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(emailEntered.getText().toString(), passEntered.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                User u = new User(nameEntered.getText().toString(),infoEntered.getText().toString());
                                mDatabase.child(user.getUid()).child("Account Data").setValue(u);
                                Toast.makeText(LogIn.this, "DONE!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(intent.getContext(),MainActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LogIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void signInUser(View view) {
        EditText emailEntered = findViewById(R.id.emailHolder);
        EditText passEntered = findViewById(R.id.passwordHolder);
        intent = view;
        mAuth.signInWithEmailAndPassword(emailEntered.getText().toString(), passEntered.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            finish();
                            startActivity(new Intent(intent.getContext(),MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
