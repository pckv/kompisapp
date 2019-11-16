package me.pckv.kompisapp.ui.listing.view;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;

import me.pckv.kompisapp.R;
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
            binding.assign.setVisibility(View.GONE);
        }

        if (!listingViewModel.isOwner(listing)) {
            binding.activate.setVisibility(View.GONE);
        }

        if (!listing.hasAssignee()) {
            binding.assignee.setVisibility(View.GONE);
        }

        binding.activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listingViewModel.activateListing();
                } else {
                    listingViewModel.deactivateListing();
                }
            }
        });

        binding.assign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listingViewModel.assignListing();
                } else {
                    listingViewModel.unassignListing();
                }
            }
        });
    }
}
