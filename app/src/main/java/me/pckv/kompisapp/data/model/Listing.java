package me.pckv.kompisapp.data.model;

import androidx.annotation.Nullable;

import lombok.Data;

@Data
public class Listing {

    private long id;
    private String title;
    private boolean driver;
    private boolean active;
    private User owner;

    @Nullable
    private User assignee;

    public boolean hasAssignee() {
        return assignee != null;
    }

    public boolean matchesQuery(String query) {
        return title.toLowerCase().contains(query.toLowerCase());
    }
}
