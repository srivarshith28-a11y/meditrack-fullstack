package com.meditrak.dto;

public class FacilityMapPoint {
    private final Long id;
    private final String name;
    private final String zone;
    private final String address;
    private final double latitude;
    private final double longitude;

    public FacilityMapPoint(Long id, String name, String zone, String address, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.zone = zone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getZone() { return zone; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
