package me.pckv.kompisapp.ui.listing.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.model.Listing;

public class ListingActivity extends AppCompatActivity {

    private ListingViewModel listingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Listing listing = JSON.parseObject(extras.getString("listingJson"), Listing.class);

        listingViewModel = ViewModelProviders.of(this, new ListingViewModelFactory(listing.getId()))
                .get(ListingViewModel.class);

        final TextView titleTextView = findViewById(R.id.title);
        final TextView ownerNameTextView = findViewById(R.id.owner_name);
        final TextView distanceTextView = findViewById(R.id.distance);
        final TextView assignee = findViewById(R.id.assignee);
        final Switch assignSwitch = findViewById(R.id.assign);
        final Switch activateSwitch = findViewById(R.id.activate);

        titleTextView.setText(listing.getTitle());
        ownerNameTextView.setText(listing.getOwner().getDisplayName());
        distanceTextView.setText("5 km");

        if (listing.hasAssignee()) {
            assignee.setText(listing.getAssignee().getDisplayName());
        } else {
            assignee.setVisibility(View.INVISIBLE);
        }


        if (!listingViewModel.isOwner(listing) && (listing.hasAssignee() && !listingViewModel.isAssignee(listing))) {
            assignSwitch.setVisibility(View.INVISIBLE);
        } else {
            assignSwitch.setChecked(listing.hasAssignee());
        }


        if (!listingViewModel.isOwner(listing)) {
            activateSwitch.setVisibility(View.INVISIBLE);
        } else {
            activateSwitch.setChecked(listing.isActive());
        }
    }
}
