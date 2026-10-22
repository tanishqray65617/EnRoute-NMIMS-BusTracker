package com.example.myapplication;

public class BusInfo {
    public String busId;
    public String driverName;
    public String contactNumber;
    public String routeInfo;

    public BusInfo() {
        // Default constructor required for DataSnapshot
    }

    public BusInfo(String busId, String driverName, String contactNumber, String routeInfo) {
        this.busId = busId;
        this.driverName = driverName;
        this.contactNumber = contactNumber;
        this.routeInfo = routeInfo;
    }
}
