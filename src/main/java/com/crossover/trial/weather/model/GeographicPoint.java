package com.crossover.trial.weather.model;

/**
 * Created by sogla on 25/09/2017.
 */
public class GeographicPoint {
    /**
     * latitude value in degrees
     */
    private double latitude;
    /**
     * longitude value in degrees
     */
    private double longitude;

    public GeographicPoint() {
    }

    public GeographicPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
