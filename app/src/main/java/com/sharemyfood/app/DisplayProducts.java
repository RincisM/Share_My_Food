package com.sharemyfood.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DisplayProducts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    MyAdapter adapter;
    DatabaseReference rootNode = FirebaseDatabase.getInstance().getReference("Products");
    ArrayList<Model> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MyAdapter(this, list);
        recyclerView.setAdapter(adapter);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        navigationView.setCheckedItem(R.id.nav_receive);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        adapter.setOnItemClickListener(model -> showProductDetails(model));

        rootNode.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplayProducts.this, "Sorry Unable to Show you the Items Available", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void showProductDetails(Model model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Details");
        //Setting Dialog Message
        String message = "Name: "+ model.getName() + "\n" + "Description: "+ model.getDescription() + "\n"+ "Quantity: "+ model.getQuantity()+ "\n" + "Expiry Date: "+ model.getExpiry() + "\n"+ "Location: "+model.getLocation()+ "\n"+ "Phone: "+model.getPhone();
        builder.setMessage(message);

        // Add a Buy button
        builder.setPositiveButton("Get", (dialog, which) -> {
            // Show confirmation dialog
            showBuyConfirmationDialog(model);
        });

        //Button to dismiss the dialog
        builder.setNegativeButton("Ok", (dialog, which) -> dialog.dismiss());

        //Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        /*Intent intent = new Intent(this, DisplayProductDetails.class);
        intent.putExtra("product", model); //Passing the whole model object
        startActivity(intent);*/

    }
    private void showBuyConfirmationDialog(Model model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Get Confirmation");
        builder.setMessage("Are you sure you want to get this item?");

        // Add a delete button
        builder.setPositiveButton("Get", (dialog, which) -> {
            // Delete the item from Firebase database
            DatabaseReference itemRef = rootNode.child(model.getName());
            itemRef.removeValue();
            list.remove(model); //To remove the item from the list

            //Update the change to the Firebase Database
            rootNode.setValue(list);
            adapter.notifyDataSetChanged();

            Toast.makeText(DisplayProducts.this, "Have a Great Day", Toast.LENGTH_SHORT).show();
        });

        // Add a cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_receive) {
            // Handle nav_receive click
        } else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(this, UserProfile.class));
            finish();
        } else if (itemId == R.id.nav_give) {
            startActivity(new Intent(this, Products.class));
            finish();
        }
        else if (itemId == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Thank You", Toast.LENGTH_SHORT).show();
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}