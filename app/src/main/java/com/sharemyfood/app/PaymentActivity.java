package com.sharemyfood.app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class PaymentActivity extends NavigationActivity {

    private TextInputLayout amountInput;
    private Button donate;
    private static final int TEZ_REQUEST_CODE = 123;

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

    private ActivityResultLauncher<Intent> paymentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amountInput = findViewById(R.id.donateAmount);
        donate = findViewById(R.id.donateButton);

        paymentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Log.d("result", result.getData().getStringExtra("Status"));
            } else {
                Toast.makeText(this, "Payment Failure. Sorry", Toast.LENGTH_SHORT).show();
            }
        });

        donate.setOnClickListener(v -> {
            String amount = amountInput.getEditText().getText().toString().trim();
            if (amount.isEmpty()) {
                Toast.makeText(this, "Your Contribution?", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "test@axisbank")
                                .appendQueryParameter("pn", "Share-My-Food")
                                .appendQueryParameter("mc", "1234")
                                .appendQueryParameter("tr", "123456789")
                                .appendQueryParameter("tn", "Donation")
                                .appendQueryParameter("am", amount)
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "https://test.merchant.website")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // There is an app available to handle the Intent
                    paymentLauncher.launch(intent);
                } else {
                    // No UPI payment app is available, show a message to the user
                    Toast.makeText(this, "Please install a UPI payment app to proceed with the donation.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}