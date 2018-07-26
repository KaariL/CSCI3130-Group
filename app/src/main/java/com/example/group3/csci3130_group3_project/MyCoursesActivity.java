package com.example.group3.csci3130_group3_project;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MyCoursesActivity extends BaseActivity implements View.OnClickListener{
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> firebaseAdapter;
    private FirebaseRecyclerOptions<Course> options;
    private Query query;

    @Override
    public void onClick(View v) {

    }

    //Stuff required for onClick in RecyclerView

    class CourseViewHolder extends RecyclerView.ViewHolder{
        public final TextView courseItemView;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        public CourseViewHolder(View itemView){
            super (itemView);
            courseItemView = (TextView) itemView.findViewById(R.id.courses_element);
            if (courseItemView != null) {
                courseItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Course selectedCourse = (Course) v.getTag();
                        if (selectedCourse != null) {
                            showDialog(selectedCourse, user);

                        }

                    }
                });
            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_my_courses);


        //Set-up Firebase

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        firebaseDBInstance = FirebaseDatabase.getInstance();
        firebaseReference =  firebaseDBInstance.getReference();

        //Code to add recycler View below
        mRecyclerView = (RecyclerView) findViewById(R.id.courses_recyclerview);
        query = firebaseDBInstance.getReference().child("users").child(uid).child("courses");
        options = new FirebaseRecyclerOptions.Builder<Course>().setQuery(query, Course.class).build();
        firebaseAdapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>(options) {

            @Override
            public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courseslist_item, parent, false);

                return new CourseViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(CourseViewHolder holder, int position, Course model){
                String mCourseName = model.description + " (" + model.code + ")";
                holder.courseItemView.setText(mCourseName);
                holder.courseItemView.setTag(model);
            }

        };
        mRecyclerView.setAdapter(firebaseAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAdapter != null)
            firebaseAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        firebaseAdapter.stopListening();
    }

    public void showDialog(Course c, FirebaseUser user){
        final Course selectedCourse = c;
        final String uid = user.getUid();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(c.code +"\n"+ c.description);
        builder.setMessage(selectedCourse.description);
        builder.setNegativeButton("Remove Course", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
              DatabaseReference root = firebaseDBInstance.getReference();
              root.child("users").child(uid).child("courses").child(selectedCourse.code).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Write was successful!
                                Log.d("Remove Course", "Write Successful");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Write failed
                                // ...
                                String error = e.getMessage() + ": " + e.getCause();
                                Log.d("remove course", "Write Failed: " + error);
                            }
                        });

            }
        });
        builder.setPositiveButton("Navigate", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MyCoursesActivity.this, MainActivity.class);
                intent.putExtra("my course", selectedCourse);
                startActivity(intent);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        Dialog alertDialog = builder.create();
        alertDialog.show();
    }

}
