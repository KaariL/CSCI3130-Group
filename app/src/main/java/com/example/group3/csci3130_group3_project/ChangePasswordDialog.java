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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;


public class ChangePasswordDialog extends DialogFragment {


    public ChangePasswordDialog() {
        // Required empty public constructor
    }
    //EditText oldPassField;
    EditText newPassField, confirmPassField;

    String uid;
    private String userEmail;
    //String oldPassword;
    String newPassword;
    String confirmPassword;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        if (uid == null)
            dismiss();
        userEmail = user.getEmail();



        builder.setView(inflater.inflate(R.layout.fragment_change_password_dialog, null)).
                setMessage(R.string.changePass_message);

        builder.setPositiveButton(R.string.changePass_pos, new DialogInterface.OnClickListener() {

            //Click the positive action button
            //navigate
            public void onClick(DialogInterface dialog, int id) {
              //  oldPassField = (EditText) getDialog().findViewById(R.id.changePass_oldPassword);
                newPassField = (EditText) getDialog().findViewById(R.id.changePass_newPassword);
                confirmPassField = (EditText) getDialog().findViewById(R.id.changePass_confirmNewPassword);

              //  oldPassword = oldPassField.getText().toString();
                newPassword = newPassField.getText().toString();
                confirmPassword = confirmPassField.getText().toString();


               /* AuthCredential credential = EmailAuthProvider
                        .getCredential(userEmail, oldPassword);
                firebaseAuth.getCurrentUser().reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User re-authenticated.");
                                setUserToAuthenticated();
                                if(userConfirmed){
                                    Log.d("user confirmed", "true");
                                }else{
                                    Log.d("user confirmed", "false");
                                }
                            }
                        }); */

                    if(newPasswordIsValid(newPassword, confirmPassword)) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "password changed");
                                    dismiss();
                                }else{
                                    Log.d(TAG, "password not changed");
                                }
                            }

                        });
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

    public boolean newPasswordIsValid(String newPassword, String confirmPassword){
        registrationValidator validator = new registrationValidator();
        boolean passwordsMatch = validator.passwordsMatch(newPassword, confirmPassword);
        boolean passwordStrongEnough = validator.passwordIsStrong(newPassword);
        return (passwordsMatch && passwordStrongEnough);
    }



}