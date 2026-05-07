package com.meditrak.model;

public class RoutePlan {
    private Long routeId;
    private String zone;
    private String vehicleNumber;
    private String driverName;
    private Double estimatedWasteKg;
    private Double optimizedDistanceKm;
    private Double efficiencyScore;

    public RoutePlan() {
    }

    public RoutePlan(Long routeId, String zone, String vehicleNumber, String driverName,
                     Double estimatedWasteKg, Double optimizedDistanceKm) {
        this(routeId, zone, vehicleNumber, driverName, estimatedWasteKg, optimizedDistanceKm, null);
    }

    public RoutePlan(Long routeId, String zone, String vehicleNumber, String driverName,
                     Double estimatedWasteKg, Double optimizedDistanceKm, Double efficiencyScore) {
        this.routeId = routeId;
        this.zone = zone;
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.estimatedWasteKg = estimatedWasteKg;
        this.optimizedDistanceKm = optimizedDistanceKm;
        this.efficiencyScore = efficiencyScore;
    }

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public Double getEstimatedWasteKg() { return estimatedWasteKg; }
    public void setEstimatedWasteKg(Double estimatedWasteKg) { this.estimatedWasteKg = estimatedWasteKg; }
    public Double getOptimizedDistanceKm() { return optimizedDistanceKm; }
    public void setOptimizedDistanceKm(Double optimizedDistanceKm) { this.optimizedDistanceKm = optimizedDistanceKm; }
    public Double getEfficiencyScore() { return efficiencyScore; }
    public void setEfficiencyScore(Double efficiencyScore) { this.efficiencyScore = efficiencyScore; }
}
