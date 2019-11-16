package me.pckv.kompisapp.ui.listing.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.listing.create.CreateListingActivity;
import me.pckv.kompisapp.ui.user.login.LoginActivity;

public class ListingsActivity extends AppCompatActivity {

    public static final int CREATE_LISTING_REQUEST = 1;

    private ListingsViewModel listingsViewModel;
    private ListingRecyclerViewAdapter adapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
                startActivityForResult(createListingIntent, CREATE_LISTING_REQUEST);
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
                    if (adapter == null) {
                        setUpRecyclerView(listingsResult.getSuccess());
                    } else {
                        updateRecyclerView(listingsResult.getSuccess());
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        listingsViewModel.getListings();

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listingsViewModel.getListings();
            }
        });
    }

    private void setUpRecyclerView(List<Listing> listings) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingRecyclerViewAdapter(this, listings);
        recyclerView.setAdapter(adapter);
        adapter.getFilter().filter("");
    }

    private void updateRecyclerView(List<Listing> listings) {
        adapter.updateListings(listings);
        adapter.getFilter().filter("");
    }

    private void showGetListingsFailed() {
        Toast.makeText(getApplicationContext(), R.string.create_listing_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_LISTING_REQUEST && resultCode == RESULT_OK) {
            listingsViewModel.getListings();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            listingsViewModel.logout();

            startActivity(new Intent(ListingsActivity.this, LoginActivity.class));
            setResult(RESULT_OK);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
