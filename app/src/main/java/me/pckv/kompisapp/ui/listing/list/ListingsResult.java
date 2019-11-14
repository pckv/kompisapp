package me.pckv.kompisapp.ui.listing.list;

import androidx.annotation.Nullable;

import java.util.List;

import me.pckv.kompisapp.data.model.Listing;

public class ListingsResult {

    @Nullable
    private List<Listing> success;

    @Nullable
    private Integer error;

    public ListingsResult(@Nullable List<Listing> success) {
        this.success = success;
    }

    public ListingsResult(@Nullable Integer error) {
        this.error = error;
    }

    @Nullable
    public List<Listing> getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
