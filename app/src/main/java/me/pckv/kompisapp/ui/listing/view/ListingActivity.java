package me.pckv.kompisapp.ui.listing.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.Location;
import me.pckv.kompisapp.databinding.ActivityListingBinding;

public class ListingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ListingViewModel listingViewModel;

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mMapView;
    private Listing listing;


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

        mMapView = this.findViewById(R.id.listings_map);
        initGoogleMap(savedInstanceState);

        listing = JSON.parseObject(extras.getString("listingJson"), Listing.class);
        Location location = JSON.parseObject(extras.getString("locationJson"), Location.class);

        listingViewModel = ViewModelProviders.of(this, new ListingViewModelFactory(listing, location))
                .get(ListingViewModel.class);

        binding.setViewmodel(listingViewModel);

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

                setResult(RESULT_OK);
                binding.setViewmodel(listingViewModel);
            }
        });

        listingViewModel.getAssignResult().observe(this, assignResult -> {
            if (assignResult.isError()) {
                showError(assignResult.getError());
            }
            if (assignResult.isSuccess()) {
                showChangeSwitchSuccess(assignResult.getSuccess() ?
                        R.string.listing_assigned : R.string.listing_unassigned);

                setResult(RESULT_OK);
                binding.setViewmodel(listingViewModel);
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

    @Nullable
    //@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_listing, container, false);
        mMapView = view.findViewById(R.id.listings_map);
        initGoogleMap(savedInstanceState);

        return view;
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
            showDeleteListingDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteListingDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_listing_dialog)
                .setMessage(R.string.delete_listing_dialog_message)
                .setCancelable(true)
                .setPositiveButton(R.string.prompt_yes, ((dialog, which) -> listingViewModel.deleteListing()))
                .setNegativeButton(R.string.prompt_no, null)
                .show();
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

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        LatLng ownerLocation = new LatLng(listing.getLocation().getLatitude(), listing.getLocation().getLongitude());
        map.addMarker(new MarkerOptions().position(ownerLocation)).setTitle(listing.getOwner().getDisplayName());
        if (listing.hasAssignee()) {
            LatLng assigneeLocation = new LatLng(listing.getAssignee().getLocation().getLatitude(), listing.getAssignee().getLocation().getLongitude());
            map.addMarker(new MarkerOptions().position(assigneeLocation)).setTitle(listing.getAssignee().getUser().getDisplayName());
        }
        map.moveCamera(CameraUpdateFactory.zoomTo(15));
        map.moveCamera(CameraUpdateFactory.newLatLng(ownerLocation));
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}

