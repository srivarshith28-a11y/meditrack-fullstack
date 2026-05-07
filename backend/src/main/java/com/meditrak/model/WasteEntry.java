package com.meditrak.model;

import java.time.LocalDate;

public class WasteEntry {
    private Long id;
    private Long hospitalId;
    private String wasteType;
    private Double quantityKg;
    private String binColor;
    private String collectionStatus;
    private LocalDate pickupDate;

    public WasteEntry() {
    }

    public WasteEntry(Long id, Long hospitalId, String wasteType, Double quantityKg, String binColor,
                      String collectionStatus, LocalDate pickupDate) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.wasteType = wasteType;
        this.quantityKg = quantityKg;
        this.binColor = binColor;
        this.collectionStatus = collectionStatus;
        this.pickupDate = pickupDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getWasteType() { return wasteType; }
    public void setWasteType(String wasteType) { this.wasteType = wasteType; }
    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }
    public String getBinColor() { return binColor; }
    public void setBinColor(String binColor) { this.binColor = binColor; }
    public String getCollectionStatus() { return collectionStatus; }
    public void setCollectionStatus(String collectionStatus) { this.collectionStatus = collectionStatus; }
    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
}
