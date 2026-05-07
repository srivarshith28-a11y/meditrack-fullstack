package com.meditrak.dto;

import java.util.List;

public class RouteMapPath {
    private final String zone;
    private final String vehicleNumber;
    private final String driverName;
    private final String color;
    private final List<List<Double>> path;

    public RouteMapPath(String zone, String vehicleNumber, String driverName, String color, List<List<Double>> path) {
        this.zone = zone;
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.color = color;
        this.path = path;
    }

    public String getZone() { return zone; }
    public String getVehicleNumber() { return vehicleNumber; }
    public String getDriverName() { return driverName; }
    public String getColor() { return color; }
    public List<List<Double>> getPath() { return path; }
}
