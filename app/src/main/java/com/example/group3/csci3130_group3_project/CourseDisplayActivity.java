package com.example.group3.csci3130_group3_project;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import static android.content.ContentValues.TAG;

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
        final FirebaseUser user = firebaseAuth.getCurrentUser();



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
                contactName.setText(model.description+" ("+model.code+")");
            }
        };
        classListView.setAdapter(firebaseAdapter);
        firebaseAdapter.startListening();
        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // onItemClick method is called everytime a user clicks an item on the list
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course selection = (Course) firebaseAdapter.getItem(position);
                popUP(selection, user);
            }
        });


    }

    public void popUP(Course c, FirebaseUser user){
        final Course selectedCourse = c;
        final String uid = user.getUid();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(c.code +"\n"+ c.description);
        builder.setMessage("Please choose one of these options:");
        builder.setNegativeButton("Navigate", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                goToMain(selectedCourse);
            }
        });
        builder.setPositiveButton(R.string.coursedisplay_addPrompt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference rootReference = firebaseDBInstance.getReference();

                String courseId = rootReference.child("users").child(uid).child("courses").child("course").push().getKey();
                rootReference.child("users").child(uid).child("courses").child(selectedCourse.code).setValue(selectedCourse)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Write was successful!
                                Log.d("Add new course:", "Write Successful");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Write failed
                                // ...
                                String error = e.getMessage() + ": " + e.getCause();
                                Log.d("Add new course:", "Write Failed: " + error);
                            }
                        });
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        Dialog alertDialog = builder.create();
        //alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void goToMain(Course c){
        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra("Course Sent", c);
        startActivity(intent);
    }

    }
