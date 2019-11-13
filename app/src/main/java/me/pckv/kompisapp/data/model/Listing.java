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
}
