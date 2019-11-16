package me.pckv.kompisapp.ui.listing.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.databinding.ActivityListingBinding;

public class ListingActivity extends AppCompatActivity {

    private ListingViewModel listingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_listing);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Listing listing = JSON.parseObject(extras.getString("listingJson"), Listing.class);

        listingViewModel = ViewModelProviders.of(this, new ListingViewModelFactory(listing.getId()))
                .get(ListingViewModel.class);

        binding.setListing(listing);

        if (!listingViewModel.isOwner(listing) && (listing.hasAssignee() && !listingViewModel.isAssignee(listing))) {
            binding.assign.setVisibility(View.INVISIBLE);
        }


        if (!listingViewModel.isOwner(listing)) {
            binding.activate.setVisibility(View.INVISIBLE);
        }

        binding.activate.setOnClickListener(v -> {
            if (binding.activate.isChecked()) {
                listingViewModel.activateListing();
            } else {
                listingViewModel.deactivateListing();
            }
        });

        binding.assign.setOnClickListener(v -> {
            if (binding.assign.isChecked()) {
                listingViewModel.assignListing();
            } else {
                listingViewModel.unassignListing();
            }
        });

        listingViewModel.getActivateResult().observe(this, activateResult -> {
            if (activateResult.isError()) {
                showChangeSwitchError(activateResult.getError());
            }
            if (activateResult.isSuccess()) {
                showChangeSwitchSuccess(activateResult.getSuccess() ?
                        R.string.listing_activated : R.string.listing_deactivated);
            }
        });

        listingViewModel.getAssignResult().observe(this, assignResult -> {
            if (assignResult.isError()) {
                showChangeSwitchError(assignResult.getError());
            }
            if (assignResult.isSuccess()) {
                showChangeSwitchSuccess(assignResult.getSuccess() ?
                        R.string.listing_assigned : R.string.listing_unassigned);
            }
        });
    }

    private void showChangeSwitchSuccess(@StringRes Integer successString) {
        Toast.makeText(getApplicationContext(), successString, Toast.LENGTH_SHORT).show();
    }

    private void showChangeSwitchError(HttpStatusException exception) {
        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
