package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class FavoritesActivity extends BaseActivity implements View.OnClickListener{
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Favorite, FavoriteViewHolder> firebaseAdapter;
    private FirebaseRecyclerOptions<Favorite> options;
    private Query query;

    @Override
    public void onClick(View v) {

    }

    //Stuff required for onClick in RecyclerView

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        public final TextView favoriteItemView;
        public FavoriteViewHolder(View itemView){
            super (itemView);
            favoriteItemView = (TextView) itemView.findViewById(R.id.element);
            favoriteItemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Favorite selectedFavorite = (Favorite)v.getTag();
                    if(selectedFavorite != null) {
                        ModifyFavoriteDialogue modifyFavoriteDialogue = new ModifyFavoriteDialogue();
                        modifyFavoriteDialogue.setSelectedFavorite(selectedFavorite);
                        Log.d("favorite pass", String.format("latitude %f longitude %f",selectedFavorite.getmLatitude(),selectedFavorite.getmLongitude()));
                        modifyFavoriteDialogue.show(getSupportFragmentManager(), "Favorite");
                    }
                    else{
                        Toast.makeText(FavoritesActivity.this, "That didn't work.", Toast.LENGTH_LONG);
                    }
                }
            });
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_favorites);


        //Set-up Firebase

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        firebaseDBInstance = FirebaseDatabase.getInstance();
        firebaseReference =  firebaseDBInstance.getReference();

        //Code to add recycler View below
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        query = firebaseDBInstance.getReference().child("users").child(uid).child("favorites");
        options = new FirebaseRecyclerOptions.Builder<Favorite>().setQuery(query, Favorite.class).build();
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
                holder.favoriteItemView.setTag(model);
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

}
