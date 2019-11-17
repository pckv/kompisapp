package me.pckv.kompisapp.ui.user.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.pckv.kompisapp.data.Repository;
import me.pckv.kompisapp.data.model.User;
import me.pckv.kompisapp.ui.TaskResult;
import me.pckv.kompisapp.ui.UiAsyncTask;

public class CreateUserViewModel extends ViewModel {

    private MutableLiveData<TaskResult<User>> createUserResult = new MutableLiveData<>();
    private Repository repository;

    public CreateUserViewModel() {
        this.repository = Repository.getInstance();
    }

    public LiveData<TaskResult<User>> getCreateUserResult() {
        return createUserResult;
    }

    public void createUser(final String displayName, final String email, final String password) {
        UiAsyncTask.executeAndUpdate(createUserResult, () -> repository.createUser(displayName, email, password));
    }
}
