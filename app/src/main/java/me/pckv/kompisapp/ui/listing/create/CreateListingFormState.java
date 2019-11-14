package me.pckv.kompisapp.ui.listing.create;

import androidx.annotation.Nullable;

public class CreateListingFormState {

    @Nullable
    private Integer titleError;

    private boolean isDataValid;

    CreateListingFormState(@Nullable Integer titleError) {
        this.titleError = titleError;
        this.isDataValid = false;
    }

    CreateListingFormState(boolean isDataValid) {
        this.titleError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getTitleError() {
        return titleError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
