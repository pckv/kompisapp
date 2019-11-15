package me.pckv.kompisapp.ui.listing.list;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class ListingsViewModel extends ViewModel {

    private MutableLiveData<TaskResult<List<Listing>>> listingsResult = new MutableLiveData<>();
    private Repository repository;

    public ListingsViewModel() {
        this.repository = Repository.getInstance();
    }

    public MutableLiveData<TaskResult<List<Listing>>> getListingsResult() {
        return listingsResult;
    }

    @SuppressLint("StaticFieldLeak")
    public void getListings() {
        new UiAsyncTask<List<Listing>>(listingsResult) {

            @Override
            protected List<Listing> doInBackground() throws HttpStatusException {
                return repository.getListings();
            }
        }.execute();
    }
}
