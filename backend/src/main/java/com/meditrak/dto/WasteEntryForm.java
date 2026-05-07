package com.meditrak.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WasteEntryForm {
    @NotNull
    private Long hospitalId;
    @NotBlank
    private String wasteType;
    @NotNull
    @Min(1)
    private Double quantityKg;
    @NotBlank
    private String binColor;
    @NotBlank
    private String pickupDate;

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getWasteType() { return wasteType; }
    public void setWasteType(String wasteType) { this.wasteType = wasteType; }
    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }
    public String getBinColor() { return binColor; }
    public void setBinColor(String binColor) { this.binColor = binColor; }
    public String getPickupDate() { return pickupDate; }
    public void setPickupDate(String pickupDate) { this.pickupDate = pickupDate; }
}
