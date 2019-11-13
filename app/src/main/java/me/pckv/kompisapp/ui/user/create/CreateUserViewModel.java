package me.pckv.kompisapp.ui.user.create;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.CreateUserDataSource;
import me.pckv.kompisapp.data.DatalessResult;

public class CreateUserViewModel extends ViewModel {

    private MutableLiveData<CreateUserFormState> createUserFormState = new MutableLiveData<>();
    private MutableLiveData<CreateUserResult> createUserResult = new MutableLiveData<>();
    private CreateUserDataSource createUserDataSource;

    public CreateUserViewModel(CreateUserDataSource createUserDataSource) {
        this.createUserDataSource = createUserDataSource;
    }

    public LiveData<CreateUserFormState> getCreateUserFormState() {
        return createUserFormState;
    }

    public LiveData<CreateUserResult> getCreateUserResult() {
        return createUserResult;
    }

    @SuppressLint("StaticFieldLeak")
    public void createUser(final String displayName, final String email, final String password) {
        new AsyncTask<Void, Void, DatalessResult>() {
            @Override
            protected DatalessResult doInBackground(Void... voids) {
                return createUserDataSource.createUser(displayName, email, password);
            }

            @Override
            protected void onPostExecute(DatalessResult result) {
                if (result instanceof DatalessResult.Success) {
                    createUserResult.setValue(new CreateUserResult());
                } else {
                    createUserResult.setValue(new CreateUserResult(R.string.create_user_failed));
                }
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
