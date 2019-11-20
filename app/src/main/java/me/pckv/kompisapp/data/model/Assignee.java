package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Assignee {

    private User user;
    private Location location;
}
