package com.sharemyfood.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button signUp, signIn, forgetPassword;
    ImageView image;
    TextView loginHeader, signInText;
    TextInputLayout userNameText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        signUp = findViewById(R.id.newUser);
        loginHeader = findViewById(R.id.LoginHeader);
        signInText = findViewById(R.id.SignInText);
        userNameText = findViewById(R.id.userNameText);
        passwordText = findViewById(R.id.passwordText);
        forgetPassword = findViewById(R.id.ForgetPassword);
        signIn = findViewById(R.id.signIn);

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<View, String>(loginHeader, "logo_text");
            pairs[1] = new Pair<View, String>(signInText, "sign_text_tran");
            pairs[2] = new Pair<View, String>(userNameText, "email_tran");
            pairs[3] = new Pair<View, String>(passwordText, "password_tran");
            pairs[4] = new Pair<View, String>(forgetPassword, "forget_pass_otp_tran");
            pairs[5] = new Pair<View, String>(signIn, "signIn_button_tran");
            pairs[6] = new Pair<View, String>(signUp, "newUser_button_tran");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intent, options.toBundle());
        });
    }

    private Boolean validationUserName() {
        //Get all the values
        String val = userNameText.getEditText().getText().toString();
        if(val.isEmpty()) {
            userNameText.setError("Field cannot be Empty");
            return false;
        }
        else {
            userNameText.setError(null);
            userNameText.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validationPassword() {
        //Get all the values
        String val = passwordText.getEditText().getText().toString();

        if(val.isEmpty()) {
            passwordText.setError("Field cannot be Empty");
            return false;
        }
        else {
            passwordText.setError(null);
            passwordText.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        if(!validationUserName() | !validationPassword()) {
            return;
        }
        else {
            isUser();
        }
    }

    private void isUser() {
        String userEnteredUserName = userNameText.getEditText().getText().toString().trim();
        String userEnteredPassword = passwordText.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUserName);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userNameText.setError(null);
                    userNameText.setErrorEnabled(false);
                    String passwordFromDB = snapshot.child(userEnteredUserName).child("password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)) {
                        passwordText.setError(null);
                        passwordText.setErrorEnabled(false);

                        //If the Password Matches, get all the values from the Database
                        String nameFromDB = snapshot.child(userEnteredUserName).child("name").getValue(String.class);
                        String userNameFromDB = snapshot.child(userEnteredUserName).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUserName).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(userEnteredUserName).child("phone").getValue(String.class);

                        //Pass the above values to the profiles activity
                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", userNameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        passwordText.setError("Wrong Password");
                        passwordText.requestFocus();
                    }
                }
                else {
                    userNameText.setError("No such User Exists");
                    userNameText.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //We will have error methods
                Toast.makeText(LoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}