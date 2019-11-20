package me.pckv.kompisapp.ui.listing.list;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.Location;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class ListingsViewModel extends AndroidViewModel {

    private static final int MAXIMUM_LISTING_DISTANCE = 100;

    @Getter
    private MutableLiveData<TaskResult<List<Listing>>> listingsResult = new MutableLiveData<>();

    @Getter
    private MutableLiveData<List<Listing>> listings = new MutableLiveData<>();

    @Getter
    private MutableLiveData<Location> location = new MutableLiveData<>();

    private Repository repository;

    private Application application;
    private SharedPreferences sharedPreferences;

    public ListingsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance();
        this.application = application;

        sharedPreferences = application.getSharedPreferences(
                application.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public void getListOfListings() {
        UiAsyncTask.executeAndUpdate(listingsResult, () -> repository.getListings(), this::setListings);
    }

    public void logout() {
        repository.logout();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(application.getString(R.string.logged_in_user_key));
        editor.apply();
    }

    private void calculateDistances(List<Listing> listings) {
        for (Listing listing : listings) {
            listing.setDistance(location.getValue().distanceTo(listing.getLocation()));
        }
    }

    private void hideInactive(List<Listing> listings) {
        listings.removeIf(listing -> (!listing.isActive() && !repository.isOwner(listing)));
    }

    private void sortByDistance(List<Listing> listings) {
        Collections.sort(listings, (listing1, listing2) ->
                Double.compare(listing1.getDistance(), listing2.getDistance()));
    }

    private void hideByDistance(List<Listing> listings) {
        listings.removeIf(listing -> listing.getDistance() > MAXIMUM_LISTING_DISTANCE);
    }

    private void setListings(List<Listing> listings) {
        hideInactive(listings);

        if (location.getValue() != null) {
            calculateDistances(listings);
            sortByDistance(listings);
            hideByDistance(listings);
        }

        this.listings.setValue(listings);
    }

    public void setLocation(android.location.Location location) {
        this.location.setValue(Location.fromAndroidLocation(location));
        if (listings.getValue() == null) {
            return;
        }

        List<Listing> listings = this.listings.getValue();

        calculateDistances(listings);
        sortByDistance(listings);
        hideByDistance(listings);

        this.listings.setValue(listings);
    }
}
