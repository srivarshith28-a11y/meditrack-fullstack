package com.meditrak.controller;

import com.meditrak.dto.RouteOptimizationRequest;
import com.meditrak.dto.WasteEntryForm;
import com.meditrak.service.MediTrackService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final MediTrackService mediTrackService;

    public ApiController(MediTrackService mediTrackService) {
        this.mediTrackService = mediTrackService;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return mediTrackService.dashboardSummary();
    }

    @PostMapping("/addWaste")
    public ResponseEntity<Map<String, String>> addWaste(@Valid @RequestBody WasteEntryForm form) {
        mediTrackService.addWaste(form);
        return ResponseEntity.ok(Map.of("message", "Waste entry stored successfully"));
    }

    @PostMapping("/optimizeRoute")
    public ResponseEntity<?> optimizeRoute(@Valid @RequestBody RouteOptimizationRequest request) {
        return ResponseEntity.ok(mediTrackService.optimizeRoutes(request));
    }

    @GetMapping("/getReport")
    public ResponseEntity<?> getReport() {
        return ResponseEntity.ok(Map.of(
            "summary", mediTrackService.dashboardSummary(),
            "routes", mediTrackService.getRoutes(),
            "wasteEntries", mediTrackService.getWasteEntries(),
            "priorityPickups", mediTrackService.getPriorityWasteEntries(),
            "facilities", mediTrackService.getFacilityMapPoints(),
            "mapRoutes", mediTrackService.getRouteMapPaths()
        ));
    }

    @GetMapping("/map-data")
    public ResponseEntity<?> mapData() {
        return ResponseEntity.ok(Map.of(
            "facilities", mediTrackService.getFacilityMapPoints(),
            "mapRoutes", mediTrackService.getRouteMapPaths()
        ));
    }
}
