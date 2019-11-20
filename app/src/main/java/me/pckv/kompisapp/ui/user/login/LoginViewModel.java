package me.pckv.kompisapp.ui.user.login;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.LoggedInUser;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class LoginViewModel extends AndroidViewModel {

    @Getter
    private MutableLiveData<TaskResult<LoggedInUser>> loginResult = new MutableLiveData<>();
    private Repository repository;

    private Application application;
    private SharedPreferences sharedPreferences;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        this.repository = Repository.getInstance();

        sharedPreferences = application.getSharedPreferences(
                application.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @SuppressLint("StaticFieldLeak")
    public void setFirebaseToken(String token) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    repository.setFirebaseToken(token);
                } catch (HttpStatusException e) {
                    Log.d("LoginViewModel", "Failed to send firebase token to server");
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void updateLoggedInUser(LoggedInUser loggedInUser) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(application.getString(R.string.logged_in_user_key), JSON.toJSONString(loggedInUser));
        editor.apply();
    }

    @SuppressLint("StaticFieldLeak")
    public boolean checkLoggedIn() {
        // Attempt to load current logged in user
        String loggedInUserJson = sharedPreferences.getString(application.getString(R.string.logged_in_user_key), null);
        if (loggedInUserJson == null) {
            return false;
        }

        System.out.println("Got saved authentication " + loggedInUserJson);
        final LoggedInUser loggedInUser = JSON.parseObject(loggedInUserJson, LoggedInUser.class);

        // Verify authentication and update the logged in user on success
        UiAsyncTask.executeAndUpdate(loginResult, () -> repository.authenticate(loggedInUser), this::updateLoggedInUser);

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public void login(final String email, final String password) {
        UiAsyncTask.executeAndUpdate(loginResult, () -> repository.authorize(email, password), this::updateLoggedInUser);
    }
}
