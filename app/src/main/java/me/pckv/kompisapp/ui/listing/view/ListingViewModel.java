package me.pckv.kompisapp.ui.listing.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    public void activateListing() {
        UiAsyncTask.executeAndUpdate(activateResult, () -> {
            repository.activateListing(listingId);
            return true;
        });
    }

    public void deactivateListing() {
        UiAsyncTask.executeAndUpdate(activateResult, () -> {
            repository.deactivateListing(listingId);
            return false;
        });
    }

    public void assignListing() {
        UiAsyncTask.executeAndUpdate(assignResult, () -> {
            repository.assignListing(listingId);
            return true;
        });
    }

    public void unassignListing() {
        UiAsyncTask.executeAndUpdate(assignResult, () -> {
            repository.unassignListing(listingId);
            return false;
        });
    }
}
