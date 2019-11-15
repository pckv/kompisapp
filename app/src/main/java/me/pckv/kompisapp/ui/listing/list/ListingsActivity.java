package me.pckv.kompisapp.ui.listing.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.listing.create.CreateListingActivity;

public class ListingsActivity extends AppCompatActivity {

    private ListingsViewModel listingsViewModel;
    private ListingRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        listingsViewModel = ViewModelProviders.of(this).get(ListingsViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createListingIntent = new Intent(ListingsActivity.this, CreateListingActivity.class);
                startActivity(createListingIntent);
            }
        });

        listingsViewModel.getListingsResult().observe(this, new Observer<TaskResult<List<Listing>>>() {
            @Override
            public void onChanged(TaskResult<List<Listing>> listingsResult) {
                if (listingsResult == null) {
                    return;
                }

                if (listingsResult.isError()) {
                    showGetListingsFailed();
                }
                if (listingsResult.isSuccess()) {
                    setUpRecyclerView(listingsResult.getSuccess());
                }
            }
        });

        listingsViewModel.getListings();
    }

    private void setUpRecyclerView(List<Listing> listings) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingRecyclerViewAdapter(listings);
        recyclerView.setAdapter(adapter);
    }

    private void showGetListingsFailed() {
        Toast.makeText(getApplicationContext(), R.string.create_listing_failed, Toast.LENGTH_SHORT).show();
    }
}
