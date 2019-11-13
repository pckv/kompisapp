package me.pckv.kompisapp.ui.user.create;

import androidx.annotation.Nullable;

import lombok.Getter;

/**
 * Authentication result : success or error message.
 */
public class CreateUserResult {

    @Getter
    private boolean success;

    @Nullable
    private Integer error;

    CreateUserResult(@Nullable Integer error) {
        this.error = error;
        this.success = false;
    }

    CreateUserResult() {
        this.success = true;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
