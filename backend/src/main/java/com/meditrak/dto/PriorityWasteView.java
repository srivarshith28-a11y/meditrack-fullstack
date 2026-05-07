package com.meditrak.dto;

public class PriorityWasteView {
    private final String hospitalName;
    private final String zone;
    private final String wasteType;
    private final Double quantityKg;
    private final String collectionStatus;
    private final String pickupDate;
    private final String priorityLabel;
    private final Double priorityScore;

    public PriorityWasteView(
        String hospitalName,
        String zone,
        String wasteType,
        Double quantityKg,
        String collectionStatus,
        String pickupDate,
        String priorityLabel,
        Double priorityScore
    ) {
        this.hospitalName = hospitalName;
        this.zone = zone;
        this.wasteType = wasteType;
        this.quantityKg = quantityKg;
        this.collectionStatus = collectionStatus;
        this.pickupDate = pickupDate;
        this.priorityLabel = priorityLabel;
        this.priorityScore = priorityScore;
    }

    public String getHospitalName() { return hospitalName; }
    public String getZone() { return zone; }
    public String getWasteType() { return wasteType; }
    public Double getQuantityKg() { return quantityKg; }
    public String getCollectionStatus() { return collectionStatus; }
    public String getPickupDate() { return pickupDate; }
    public String getPriorityLabel() { return priorityLabel; }
    public Double getPriorityScore() { return priorityScore; }
}
