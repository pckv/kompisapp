package me.pckv.kompisapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {

    private static final double P = Math.PI / 180;
    private static final double EARTH_RADIUS = 6371;

    private float latitude;
    private float longitude;
    private float accuracy;

    public static Location fromAndroidLocation(android.location.Location location) {
        return new Location((float) location.getLatitude(), (float) location.getLongitude(), location.getAccuracy());
    }

    /**
     * Calculate distance to point using Haversine formula.
     * <p>
     * source: https://stackoverflow.com/a/21623206
     *
     * @param other another location to compare distance to
     * @return distance in kilometers
     */
    public double distanceTo(Location other) {
        double a = 0.5 - Math.cos((other.latitude - latitude) * P) / 2 +
                Math.cos(latitude * P) * Math.cos(other.latitude * P) *
                        (1 - Math.cos((other.longitude - longitude) * P)) / 2;
        return EARTH_RADIUS * 2 * Math.asin(Math.sqrt(a));
    }
}
