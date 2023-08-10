package com.sharemyfood.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends NavigationActivity {
    TextInputLayout fullName, email, phone, password;
    ImageView shareLogo;
    String user_fullName, user_userName, user_email, user_password, user_phone;
    DatabaseReference reference;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        reference = FirebaseDatabase.getInstance().getReference("users");
        //Hooks

        fullName = findViewById(R.id.fullNameProfile);
        email = findViewById(R.id.emailProfile);
        phone = findViewById(R.id.phoneProfile);
        password = findViewById(R.id.passwordProfile);
        shareLogo = findViewById(R.id.share_image);

        String userEmail = getIntent().getStringExtra("userEmail");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        shareLogo.setOnClickListener(v -> {
            startActivity(new Intent(this, DisplayProducts.class));
            finish();
        });
        
        //ShowAllData
        showAllUserData();
    }

    private void showAllUserData() {
        Intent intent = getIntent();
        user_fullName = intent.getStringExtra("name");
        user_userName = intent.getStringExtra("username");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");

        //To put the data in the boxes
        fullName.getEditText().setText(user_fullName);
        email.getEditText().setText(user_email);
        phone.getEditText().setText(user_phone);
        password.getEditText().setText(user_password);
    }


    public void updateUser(View view) {
        if(isNameChanged() || isPasswordChanged() || isEmailChanged()) {
            Toast.makeText(this, "Data is Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data cannot be updated.", Toast.LENGTH_SHORT).show();
        }
    }
    public void logout(View view) {
        // Here, you can perform any necessary logout actions, such as clearing session data or signing out the user.
        // For example, if you are using Firebase Authentication, you can sign out the user like this:
        FirebaseAuth.getInstance().signOut();

        // After logout, navigate to the login page (Assuming your login activity is named LoginActivity)
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the current activity to prevent the user from going back to the profile page after logout.
    }

    private boolean isEmailChanged() {
        if(!user_email.equals(email.getEditText().getText().toString())) {
            reference.child(user_email).child("email").setValue(email.getEditText().getText().toString());
            user_email = email.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPasswordChanged() {
        if(!user_password.equals(password.getEditText().getText().toString())) {
            reference.child(user_userName).child("password").setValue(password.getEditText().getText().toString());
            user_password = password.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        if(!user_fullName.equals(fullName.getEditText().getText().toString())) {
            reference.child(user_userName).child("name").setValue(fullName.getEditText().getText().toString());
            user_fullName = fullName.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }
}