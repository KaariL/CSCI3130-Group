package com.example.group3.csci3130_group3_project;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    protected registrationValidator validator;
    protected boolean formIsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        validator = new registrationValidator();
        formIsValid = true;
    }


    public void validateForm(View view){
        this.restoreFieldDefaults();
        if(!validator.usernameIsValid(this.getUsernameField())){
            TextView username = findViewById(R.id.usernameText);
            username.setText(R.string.userRegErr);
            username.setTextColor(Color.RED);
            this.formIsValid = false;
        }
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

    }
    public void restoreFieldDefaults(){
        TextView user = findViewById(R.id.usernameText);
        TextView email = findViewById(R.id.emailText);
        TextView password = findViewById(R.id.passwordText);
        TextView confirmPass = findViewById(R.id.confirmPasswordText);
        user.setText(R.string.userRegDefault);
        user.setTextColor(getResources().getColor(R.color.colorPrimary));
        email.setText(R.string.emailRegDefault);
        email.setTextColor(getResources().getColor(R.color.colorPrimary));
        password.setText(R.string.passwordRegDefault);
        password.setTextColor(getResources().getColor(R.color.colorPrimary));
        confirmPass.setText(R.string.confirmPassRegDefault);
        confirmPass.setTextColor(getResources().getColor(R.color.colorPrimary));

    }
    public String getUsernameField(){
       EditText username = (EditText) findViewById(R.id.username);
       return username.getText().toString();
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
