package me.pckv.kompisapp.ui.listing.create;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.databinding.ActivityCreateListingBinding;
import me.pckv.kompisapp.ui.FormValidator;

public class CreateListingActivity extends AppCompatActivity {

    private CreateListingViewModel createListingViewModel;
    private ActivityCreateListingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_listing);
        createListingViewModel = ViewModelProviders.of(this).get(CreateListingViewModel.class);

        // Create a validator for the form fields
        FormValidator form = new FormValidator();
        form.addField(R.id.title, getString(R.string.invalid_title), FormValidator.lengthValidator(3));

        binding.setForm(form);
        binding.setLifecycleOwner(this);

        // Bind the result of the create user action
        createListingViewModel.getCreateUserResult().observe(this, createListingResult -> {
            binding.loading.setVisibility(View.GONE);

            if (createListingResult.isError()) {
                showCreateListingFailed();
            }

            if (createListingResult.isSuccess()) {
                showCreateListingSuccess();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        // Bind submit button and keyboard event
        binding.createListing.setOnClickListener(v -> createListing());
        binding.title.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createListing();
            }
            return false;
        });
    }

    private void createListing() {
        binding.loading.setVisibility(View.VISIBLE);
        createListingViewModel.createListing(
                binding.title.getText().toString().trim(),
                binding.driver.isChecked());
    }

    private void showCreateListingSuccess() {
        Toast.makeText(getApplicationContext(), R.string.listing_created, Toast.LENGTH_SHORT).show();
    }

    private void showCreateListingFailed() {
        Toast.makeText(getApplicationContext(), R.string.create_listing_failed, Toast.LENGTH_SHORT).show();
    }
}
