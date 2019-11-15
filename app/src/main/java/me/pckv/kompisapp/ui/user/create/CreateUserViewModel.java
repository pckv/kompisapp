package me.pckv.kompisapp.ui.user.create;

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

public class CreateUserViewModel extends ViewModel {

    private MutableLiveData<CreateUserFormState> createUserFormState = new MutableLiveData<>();
    private MutableLiveData<TaskResult<User>> createUserResult = new MutableLiveData<>();
    private Repository repository;

    public CreateUserViewModel() {
        this.repository = Repository.getInstance();
    }

    public LiveData<CreateUserFormState> getCreateUserFormState() {
        return createUserFormState;
    }

    public LiveData<TaskResult<User>> getCreateUserResult() {
        return createUserResult;
    }

    @SuppressLint("StaticFieldLeak")
    public void createUser(final String displayName, final String email, final String password) {
        new UiAsyncTask<User>(createUserResult) {

            @Override
            protected User doInBackground() throws HttpStatusException {
                return repository.createUser(displayName, email, password);
            }
        }.execute();
    }

    public void createUserDataChanged(String displayName, String email, String password) {
        if (!isDisplayNameValid(displayName)) {
            createUserFormState.setValue(new CreateUserFormState(R.string.invalid_display_name, null, null));
        } else if (!isEmailValid(email)) {
            createUserFormState.setValue(new CreateUserFormState(null, R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            createUserFormState.setValue(new CreateUserFormState(null, null, R.string.invalid_password));
        } else {
            createUserFormState.setValue(new CreateUserFormState(true));
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

    private boolean isDisplayNameValid(String displayName) {
        return displayName != null && displayName.trim().length() > 1;
    }
}
