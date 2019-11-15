package me.pckv.kompisapp.ui.user.login;

import android.annotation.SuppressLint;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.HttpStatusException;
import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.User;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<TaskResult<User>> loginResult = new MutableLiveData<>();
    private Repository repository;

    LoginViewModel() {
        this.repository = Repository.getInstance();
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<TaskResult<User>> getLoginResult() {
        return loginResult;
    }

    @SuppressLint("StaticFieldLeak")
    public void login(final String email, final String password) {
        new UiAsyncTask<User>(loginResult) {

            @Override
            protected User doInBackground() throws HttpStatusException {
                return repository.authorize(email, password);
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
