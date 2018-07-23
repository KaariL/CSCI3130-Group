package com.example.group3.csci3130_group3_project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class ModifyFavoriteDialogue extends DialogFragment {
    //firebase
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;
    //
    Favorite selectedFavorite;

    public void setSelectedFavorite(Favorite favorite) {
        selectedFavorite = favorite;
    }


    String uid;
    String favId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        if (uid == null)
            dismiss();
        if(selectedFavorite == null){
            dismiss();
        }


        builder.setView(inflater.inflate(R.layout.fragment_modify_favorite_dialogue, null)).
                setMessage(selectedFavorite.getName());
        builder.setPositiveButton(R.string.fav_modPos, new DialogInterface.OnClickListener() {

                    //Click the positive action button
                    //navigate
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        Log.d("dialog put:", selectedFavorite.getName());
                        intent.putExtra("Favorite", selectedFavorite);
                        getContext().startActivity(intent);
                    }
                })
                .setNeutralButton(R.string.fav_modNeut, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.fav_modNeg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseDBInstance = FirebaseDatabase.getInstance();
                        firebaseReference =  firebaseDBInstance.getReference();
                        favId = selectedFavorite.getId();
                        firebaseReference.child("users").child(uid).child("favorites").child(selectedFavorite.getName()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        Log.d("remove", "Write Successful");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        // ...
                                        String error = e.getMessage() + ": " + e.getCause();
                                        Log.d("remove", "Write Failed: " + error);
                                    }
                                });

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();

    }

}