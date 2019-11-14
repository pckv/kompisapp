package me.pckv.kompisapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import me.pckv.kompisapp.ui.listing.create.CreateListingActivity;
import me.pckv.kompisapp.ui.user.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        FloatingActionButton floatingActionButton = findViewById(R.id.create_listing_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createListingIntent = new Intent(MainActivity.this, CreateListingActivity.class);
                startActivity(createListingIntent);
            }
        });
    }
}
