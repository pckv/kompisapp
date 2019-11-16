package me.pckv.kompisapp.ui.listing.view;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class ListingViewModel extends ViewModel {

    private Repository repository;
    private long listingId;

    private MutableLiveData<TaskResult<Boolean>> activateResult = new MutableLiveData<>();
    private MutableLiveData<TaskResult<Boolean>> assignResult = new MutableLiveData<>();

    public ListingViewModel(long listingId) {
        this.repository = Repository.getInstance();
        this.listingId = listingId;
    }

    public LiveData<TaskResult<Boolean>> getActivateResult() {
        return activateResult;
    }

    public LiveData<TaskResult<Boolean>> getAssignResult() {
        return assignResult;
    }

    public boolean isOwner(Listing listing) {
        return repository.isOwner(listing);
    }

    public boolean isAssignee(Listing listing) {
        return repository.isAssignee(listing);
    }

    @SuppressLint("StaticFieldLeak")
    public void activateListing() {
        new UiAsyncTask<Boolean>(activateResult) {

            @Override
            protected Boolean doInBackground() throws HttpStatusException {
                repository.activateListing(listingId);
                return true;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deactivateListing() {
        new UiAsyncTask<Boolean>(activateResult) {

            @Override
            protected Boolean doInBackground() throws HttpStatusException {
                repository.deactivateListing(listingId);
                return false;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void assignListing() {
        new UiAsyncTask<Boolean>(assignResult) {

            @Override
            protected Boolean doInBackground() throws HttpStatusException {
                repository.assignListing(listingId);
                return true;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void unassignListing() {
        new UiAsyncTask<Boolean>(assignResult) {

            @Override
            protected Boolean doInBackground() throws HttpStatusException {
                repository.unassignListing(listingId);
                return false;
            }
        }.execute();
    }
}
