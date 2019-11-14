package me.pckv.kompisapp.ui.listing.create;

import androidx.annotation.Nullable;

import lombok.Getter;

public class CreateListingResult {

    @Getter
    private boolean success;

    @Nullable
    private Integer error;

    CreateListingResult(@Nullable Integer error) {
        this.error = error;
        this.success = false;
    }

    CreateListingResult() {
        this.success = true;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
