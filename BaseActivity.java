package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    FirebaseAuth firebaseAuth;
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /************FIREBASE************************/
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDBInstance = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDBInstance.getReference();

        /****************************************************/
        addNavBar();
    }
    public void addNavBar() {
        /*****get the user's color and color the view*/
        final String[] colorString = {"FFFFFF"}; //default
        ChildEventListener userListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
                if (thisUser != null) {
                    colorString[0] = thisUser.getFavoriteColor();
                    Log.d("Favorite COLOR:", colorString[0]);
                }
                View thisView = findViewById(R.id.drawer_layout);
                int color;
                try {
                    color = Color.parseColor(String.valueOf(colorString[0]));
                } catch(Exception e) {
                    color = Color.parseColor(String.valueOf("#FFFFFF"));
                }
                thisView.setBackgroundColor(color);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                View thisView = findViewById(R.id.mainViewForBG);
//                View rootOfThisView = thisView.getRootView();
//                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
//                if (thisUser != null) {
//                    String colorString = thisUser.getFavoriteColor();
//                    Log.d("Favorite COLOR:", colorString);
//                    int color = Color.parseColor(String.valueOf(colorString));
//                    rootOfThisView.setBackgroundColor(color);

                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
                if (thisUser != null) {
                    colorString[0] = thisUser.getFavoriteColor();
                    Log.d("Favorite COLOR:", colorString[0]);
                }
                View thisView = findViewById(R.id.drawer_layout);
                int color;
                try {
                    color = Color.parseColor(String.valueOf(colorString[0]));
                } catch(Exception e) {
                    color = Color.parseColor(String.valueOf("#FFFFFF"));
                }
                    thisView.setBackgroundColor(color);
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
        setContentView(R.layout.activity_base);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        //firebaseAuth = FirebaseAuth.getInstance();
        //firebaseReference = firebaseDBInstance.getReference();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, CredentialActivity.class));
        }
       // user=firebaseAuth.getCurrentUser();
        Log.d("Favorite  DEBUG, UID: ", user.getUid());
        firebaseReference.child("users").child(user.getUid()).child("userprofile").addChildEventListener(userListener);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        //Intent loginIntent = new Intent(this,LoginActivity.class);
                        if(menuItem.getItemId() == R.id.nav_home){
                            showMain();
                        }
                        if(menuItem.getItemId() == R.id.nav_logout){
                            logout();
                        }
                        if(menuItem.getItemId() == R.id.nav_favorites){
                            showFavorites();
                        }
                        if(menuItem.getItemId() == R.id.nav_services){
                            launchServices();
                        }
                        if(menuItem.getItemId() == R.id.nav_profile){
                            showProfile();


                        }
                        return true;
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setActivityLayout(int id) {
        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout linearLayout = findViewById(R.id.content_frame);
        View layout = findViewById(id);
        linearLayout.addView(inflater.inflate(id, null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

    }
    public void logout() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            return;
        }
            firebaseAuth.signOut();
            Intent logout = new Intent(BaseActivity.this, CredentialActivity.class);
            finish();
            startActivity(logout);
    }


    public void showFavorites() {
        Intent i = new Intent(this,FavoritesActivity.class);
        startActivity(i);
    }

    public void showMain() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    public void launchServices() {
        startActivity(new Intent(this, ServicesCardView.class));
    }
    public void showProfile(){
        startActivity(new Intent(this, ProfileActivity.class));
    }



}
