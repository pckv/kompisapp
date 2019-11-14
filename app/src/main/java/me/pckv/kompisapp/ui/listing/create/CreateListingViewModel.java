package me.pckv.kompisapp.ui.listing.create;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.DatalessResult;
import me.pckv.kompisapp.data.ListingDatasource;

public class CreateListingViewModel extends ViewModel {

    private MutableLiveData<CreateListingFormState> createListingFormState = new MutableLiveData<>();
    private MutableLiveData<CreateListingResult> createListingResult = new MutableLiveData<>();
    private ListingDatasource listingDatasource;

    public CreateListingViewModel() {
    }

    public LiveData<CreateListingFormState> getCreateListingFormState() {
        return createListingFormState;
    }

    public LiveData<CreateListingResult> getCreateUserResult() {
        return createListingResult;
    }

    @SuppressLint("StaticFieldLeak")
    public void createListing(final String title, final boolean driver) {
        new AsyncTask<Void, Void, DatalessResult>() {
            @Override
            protected DatalessResult doInBackground(Void... voids) {
                return listingDatasource.createListing(title, driver);
            }

            @Override
            protected void onPostExecute(DatalessResult result) {
                if (result instanceof DatalessResult.Success) {
                    createListingResult.setValue(new CreateListingResult());
                } else {
                    createListingResult.setValue(new CreateListingResult(R.string.create_listing_failed));
                }
            }
        }.execute();
    }

    public void createListingDataChanged(String title, boolean driver) {
        if (!isTitleValid(title)) {
            createListingFormState.setValue(new CreateListingFormState(R.string.invalid_title));
        } else {
            createListingFormState.setValue(new CreateListingFormState(true));
        }
    }

    private boolean isTitleValid(String title) {
        return title != null;
    }
}
