package me.pckv.kompisapp.ui.listing.view;

import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.data.ListingRepository;

public class ListingViewModel extends ViewModel {

    private ListingRepository listingRepository;
    private long listingId;

    public ListingViewModel(ListingRepository listingRepository, long listingId) {
        this.listingRepository = listingRepository;
        this.listingId = listingId;
    }
}
