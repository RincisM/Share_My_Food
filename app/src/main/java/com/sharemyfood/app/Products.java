package com.sharemyfood.app;

import com.google.android.gms.location.FusedLocationProviderClient;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class Products extends NavigationActivity {

    Button giveProductButton, showProductsButton, donate;
    ImageView product;
    ProgressBar progressBar;
    TextInputLayout productName, productDescription, productQuantity, productExpiryDate, prodLocat, prodPho;

    DatabaseReference rootNode = FirebaseDatabase.getInstance().getReference("Products");
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    Uri imageURI;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    /*private void checkLocationPermissions() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permissions are already granted, proceed to get the location
            getLocation();
        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permissions granted, get the location
                getLocation();
            } else {
                // Location permissions denied, handle it accordingly (e.g., show a message)
                Toast.makeText(this, "Location permissions are required to access the user's location.", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    /*private void getLocation() {
        // Check for permissions again (in case the user didn't grant them)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the last known location from FusedLocationProviderClient
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got the location!
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            // Do something with the latitude and longitude
                            // For example, you can store them in the Model object or use them as needed
                        } else {
                            // Location is null (e.g., location services are disabled)
                            // Handle this case as needed
                            Toast.makeText(this, "Failed to get location. Please make sure location services are enabled.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Location permissions are not granted
            // Handle this case as needed (e.g., show a message or request permissions again)
            Toast.makeText(this, "Location permissions are required to access the user's location.", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        product = findViewById(R.id.imageView);
        giveProductButton = findViewById(R.id.giveProduct);
        showProductsButton = findViewById(R.id.showProducts);
        progressBar = findViewById(R.id.progressBar);
        productName = findViewById(R.id.prodName);
        productDescription = findViewById(R.id.prodDescription);
        productQuantity = findViewById(R.id.prodQuantity);
        productExpiryDate = findViewById(R.id.prodExpiryDate);
        progressBar.setVisibility(View.INVISIBLE);
        donate = findViewById(R.id.donateButton);
        prodLocat = findViewById(R.id.prodLocation);
        prodPho = findViewById(R.id.prodPhone);

        donate.setOnClickListener(v-> {
            startActivity(new Intent(this, PaymentActivity.class));
        });

        productExpiryDate.getEditText().setOnClickListener(v -> selectDate());

        showProductsButton.setOnClickListener(v -> {
            startActivity(new Intent(Products.this, DisplayProducts.class));
            finish();
        });


        /*product.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 2);
        });*/

        ActivityResultLauncher<String> galleryLauncher;

        // Initialize the ActivityResultLauncher in your onCreate() or onViewCreated() method
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        // Handle the result here
                        imageURI = result;
                        product.setImageURI(imageURI);

                        // Process the data as needed
                    }
                });

        // Update your onClickListener to use the ActivityResultLauncher
        product.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            galleryLauncher.launch("image/*");
        });

        giveProductButton.setOnClickListener(v -> {
//            if(imageURI != null) {
//                String name = productName.getEditText().getText().toString().trim();
//                String description = productDescription.getEditText().getText().toString().trim();
//                String date = productExpiryDate.getEditText().getText().toString().trim();
//                String quantity = productQuantity.getEditText().getText().toString().trim();
//
//                // Create a new instance of the Model class
//                Model model = new Model();
//
//                // Set the values in the Model object
//                model.setName(name);
//                model.setDescription(description);
//                model.setExpiry(date);
//                model.setQuantity(quantity);
//                uploadToFirebase(imageURI, model);
//
//            }
//            else {
//                Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
//            }
            if (imageURI != null) {
                String name = productName.getEditText().getText().toString().trim();
                String description = productDescription.getEditText().getText().toString().trim();
                String date = productExpiryDate.getEditText().getText().toString().trim();
                String quantity = productQuantity.getEditText().getText().toString().trim();
                String location = prodLocat.getEditText().getText().toString().trim();
                String phone = prodPho.getEditText().getText().toString().trim();

                // Create a new instance of the Model class
                Model model = new Model();

                // Set the values in the Model object
                model.setName(name);
                model.setDescription(description);
                model.setExpiry(date);
                model.setQuantity(quantity);
                model.setLocation(location);
                model.setPhone(phone);
                uploadToFirebase(imageURI, model);

                /*// Check and request location permissions
                checkLocationPermissions();*/
            } else {
                Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectDate() {
        // Get the current date from the EditText, if available
        String currentDate = productExpiryDate.getEditText().getText().toString().trim();

        // Create a calendar instance to set the initial date in the dialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Products.this, (view, year1, monthOfYear, dayOfMonth1) -> {
            // Update the EditText with the selected date
            String selectedDate = (monthOfYear + 1) + "/" + dayOfMonth1 + "/" + year1;
            productExpiryDate.getEditText().setText(selectedDate);
        }, year, month, dayOfMonth);

        // Show the dialog
        datePickerDialog.show();
    }

    private void uploadToFirebase(Uri imageURI, Model model) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(imageURI));
        fileRef.putFile(imageURI).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                model.setImageUrl(uri.toString());

                // Generate a unique key for the model
                String modelId = rootNode.push().getKey();

                // Upload the Model object to Firebase
                assert modelId != null;
                rootNode.child(modelId).setValue(model);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Products.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                product.setImageResource((R.drawable.baseline_add_photo_alternate_24));
            });
        }).addOnProgressListener(snapshot -> {
            progressBar.setVisibility(View.VISIBLE);
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Uploading Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileExtension(Uri imageURI) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType((imageURI)));
    }
}