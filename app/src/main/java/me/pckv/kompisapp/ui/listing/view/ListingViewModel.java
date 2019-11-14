package me.pckv.kompisapp.ui.listing.view;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.data.DatalessResult;
import me.pckv.kompisapp.data.ListingRepository;

public class ListingViewModel extends ViewModel {

    private ListingRepository listingRepository;
    private long listingId;

    public ListingViewModel(ListingRepository listingRepository, long listingId) {
        this.listingRepository = listingRepository;
        this.listingId = listingId;
    }

    @SuppressLint("StaticFieldLeak")
    public void actiavteListing() {
        new AsyncTask<Void, Void, DatalessResult>() {
            @Override
            protected DatalessResult doInBackground(Void... voids) {
                return listingRepository.activateListing(listingId);
            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    public void deactiavteListing() {
        new AsyncTask<Void, Void, DatalessResult>() {
            @Override
            protected DatalessResult doInBackground(Void... voids) {
                return listingRepository.deactivateListing(listingId);
            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    public void assignListing() {
        new AsyncTask<Void, Void, DatalessResult>() {
            @Override
            protected DatalessResult doInBackground(Void... voids) {
                return listingRepository.assignListing(listingId);
            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    public void unassignListing() {
        new AsyncTask<Void, Void, DatalessResult>() {
            @Override
            protected DatalessResult doInBackground(Void... voids) {
                return listingRepository.unassignListing(listingId);
            }
        };
    }
}
