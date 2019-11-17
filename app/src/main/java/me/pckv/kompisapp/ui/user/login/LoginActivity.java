package me.pckv.kompisapp.ui.user.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import me.pckv.kompisapp.R;
import me.pckv.kompisapp.data.model.LoggedInUser;
import me.pckv.kompisapp.databinding.ActivityLoginBinding;
import me.pckv.kompisapp.ui.FormValidator;
import me.pckv.kompisapp.ui.listing.list.ListingsActivity;
import me.pckv.kompisapp.ui.user.create.CreateUserActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        // Try to login with saved authentication
        if (loginViewModel.checkLoggedIn()) {
            binding.loading.setVisibility(View.VISIBLE);
        }

        // Create a validator for the form fields
        FormValidator form = new FormValidator();
        form.addField(R.id.email, getString(R.string.invalid_email), email -> Patterns.EMAIL_ADDRESS.matcher(email).matches());
        form.addField(R.id.password, getString(R.string.invalid_password), FormValidator.lengthValidator(6));

        binding.setForm(form);
        binding.setLifecycleOwner(this);

        // Bind the result of the login action
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            binding.loading.setVisibility(View.GONE);

            if (loginResult.isError()) {
                showLoginFailed();
            }

            if (loginResult.isSuccess()) {
                updateUiWithUser(loginResult.getSuccess());

                startActivity(new Intent(LoginActivity.this, ListingsActivity.class));
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        // Bind submit button and keyboard event
        binding.login.setOnClickListener(v -> login());
        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
            }
            return false;
        });

        // Bind the create user button
        binding.createUser.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, CreateUserActivity.class)));
    }

    private void login() {
        binding.loading.setVisibility(View.VISIBLE);
        loginViewModel.login(
                binding.email.getText().toString().trim(),
                binding.password.getText().toString().trim());
    }

    private void updateUiWithUser(LoggedInUser user) {
        String welcome = getString(R.string.welcome) + user.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed() {
        Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
    }
}
