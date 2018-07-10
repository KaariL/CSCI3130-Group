package com.example.group3.csci3130_group3_project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;


public class AddFavoriteDialog extends DialogFragment {
    //firebase
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;
    //
    LatLng newLocation;
    public void setNewLocation(LatLng coordinates){
        newLocation = coordinates;
    }
    EditText nicknameField;
    String uid;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        if(uid == null)
            dismiss();
        builder.setView(inflater.inflate(R.layout.fragment_add_favorite_dialog, null)).
        setMessage(R.string.fav_addPrompt)
                .setPositiveButton(R.string.fav_addPos, new DialogInterface.OnClickListener() {
                    //Click the positive action button
                    public void onClick(DialogInterface dialog, int id) {
                        // Add to favorites
                        nicknameField = (EditText) getDialog().findViewById(R.id.fav_nickname);
                        if(newLocation != null){
                            String nickname = nicknameField.getText().toString();
                            if(nickname != null) {
                                firebaseDBInstance = FirebaseDatabase.getInstance();
                                firebaseReference =  firebaseDBInstance.getReference();
                                //Add to favorite list
                                String favoriteId = firebaseReference.child("users").child(uid).child("favorites").child("favorite").push().getKey();
                                Favorite newFavorite = new Favorite(favoriteId, nickname, newLocation);

                                //Success listeners
                                firebaseReference.child("users").child(uid).child("favorites").child(favoriteId).setValue(newFavorite)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Write was successful!
                                                Log.d(TAG, "Write Successful");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Write failed
                                                // ...
                                                String error = e.getMessage() + ": " + e.getCause();
                                                Log.d(TAG, "Write Failed: " + error);
                                            }
                                        });

                            }
                        }
                    }
                })
                .setNegativeButton(R.string.fav_addNeg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
