package com.sharemyfood.app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    Button oldUserButton, signUpButton;
    TextInputLayout nameReg, userNameReg, emailReg, passwordReg, phoneReg;
    FirebaseDatabase rootNode;
    DatabaseReference reference; //Reference will be sub-elements of the root node
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        oldUserButton = findViewById(R.id.oldUser);
        signUpButton = findViewById(R.id.signUp);
        nameReg = findViewById(R.id.nameReg);
        userNameReg = findViewById(R.id.userNameReg);
        emailReg = findViewById(R.id.emailReg);
        passwordReg = findViewById(R.id.passwordReg);
        phoneReg = findViewById(R.id.phoneReg);

        oldUserButton.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private Boolean validationName() {
        //Get all the values
        String val = nameReg.getEditText().getText().toString();
        if(val.isEmpty()) {
            nameReg.setError("Field cannot be Empty");
            return false;
        } else {
            nameReg.setError(null); //It will remove the error if it occurred already
            nameReg.setErrorEnabled(false); //It will remove the space which the error caused
            return true;
        }
    }

    private Boolean validationUserName() {
        //Get all the values
        String val = userNameReg.getEditText().getText().toString();
        String noWhiteSpacePattern ="^\\S+$";
        if(val.isEmpty()) {
            userNameReg.setError("Field cannot be Empty");
            return false;
        }
        else if(val.length()>=15) {
            userNameReg.setError("Username too Long");
            return false;
        }
        else if(!val.matches(noWhiteSpacePattern)) {
            userNameReg.setError("White Spaces are not allowed");
            return false;
        }
        else {
            userNameReg.setError(null); //It will remove the error if it occurred already
            userNameReg.setErrorEnabled(false); //It will remove the space which the error caused
            return true;
        }
    }

    private Boolean validationEmail() {
        //Get all the values
        String val = emailReg.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._]+@[a-zA-Z]+\\.+[a-zA-Z]+";
        if(val.isEmpty()) {
            emailReg.setError("Field cannot be Empty");
            return false;
        }
        else if(!val.matches(emailPattern)) {
            emailReg.setError("Invalid Email Address");
            return false;
        }
        else {
            emailReg.setError(null);
            emailReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validationPassword() {
        //Get all the values
        String val = passwordReg.getEditText().getText().toString();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" + //At-least One Digit
                //"(?=.*[a-z])" + //At-least One LowerCase Letter
                //"(?=.*[A-Z])" + //At-least One UpperCase Letter
                "(?=.*[a-zA-Z])" + //Any Letter
                "(?=.*[@#$%^&+=])" + //At-least One Special Character
                "\\S+" + //No White Spaces
                ".{6,}" + //At-least Six Characters
                "$";

        if(val.isEmpty()) {
            passwordReg.setError("Field cannot be Empty");
            return false;
        }
        else if(!val.matches(passwordPattern)) {
            passwordReg.setError("Password is too Weak");
            return false;
        }
        else {
            passwordReg.setError(null);
            passwordReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validationPhone() {
        //Get all the values
        String val = phoneReg.getEditText().getText().toString();

        if(val.isEmpty()) {
            phoneReg.setError("Field cannot be Empty");
            return false;
        } else {
            phoneReg.setError(null);
            phoneReg.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View view) {
        if(!validationName() | !validationUserName() | !validationEmail() | !validationPassword() | !validationPhone()) {
            return;
        }

        rootNode = FirebaseDatabase.getInstance(); //It will call the root node which is in the cloud storage
        reference = rootNode.getReference("users"); //Inside the getReference we have to mention the path of the sub-element
        //Get all the values
        String name = nameReg.getEditText().getText().toString();
        String username = userNameReg.getEditText().getText().toString();
        String email = emailReg.getEditText().getText().toString();
        String password = passwordReg.getEditText().getText().toString();
        String phone = phoneReg.getEditText().getText().toString();

        /*//Storing Data in Firebase
        UserHelperClass helperClass = new UserHelperClass(name, username, email, password, phone);
        reference.child(username).setValue(helperClass); //In setValue whatever we pass will be stored inside the database. Child is added to set a difference between one user and the other*/

        Intent intent = new Intent(getApplicationContext(), VerifyPhone.class);
        intent.putExtra("name", name);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("phone", phone);
        startActivity(intent);


        //Toast.makeText(this, "Account created Successfully", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();*/

    }
}