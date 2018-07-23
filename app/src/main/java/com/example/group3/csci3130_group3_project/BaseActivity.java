package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
    }
    public void addNavBar() {
        setContentView(R.layout.activity_base);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
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
                        if(menuItem.getItemId() == R.id.nav_courses){
                            showCourses();
                        }
                        if(menuItem.getItemId() == R.id.nav_myCourses){
                            showMyCourses();
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
        finish();
        Intent i = new Intent(this,FavoritesActivity.class);
        startActivity(i);
    }

    public void showCourses() {
        finish();
        Intent i = new Intent(this,CoursesActivity.class);
        startActivity(i);
    }

    public void showMain() {
        finish();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    public void launchServices() {
        //finish();
        startActivity(new Intent(this, ServicesCardView.class));
    }
    public void showProfile(){
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
    public void showMyCourses(){
        finish();
        startActivity(new Intent(this, MyCoursesActivity.class));
    }



}
