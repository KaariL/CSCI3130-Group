package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class CourseDisplayActivity extends BaseActivity {

    public FirebaseAuth firebaseAuth;
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;

    private String receivedSubject;
    private ListView classListView;
    private FirebaseListAdapter<Course> firebaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_course_display);

        receivedSubject = getIntent().getStringExtra("Selected Subject");
        if(receivedSubject == null){
            finish();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, CredentialActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();



        firebaseDBInstance = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDBInstance.getReference("courses");


        classListView = (ListView) findViewById(R.id.listView);

        Query query = firebaseReference.child(receivedSubject);
        Log.d("myTag", firebaseReference.child(receivedSubject).getKey());

        FirebaseListOptions<Course> options = new FirebaseListOptions.Builder<Course>()
                .setQuery(query, Course.class)
                .setLayout(android.R.layout.simple_list_item_1)
                .build();



        firebaseAdapter = new FirebaseListAdapter<Course>(options) {
            @Override
            protected void populateView(View v, Course model, int position) {
                TextView contactName = (TextView)v.findViewById(android.R.id.text1);
                contactName.setText(model.description);
            }
        };
        classListView.setAdapter(firebaseAdapter);
        firebaseAdapter.startListening();
        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // onItemClick method is called everytime a user clicks an item on the list
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course selection = (Course) firebaseAdapter.getItem(position);
                selection.name = firebaseAdapter.getRef(position).getKey();
                Log.d("myTag", selection.name);
                //firebaseDBInstance.getReference("users").child()
            }
        });


    }

    }
