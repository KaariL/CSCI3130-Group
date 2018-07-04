package com.example.group3.csci3130_group3_project;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class FavoritesActivity extends BaseActivity {
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Favorite, FavoriteViewHolder> firebaseAdapter;
    private FirebaseRecyclerOptions<Favorite> options;
    private Query query;

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public final TextView favoriteItemView;
        public FavoriteViewHolder(View itemView){
            super (itemView);
            favoriteItemView = (TextView) itemView.findViewById(R.id.element);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_favorites);



        //Set-up Firebase
        firebaseDBInstance = FirebaseDatabase.getInstance();
        firebaseReference =  firebaseDBInstance.getReference("Favorites");

        //Code to add recycler View below
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        options = new FirebaseRecyclerOptions.Builder<Favorite>().setQuery(query, Favorite.class).build();
        query = firebaseDBInstance.getReference().child("Favorites");
        firebaseAdapter = new FirebaseRecyclerAdapter<Favorite, FavoriteViewHolder>(options) {
            @Override
            public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favoritelist_item, parent, false);
                return new FavoriteViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(FavoriteViewHolder holder, int position, Favorite model){
                String mFavoriteName = model.getName();
                holder.favoriteItemView.setText(mFavoriteName);
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
}
