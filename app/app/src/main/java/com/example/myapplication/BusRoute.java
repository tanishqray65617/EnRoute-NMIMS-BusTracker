package com.example.myapplication;

import java.io.Serializable;

public class BusRoute implements Serializable {
    public final String busId;
    public final String busNumber;
    public final String routeCode;
    public final String routeName;
    public final String routePath;
    public final String driverName;
    public final String driverPhone;
    public final String currentStop;
    public final String nextStop;
    public final double defaultLat;
    public final double defaultLng;

    public BusRoute(String busId, String busNumber, String routeCode, String routeName,
                    String routePath, String driverName, String driverPhone,
                    String currentStop, String nextStop, double defaultLat, double defaultLng) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.routePath = routePath;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.currentStop = currentStop;
        this.nextStop = nextStop;
        this.defaultLat = defaultLat;
        this.defaultLng = defaultLng;
    }
}
