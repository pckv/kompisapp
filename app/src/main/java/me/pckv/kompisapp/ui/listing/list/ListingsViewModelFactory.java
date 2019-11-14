package me.pckv.kompisapp.ui.listing.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import me.pckv.kompisapp.data.ListingRepository;

/**
 * ViewModel provider factory to instantiate CreateUserViewModel.
 * Required given CreateUserViewModel has a non-empty constructor
 */
public class ListingsViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListingsViewModel.class)) {
            return (T) new ListingsViewModel(ListingRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
