package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Location {

    private float latitude;
    private float longitude;
    private float accuracy;
}
