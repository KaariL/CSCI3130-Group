package com.example.group3.csci3130_group3_project;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {
    protected registrationValidator validator;
    protected boolean formIsValid;
    private FirebaseAuth mAuth;
    private static final String TAG = "Register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        validator = new registrationValidator();
        formIsValid = true;
    }

    protected void createNewUser(String email, String password){
        if(!formIsValid){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                            FirebaseUser user = mAuth.getCurrentUser();
                            RegisterActivity.this.finish();
                            CredentialActivity.credentialPage.finish();
                            startActivity(main);

                        }
                        else {
                            Log.w(TAG, "createUserWithEmail:failed");
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "This email is already in use. Please try again", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authenitifcation failed",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    public void validateForm(View view){
        this.restoreFieldDefaults();
        formIsValid = true;

        if(!validator.emailIsValid(this.getEmailField())){
            TextView email = findViewById((R.id.emailText));
            email.setText(R.string.emailRegError);
            email.setTextColor(Color.RED);
            this.formIsValid = false;
        }
        if(!validator.passwordIsStrong(this.getPasswordField())){
            TextView password = findViewById(R.id.passwordText);
            password.setText(R.string.passwordRegErr);
            password.setTextColor(Color.RED);
            this.formIsValid = false;
        }
        if(!validator.passwordsMatch(this.getPasswordField(), this.getConfirmPasswordField())){
            TextView confirmPass = findViewById(R.id.confirmPasswordText);
            confirmPass.setText(R.string.confirmPassRegErr);
            confirmPass.setTextColor(Color.RED);
            this.formIsValid = false;
        }
        if(formIsValid){
            createNewUser(this.getEmailField(), getPasswordField());
        }

    }
    public void restoreFieldDefaults(){
        TextView email = findViewById(R.id.emailText);
        TextView password = findViewById(R.id.passwordText);
        TextView confirmPass = findViewById(R.id.confirmPasswordText);
        email.setText(R.string.emailRegDefault);
        email.setTextColor(getResources().getColor(R.color.colorPrimary));
        password.setText(R.string.passwordRegDefault);
        password.setTextColor(getResources().getColor(R.color.colorPrimary));
        confirmPass.setText(R.string.confirmPassRegDefault);
        confirmPass.setTextColor(getResources().getColor(R.color.colorPrimary));

    }
    public String getEmailField(){
        EditText email = (EditText) findViewById(R.id.email);
        return email.getText().toString();
    }
    public String getPasswordField(){
        EditText password = (EditText) findViewById(R.id.password);
        return password.getText().toString();
    }
    public String getConfirmPasswordField(){
        EditText confirmPass = (EditText) findViewById(R.id.confirmPassword);
        return confirmPass.getText().toString();
    }

}
