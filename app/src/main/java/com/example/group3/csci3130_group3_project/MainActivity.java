package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity{
    FirebaseAuth firebaseAuth;
    Button logout_bt;
    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_main);
      //  ensureUserSignIn();
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this, CredentialActivity.class));
        }

        FirebaseUser user=firebaseAuth.getCurrentUser();
        userName = findViewById(R.id.userEmail);
        userName.setText(user.getEmail());
        /*logout_bt = (Button)findViewById(R.id.nav_logout);
        if(logout_bt != null) {
            logout_bt.setOnClickListener(this);
        }
    }
    public void onClick(View view){
        if(view == logout_bt){
            Toast.makeText(MainActivity.this, "A button that works", Toast.LENGTH_SHORT).show();
        }*/
    }


}
