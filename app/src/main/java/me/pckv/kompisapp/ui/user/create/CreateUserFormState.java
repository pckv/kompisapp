package me.pckv.kompisapp.ui.user.create;

import androidx.annotation.Nullable;

/**
 * Data validation state of the createUser form.
 */
class CreateUserFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer displayNameError;
    private boolean isDataValid;

    CreateUserFormState(@Nullable Integer displayNameError, @Nullable Integer emailError, @Nullable Integer passwordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.displayNameError = displayNameError;
        this.isDataValid = false;
    }

    CreateUserFormState(boolean isDataValid) {
        this.emailError = null;
        this.passwordError = null;
        this.displayNameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getDisplayNameError() {
        return displayNameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
