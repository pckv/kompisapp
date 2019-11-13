package me.pckv.kompisapp.ui.user.create;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import me.pckv.kompisapp.R;

public class CreateUserActivity extends AppCompatActivity {

    private CreateUserViewModel createUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        createUserViewModel = ViewModelProviders.of(this, new CreateUserViewModelFactory())
                .get(CreateUserViewModel.class);

        final EditText displayNameEditText = findViewById(R.id.display_name);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button createUserButton = findViewById(R.id.create_user);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        createUserViewModel.getCreateUserFormState().observe(this, new Observer<CreateUserFormState>() {
            @Override
            public void onChanged(@Nullable CreateUserFormState createUserFormState) {
                if (createUserFormState == null) {
                    return;
                }
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
            }
        });

        createUserViewModel.getCreateUserResult().observe(this, new Observer<CreateUserResult>() {
            @Override
            public void onChanged(@Nullable CreateUserResult createUserResult) {
                if (createUserResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (createUserResult.getError() != null) {
                    showCreateUserFailed(createUserResult.getError());
                }
                if (createUserResult.isSuccess()) {
                    showCreateUserSuccess();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
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
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    createUserViewModel.createUser(
                            displayNameEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                createUserViewModel.createUser(
                        displayNameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void showCreateUserSuccess() {
        Toast.makeText(getApplicationContext(), R.string.created_user, Toast.LENGTH_SHORT).show();
    }

    private void showCreateUserFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
