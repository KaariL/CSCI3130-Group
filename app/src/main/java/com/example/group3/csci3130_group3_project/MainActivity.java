package com.example.group3.csci3130_group3_project;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void onSubmit(View view) {
        EditText nameEntered = findViewById(R.id.nameHolder);
        User u = new User(nameEntered.getText().toString());
        mDatabase.child("UserList").child("001").setValue(u);
    }
}
