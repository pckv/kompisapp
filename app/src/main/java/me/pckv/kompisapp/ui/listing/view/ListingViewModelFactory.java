package me.pckv.kompisapp.ui.listing.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ListingViewModelFactory implements ViewModelProvider.Factory {

    private long listingId;

    public ListingViewModelFactory(long listingId) {
        this.listingId = listingId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListingViewModel.class)) {
            return (T) new ListingViewModel(listingId);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
