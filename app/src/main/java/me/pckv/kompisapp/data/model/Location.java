package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {

    private float latitude;
    private float longitude;
    private float accuracy;
}
