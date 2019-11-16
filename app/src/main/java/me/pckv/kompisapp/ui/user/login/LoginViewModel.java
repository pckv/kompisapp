package me.pckv.kompisapp.ui.user.login;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.LoggedInUser;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
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

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<TaskResult<LoggedInUser>> getLoginResult() {
        return loginResult;
    }

    private void updateLoggedInUser(LoggedInUser loggedInUser) {
        System.out.println("Saving authentication " + loggedInUser.toString());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(application.getString(R.string.logged_in_user_key), JSON.toJSONString(loggedInUser));
        editor.apply();
    }

    @SuppressLint("StaticFieldLeak")
    public boolean checkLoggedIn() {
        // Attempt to load current logged in user
        String loggedInUserJson = sharedPreferences.getString(
                application.getString(R.string.logged_in_user_key), null);
        if (loggedInUserJson == null) {
            return false;
        }

        System.out.println("Got saved authentication " + loggedInUserJson);
        final LoggedInUser loggedInUser = JSON.parseObject(loggedInUserJson, LoggedInUser.class);

        new UiAsyncTask<LoggedInUser>(loginResult) {

            @Override
            protected LoggedInUser doInBackground() throws HttpStatusException {
                return repository.authenticate(loggedInUser);
            }

            @Override
            protected void onSuccess(LoggedInUser result) {
                super.onSuccess(result);
                if (!loggedInUser.equals(result)) {
                    updateLoggedInUser(result);
                }
            }
        }.execute();

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public void login(final String email, final String password) {
        new UiAsyncTask<LoggedInUser>(loginResult) {

            @Override
            protected LoggedInUser doInBackground() throws HttpStatusException {
                return repository.authorize(email, password);
            }

            @Override
            protected void onSuccess(LoggedInUser result) {
                updateLoggedInUser(result);
            }
        }.execute();
    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
