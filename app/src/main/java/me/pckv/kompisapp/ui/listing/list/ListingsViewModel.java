package me.pckv.kompisapp.ui.listing.list;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.Listing;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class ListingsViewModel extends AndroidViewModel {

    private MutableLiveData<TaskResult<List<Listing>>> listingsResult = new MutableLiveData<>();
    private Repository repository;

    private Application application;
    private SharedPreferences sharedPreferences;

    public ListingsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance();
        this.application = application;

        sharedPreferences = application.getSharedPreferences(
                application.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
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

    public void logout() {
        repository.logout();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(application.getString(R.string.logged_in_user_key));
        editor.apply();
    }
}
