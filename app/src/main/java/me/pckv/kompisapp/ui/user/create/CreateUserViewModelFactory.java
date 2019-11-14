package me.pckv.kompisapp.ui.user.create;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import me.pckv.kompisapp.data.UsersRepository;

/**
 * ViewModel provider factory to instantiate CreateUserViewModel.
 * Required given CreateUserViewModel has a non-empty constructor
 */
public class CreateUserViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateUserViewModel.class)) {
            return (T) new CreateUserViewModel(UsersRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
