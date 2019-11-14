package me.pckv.kompisapp.ui.listing.create;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import me.pckv.kompisapp.R;

public class CreateListingActivity extends AppCompatActivity {

    private CreateListingViewModel createListingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);
        createListingViewModel = ViewModelProviders.of(this, new CreateListingViewModelFactory())
                .get(CreateListingViewModel.class);

        final EditText titleEditText = findViewById(R.id.title);
        final Switch driverSwitch = findViewById(R.id.driver);
        final Button createListingButton = findViewById(R.id.create_listing);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        createListingViewModel.getCreateListingFormState().observe(this, new Observer<CreateListingFormState>() {
            @Override
            public void onChanged(CreateListingFormState createListingFormState) {
                if (createListingFormState == null) {
                    return;
                }
                createListingButton.setEnabled(createListingFormState.isDataValid());
                if (createListingFormState.getTitleError() != null) {
                    titleEditText.setError(getString(createListingFormState.getTitleError()));
                }
            }
        });

        createListingViewModel.getCreateUserResult().observe(this, new Observer<CreateListingResult>() {
            @Override
            public void onChanged(CreateListingResult createListingResult) {
                if (createListingResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (createListingResult.getError() != null) {
                    showCreateListingFailed()
                }
            }
        });
    }

    private void showCreateListingSuccess() {
        Toast.makeText(getApplicationContext(), R.string.created_user, Toast.LENGTH_SHORT).show();
    }

    private void showCreateListingFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
