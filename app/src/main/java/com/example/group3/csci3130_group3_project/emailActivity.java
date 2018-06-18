package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class emailActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail;
    private Button reset_bt;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        reset_bt = (Button) findViewById(R.id.button_reset);

        reset_bt.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();





    }
    /*
    *  if(task.isSuccessful())
                        {
                            Toast.makeText(this,"Email sent successful",Toast.LENGTH_LONG).show();
                        }
    * */

    @Override
    public void onClick(View v) {
        final String em = editTextEmail.getText().toString().trim();
        if (v == reset_bt)
        {

            firebaseAuth.sendPasswordResetEmail(em)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Reset email instructions sent to "+em, Toast.LENGTH_LONG).show();
                                finish();
                                Intent i  = new Intent(emailActivity.this, CredentialActivity.class);
                                i.putExtra("email",em);
                                startActivity(i);

                            } else {
                                Toast.makeText(getApplicationContext(), em + " does not exist", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }
}

