package com.example.group3.csci3130_group3_project;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ServicesCardView extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_services_card_view);
        addNavBar();
        setActivityLayout(R.layout.activity_services_card_view);
        ArrayList<ExItem> exItems=new ArrayList<>();
        exItems.add(new ExItem(R.drawable.ic_home,"Facilities"));
        exItems.add(new ExItem(R.drawable.ic_local_cafe,"Food and Beverage"));
        exItems.add(new ExItem(R.drawable.ic_healing_black_24dp,"Health and Wellness"));
//        exItems.add(new ExItem(R.drawable.ic_home,"line4"));

        mRecyclerView =findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter=new ExAdapter(exItems);
        mLayoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }
}