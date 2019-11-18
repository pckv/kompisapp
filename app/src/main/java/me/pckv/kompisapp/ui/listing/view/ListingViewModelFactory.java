package me.pckv.kompisapp.ui.listing.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import me.pckv.kompisapp.data.model.Listing;

public class ListingViewModelFactory implements ViewModelProvider.Factory {

    private Listing listing;

    public ListingViewModelFactory(Listing listing) {
        this.listing = listing;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListingViewModel.class)) {
            return (T) new ListingViewModel(listing);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
