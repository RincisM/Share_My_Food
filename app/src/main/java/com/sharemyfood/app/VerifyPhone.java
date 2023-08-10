package com.sharemyfood.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    PhoneAuthProvider.ForceResendingToken mResendToken;
    String verificationCodeBySystem;
    EditText otp;
    Button verify;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String name, email, password, phone, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference("users");
        setContentView(R.layout.activity_verify_phone);
        otp = findViewById(R.id.otpEntered);
        verify = findViewById(R.id.verify_button);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        sendVerificationCodeToUser(phone);
        verify.setOnClickListener(v -> {
            String code = otp.getText().toString();
            if(code.isEmpty() || code.length()<6) {
                otp.setError("Wrong OTP");
                otp.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            verifyCode(code);
        });
    }

    private void sendVerificationCodeToUser(String phone) {
        mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken token){
            //What if the code has been sent but it is not the same device, If it is in the same device oVerificationComplete function will be executed, otherwise this function will execute
            verificationCodeBySystem = verificationID;
            mResendToken = token;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            String code =credential.getSmsCode();
            if(code!=null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Toast.makeText(VerifyPhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhone.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        // Update UI
                        //Storing Data in Firebase
                        UserHelperClass helperClass = new UserHelperClass(name, username, email, password, phone);
                        reference.child(username).setValue(helperClass); //In setValue whatever we pass will be stored inside the database. Child is added to set a difference between one user and the other
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //This will not allow the user to go back to the otp page after login
                        startActivity(intent);
                    } else {
                        // Sign in failed, display a message and update the UI
                        Toast.makeText(VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}