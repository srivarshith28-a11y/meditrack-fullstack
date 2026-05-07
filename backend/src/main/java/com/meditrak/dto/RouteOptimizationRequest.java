package com.meditrak.dto;

import jakarta.validation.constraints.NotBlank;

public class RouteOptimizationRequest {
    @NotBlank
    private String zone;

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
}
