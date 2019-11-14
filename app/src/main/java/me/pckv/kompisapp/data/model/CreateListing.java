package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateListing {

    private String title;
    private boolean driver;
}
