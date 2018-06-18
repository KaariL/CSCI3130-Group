package com.example.group3.csci3130_group3_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class CredentialActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login_bt;
    private Button regs_bt;
    private Button fid_bt;
    private EditText psw_editText;
    private EditText userN_editText;
    private ProgressDialog progressDialog;
    private InputMethodManager imm;
    private FirebaseAuth firebaseAuth;
    public static CredentialActivity credentialPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        credentialPage = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential);

        firebaseAuth=FirebaseAuth.getInstance();
     /*   if(firebaseAuth.getCurrentUser()!=null)
        {
            //login state
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        } */

        psw_editText=(EditText) findViewById(R.id.editText_psw);
        userN_editText=(EditText) findViewById(R.id.editText_name);
        fid_bt=(Button) findViewById(R.id.button_fid);
        regs_bt=(Button) findViewById(R.id.button_regs);
        login_bt=(Button)findViewById(R.id.button_login);
        progressDialog=new ProgressDialog(this);
        login_bt.setOnClickListener(this);
        regs_bt.setOnClickListener(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent i = getIntent();
        userN_editText.setText(i.getStringExtra("email"));

    }

    private  void user_login()
    {
        String userN=userN_editText.getText().toString().trim();
        String psw=psw_editText.getText().toString().trim();

        //check input states
        if(TextUtils.isEmpty(userN))
        {
            Toast.makeText(this,"Enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(psw))
        {
            Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Login .....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userN,psw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    finish();


                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else
                {
                    psw_editText.setText(null);
                    Toast.makeText(getApplicationContext(),"Enter corrected password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void findPassword()
    {

        startActivity(new Intent(getApplicationContext(),emailActivity.class));
    }

    public void openRegistrationForm(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(psw_editText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(userN_editText.getWindowToken(), 0);
        if (v==login_bt)
        {

            user_login();
        }

        if (v==regs_bt)
        {
            openRegistrationForm(v);
        }
        if(v==fid_bt)
        {
            findPassword();
        }
    }

}
