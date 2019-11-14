package me.pckv.kompisapp.ui.listing.list;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.ListingRepository;
import me.pckv.kompisapp.data.Result;
import me.pckv.kompisapp.data.model.Listing;

public class ListingsViewModel extends ViewModel {

    private MutableLiveData<ListingsResult> listingsResult = new MutableLiveData<>();
    private ListingRepository listingRepository;

    public ListingsViewModel(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public LiveData<ListingsResult> getLisitingsResult() {
        return listingsResult;
    }

    @SuppressLint("StaticFieldLeak")
    public void getListings() {
        new AsyncTask<Void, Void, Result<List<Listing>>>() {
            @Override
            protected Result<List<Listing>> doInBackground(Void... voids) {
                return listingRepository.getListings();
            }

            @Override
            protected void onPostExecute(Result<List<Listing>> result) {
                if (result instanceof Result.Success) {
                    List<Listing> listings = ((Result.Success<List<Listing>>) result).getData();
                    listingsResult.setValue(new ListingsResult(listings));
                } else {
                    listingsResult.setValue(new ListingsResult(R.string.get_listings_failed));
                }
            }
        }.execute();
    }
}
