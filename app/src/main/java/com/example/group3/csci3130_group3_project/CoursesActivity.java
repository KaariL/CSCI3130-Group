package com.example.group3.csci3130_group3_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CoursesActivity extends BaseActivity {

    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;

    private ListView subjectListView;
    private ArrayList<String> Subjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_courses);


        firebaseReference = firebaseDBInstance.getInstance().getReference().child("courses");
        Subjects = new ArrayList<String>();
        subjectListView = findViewById(R.id.listView);

        firebaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (  DataSnapshot  postSnapshot: dataSnapshot.getChildren())
                {
                    Subjects.add(postSnapshot.getKey());
                    Log.d("myTag",postSnapshot.getKey());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        Subjects );

                subjectListView.setAdapter(arrayAdapter);
                subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("myTag", Subjects.get(position));
                        goToCourseList(Subjects.get(position));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public Context getActivity(){
        return CoursesActivity.this;
    }

    public void goToCourseList(String S){
        Intent intent = new Intent(this, CourseDisplayActivity.class);
        intent.putExtra("Selected Subject",S);
        startActivity(intent);
    }
}
