package me.pckv.kompisapp.ui.listing.view;

import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.data.ListingRepository;

public class ListingViewModel extends ViewModel {

    private ListingRepository listingRepository;

    public ListingViewModel(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }
}
