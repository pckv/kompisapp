package me.pckv.kompisapp.ui.listing.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.model.Listing;
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
        fab.setOnClickListener(view -> startActivityForResult(
                new Intent(ListingsActivity.this, CreateListingActivity.class),
                CREATE_LISTING_REQUEST));

        listingsViewModel.getListingsResult().observe(this, listingsResult -> {
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
        });

        listingsViewModel.getListings();

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> listingsViewModel.getListings());
    }

    private void setUpRecyclerView(List<Listing> listings) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
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

        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    public void onSearchClicked(MenuItem item) {
        SearchView searchView = (SearchView) item.getActionView();

        // Get focus of the search field and open the keyboard with a delay
        searchView.requestFocus();
        searchView.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView.findFocus(), 0);
        }, 100);
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
