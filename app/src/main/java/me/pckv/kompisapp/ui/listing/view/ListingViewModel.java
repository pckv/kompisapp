package me.pckv.kompisapp.ui.listing.view;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import lombok.Getter;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class ListingViewModel extends ViewModel {

    private Repository repository;

    @Getter
    private Listing listing;

    @Getter
    private MutableLiveData<TaskResult<Boolean>> deleteResult = new MutableLiveData<>();
    @Getter
    private MutableLiveData<TaskResult<Boolean>> activateResult = new MutableLiveData<>();
    @Getter
    private MutableLiveData<TaskResult<Boolean>> assignResult = new MutableLiveData<>();

    public ListingViewModel(Listing listing) {
        this.repository = Repository.getInstance();
        this.listing = listing;
    }

    public boolean isOwner() {
        return repository.isOwner(listing);
    }

    public boolean isAssignee() {
        return repository.isAssignee(listing);
    }

    public boolean canAssign() {
        return isOwner() || (!listing.hasAssignee() || isAssignee());
    }

    public void deleteListing() {
        UiAsyncTask.executeAndUpdate(deleteResult, () -> {
            repository.deleteListing(listing.getId());
            return true;
        });
    }

    public void activateListing() {
        UiAsyncTask.executeAndUpdate(activateResult, () -> {
            repository.activateListing(listing.getId());
            return true;
        }, result -> listing.setActive(true));
    }

    public void deactivateListing() {
        UiAsyncTask.executeAndUpdate(activateResult, () -> {
            repository.deactivateListing(listing.getId());
            return false;
        }, result -> listing.setActive(false));
    }

    public void assignListing() {
        UiAsyncTask.executeAndUpdate(assignResult, () -> {
            repository.assignListing(listing.getId());
            return true;
        }, result -> listing.setAssignee(repository.getLoggedInAsUser()));
    }

    public void unassignListing() {
        UiAsyncTask.executeAndUpdate(assignResult, () -> {
            repository.unassignListing(listing.getId());
            return false;
        }, result -> listing.setAssignee(null));
    }
}
