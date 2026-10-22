package com.example.myapplication;

public class BusLocation {
    public double latitude;
    public double longitude;
    public float bearing;
    public float speed;
    public long timestamp;

    public BusLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(BusLocation.class)
    }

    public BusLocation(double latitude, double longitude, float bearing, float speed, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.speed = speed;
        this.timestamp = timestamp;
    }
}
