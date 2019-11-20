package me.pckv.kompisapp.ui.listing.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.data.model.Location;

public class ListingViewModelFactory implements ViewModelProvider.Factory {

    private Listing listing;
    private Location location;

    public ListingViewModelFactory(Listing listing, Location location) {
        this.listing = listing;
        this.location = location;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListingViewModel.class)) {
            return (T) new ListingViewModel(listing, location);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
