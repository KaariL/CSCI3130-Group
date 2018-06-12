package com.example.group3.csci3130_group3_project;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mUserData;
    private FirebaseAuth mAuth;
    private Button accountButton;
    private User user;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        accountButton = (Button)findViewById(R.id.accountButton);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            mUserData = mDatabase.child(mAuth.getUid()).child("Account Data");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    user = dataSnapshot.getValue(User.class);
                    TextView NH = findViewById(R.id.nameHolder);
                    NH.setText(user.name);
                    TextView IH = findViewById(R.id.infoHolder);
                    IH.setText(user.info);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // [START_EXCLUDE]
                    Toast.makeText(MainActivity.this, "Logged Out!",
                            Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            };
            mUserData.addValueEventListener(postListener);
        }
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

    public void updateInfo(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        TextView NNH = findViewById(R.id.nameHolder);
        TextView IH = findViewById(R.id.infoHolder);
        User u = new User(NNH.getText().toString(),IH.getText().toString());
        mDatabase.child(currentUser.getUid()).child("Account Data").setValue(u);
    }

    public void updateUI(FirebaseUser u){
        TextView NH = findViewById(R.id.statusHolder);
        TextView IH = findViewById(R.id.infoHolder);
        TextView NNH = findViewById(R.id.nameHolder);
        if(u==null){
            accountButton.setText("Sign In");
            NH.setText("Sign In Below");
            IH.setText("Sign in to see your info!");
            NNH.setText("You are not logged in");
            findViewById(R.id.updateButton).setVisibility(View.GONE);
        }
        else{
            accountButton.setText("Logout");
            NH.setText("Signed In!");
            findViewById(R.id.updateButton).setVisibility(View.VISIBLE);
        }
    }
}
