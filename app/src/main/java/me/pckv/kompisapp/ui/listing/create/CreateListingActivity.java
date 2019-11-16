package me.pckv.kompisapp.ui.listing.create;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import me.pckv.kompisapp.R;

public class CreateListingActivity extends AppCompatActivity {

    private CreateListingViewModel createListingViewModel;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);
        createListingViewModel = ViewModelProviders.of(this).get(CreateListingViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        final EditText titleEditText = findViewById(R.id.title);
        final Switch driverSwitch = findViewById(R.id.driver);
        final Button createListingButton = findViewById(R.id.create_listing);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        createListingViewModel.getCreateListingFormState().observe(this, createListingFormState -> {
            createListingButton.setEnabled(createListingFormState.isDataValid());
            if (createListingFormState.getTitleError() != null) {
                titleEditText.setError(getString(createListingFormState.getTitleError()));
            }
        });

        createListingViewModel.getCreateUserResult().observe(this, createListingResult -> {
            loadingProgressBar.setVisibility(View.GONE);

            if (createListingResult.isError()) {
                showCreateListingFailed();
            }

            if (createListingResult.isSuccess()) {
                showCreateListingSuccess();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                createListingViewModel.createListingDataChanged(
                        titleEditText.getText().toString()
                );
            }
        };

        titleEditText.addTextChangedListener(afterTextChangedListener);

        createListingButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            createListingViewModel.createListing(
                    titleEditText.getText().toString(),
                    driverSwitch.isChecked()
            );
        });


    }

    private void showCreateListingSuccess() {
        Toast.makeText(getApplicationContext(), R.string.listing_created, Toast.LENGTH_SHORT).show();
    }

    private void showCreateListingFailed() {
        Toast.makeText(getApplicationContext(), R.string.create_listing_failed, Toast.LENGTH_SHORT).show();
    }
    fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                // Logic to handle location object
            }
        }
    });
    fusedLocationClient.
}
