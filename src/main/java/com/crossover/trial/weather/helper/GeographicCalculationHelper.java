package com.crossover.trial.weather.helper;

import com.crossover.trial.weather.model.GeographicPoint;
import org.springframework.stereotype.Component;

/**
 * Created by sogla on 25/09/2017.
 */
@Component
public class GeographicCalculationHelper {


    /**
     * Earth radius in KM.
     */
    public static final double EARTH_RADIUS = 6372.8;

    /**
     * Haversine distance between two geographic points.
     *
     * @param geographicPoint1 first point
     * @param geographicPoint2 second point
     * @return the distance in KM
     */
    public double calculateDistance(GeographicPoint geographicPoint1, GeographicPoint geographicPoint2) {
        double deltaLatitude = Math.toRadians(geographicPoint2.getLatitude() - geographicPoint1.getLatitude());
        double deltaLongitude = Math.toRadians(geographicPoint2.getLongitude() - geographicPoint1.getLongitude());
        double a = Math.pow(Math.sin(deltaLatitude / 2), 2) + Math.pow(Math.sin(deltaLongitude / 2), 2)
                * Math.cos(geographicPoint1.getLatitude()) * Math.cos(geographicPoint2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }


}
