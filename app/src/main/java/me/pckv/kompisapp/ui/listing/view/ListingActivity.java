package me.pckv.kompisapp.ui.listing.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Listing listing = JSON.parseObject(extras.getString("listingJson"), Listing.class);

        listingViewModel = ViewModelProviders.of(this, new ListingViewModelFactory(listing))
                .get(ListingViewModel.class);

        binding.setListing(listing);

        if (!listingViewModel.isOwner() && (listing.hasAssignee() && !listingViewModel.isAssignee())) {
            binding.assign.setVisibility(View.GONE);
        }

        if (!listingViewModel.isOwner()) {
            binding.activate.setVisibility(View.GONE);
        }

        if (!listing.hasAssignee()) {
            binding.assignee.setVisibility(View.GONE);
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
                showError(activateResult.getError());
            }
            if (activateResult.isSuccess()) {
                showChangeSwitchSuccess(activateResult.getSuccess() ?
                        R.string.listing_activated : R.string.listing_deactivated);
            }
        });

        listingViewModel.getAssignResult().observe(this, assignResult -> {
            if (assignResult.isError()) {
                showError(assignResult.getError());
            }
            if (assignResult.isSuccess()) {
                showChangeSwitchSuccess(assignResult.getSuccess() ?
                        R.string.listing_assigned : R.string.listing_unassigned);
            }
        });

        listingViewModel.getDeleteResult().observe(this, deleteResult -> {
            if (deleteResult.isError()) {
                showError(deleteResult.getError());
            }
            if (deleteResult.isSuccess()) {
                showDeleteListingSuccess();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listing, menu);

        // Remove the delete button if the listing is not owned by the logged in user
        if (!listingViewModel.isOwner()) {
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            // TODO: show prompt
            listingViewModel.deleteListing();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showChangeSwitchSuccess(@StringRes Integer successString) {
        Toast.makeText(getApplicationContext(), successString, Toast.LENGTH_SHORT).show();
    }

    private void showDeleteListingSuccess() {
        Toast.makeText(getApplicationContext(), getString(R.string.listing_deleted), Toast.LENGTH_SHORT).show();
    }

    private void showError(HttpStatusException exception) {
        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
