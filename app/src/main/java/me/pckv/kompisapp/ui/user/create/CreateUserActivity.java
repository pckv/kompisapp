package me.pckv.kompisapp.ui.user.create;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.databinding.ActivityCreateUserBinding;
import me.pckv.kompisapp.ui.FormValidator;

public class CreateUserActivity extends AppCompatActivity {

    private CreateUserViewModel createUserViewModel;
    private ActivityCreateUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_user);
        createUserViewModel = ViewModelProviders.of(this).get(CreateUserViewModel.class);

        // Create a validator for the form fields
        FormValidator form = new FormValidator();
        form.addField(R.id.display_name, getString(R.string.invalid_display_name), FormValidator.lengthValidator(2));
        form.addField(R.id.email, getString(R.string.invalid_email), email -> Patterns.EMAIL_ADDRESS.matcher(email).matches());
        form.addField(R.id.password, getString(R.string.invalid_password), FormValidator.lengthValidator(6));

        binding.setForm(form);
        binding.setLifecycleOwner(this);

        // Bind the result of the create user action
        createUserViewModel.getCreateUserResult().observe(this, createUserResult -> {
            binding.loading.setVisibility(View.GONE);

            if (createUserResult.isError()) {
                showCreateUserFailed();
            }

            if (createUserResult.isSuccess()) {
                showCreateUserSuccess();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        // Bind submit button and keyboard event
        binding.createUser.setOnClickListener(v -> createUser());
        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createUser();
            }
            return false;
        });

    }

    private void createUser() {
        binding.loading.setVisibility(View.VISIBLE);
        createUserViewModel.createUser(
                binding.displayName.getText().toString(),
                binding.email.getText().toString(),
                binding.password.getText().toString());
    }

    private void showCreateUserSuccess() {
        Toast.makeText(getApplicationContext(), R.string.created_user, Toast.LENGTH_SHORT).show();
    }

    private void showCreateUserFailed() {
        Toast.makeText(getApplicationContext(), R.string.create_user_failed, Toast.LENGTH_SHORT).show();
    }
}
