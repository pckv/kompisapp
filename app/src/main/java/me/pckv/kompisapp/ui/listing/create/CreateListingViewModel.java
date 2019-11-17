package me.pckv.kompisapp.ui.listing.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class CreateListingViewModel extends ViewModel {

    private MutableLiveData<TaskResult<Listing>> createListingResult = new MutableLiveData<>();
    private Repository repository;

    public CreateListingViewModel() {
        this.repository = Repository.getInstance();
    }

    public LiveData<TaskResult<Listing>> getCreateUserResult() {
        return createListingResult;
    }

    public void createListing(final String title, final boolean driver) {
        UiAsyncTask.executeAndUpdate(createListingResult, () -> repository.createListing(title, driver));
    }
}
