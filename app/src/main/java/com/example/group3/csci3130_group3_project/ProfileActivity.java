package com.example.group3.csci3130_group3_project;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.instrumentation.stats.Tag;

public class ProfileActivity extends BaseActivity {

    private EditText username, favoriteColor;
    private Button updateButton, passwordChangeButton;
    String profileId;
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_profile);

        /************FIREBASE************************/
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDBInstance = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDBInstance.getReference();
        /****************************************************/

        username = (EditText) findViewById(R.id.profile_username);
        favoriteColor = (EditText) findViewById(R.id.profile_favoriteColor);
        updateButton = (Button) findViewById(R.id.profile_updateButton);
        passwordChangeButton = (Button) findViewById(R.id.profile_passwordButton);


        /*****get the user's info and populate the views*/
        ChildEventListener userListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
                profileId = thisUser.getUid();
                username.setText(thisUser.getUsername());
                favoriteColor.setText(thisUser.getFavoriteColor());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
                profileId = thisUser.getUid();
                username.setText(thisUser.getUsername());
                favoriteColor.setText(thisUser.getFavoriteColor());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseReference.child(user.getUid()).addChildEventListener(userListener);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);


    }
    public void updateProfile(View v){
        String uid = user.getUid();
        String userName = username.getText().toString();
        String color = favoriteColor.getText().toString();
        UserProfile profile = new UserProfile(uid, userName, color);
        if (profileId != null) {


            firebaseReference.child(uid).child(profileId).setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("update", "Success");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage() + ": " + e.getCause();
                            Log.d("update", "Update Failed: " + error);
                        }
                    });
            firebaseReference.child(uid).child(profileId).setValue(profile);

        }
    }
    public void changePassword(View view){
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
        changePasswordDialog.show(getSupportFragmentManager(), "ChangePass");
    }
}


