package me.pckv.kompisapp.ui.user.create;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import me.pckv.kompisapp.R;

public class CreateUserActivity extends AppCompatActivity {

    private CreateUserViewModel createUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        createUserViewModel = ViewModelProviders.of(this).get(CreateUserViewModel.class);

        final EditText displayNameEditText = findViewById(R.id.display_name);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button createUserButton = findViewById(R.id.create_user);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        createUserViewModel.getCreateUserFormState().observe(this, createUserFormState -> {
            createUserButton.setEnabled(createUserFormState.isDataValid());
            if (createUserFormState.getDisplayNameError() != null) {
                displayNameEditText.setError(getString(createUserFormState.getDisplayNameError()));
            }
            if (createUserFormState.getEmailError() != null) {
                emailEditText.setError(getString(createUserFormState.getEmailError()));
            }
            if (createUserFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(createUserFormState.getPasswordError()));
            }
        });

        createUserViewModel.getCreateUserResult().observe(this, createUserResult -> {
            loadingProgressBar.setVisibility(View.GONE);

            if (createUserResult.isError()) {
                showCreateUserFailed();
            }

            if (createUserResult.isSuccess()) {
                showCreateUserSuccess();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                createUserViewModel.createUserDataChanged(
                        displayNameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createUserViewModel.createUser(
                        displayNameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        createUserButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            createUserViewModel.createUser(
                    displayNameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });
    }

    private void showCreateUserSuccess() {
        Toast.makeText(getApplicationContext(), R.string.created_user, Toast.LENGTH_SHORT).show();
    }

    private void showCreateUserFailed() {
        Toast.makeText(getApplicationContext(), R.string.create_user_failed, Toast.LENGTH_SHORT).show();
    }
}
