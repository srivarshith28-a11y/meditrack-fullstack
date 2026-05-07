package com.meditrak.service;

import com.meditrak.dto.HospitalForm;
import com.meditrak.dto.FacilityMapPoint;
import com.meditrak.dto.PriorityWasteView;
import com.meditrak.dto.RouteOptimizationRequest;
import com.meditrak.dto.RouteMapPath;
import com.meditrak.dto.WasteEntryForm;
import com.meditrak.model.Hospital;
import com.meditrak.model.RoutePlan;
import com.meditrak.model.WasteEntry;
import com.meditrak.repository.HospitalRepository;
import com.meditrak.repository.RouteRepository;
import com.meditrak.repository.WasteRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MediTrackService {
    private final HospitalRepository hospitalRepository;
    private final WasteRepository wasteRepository;
    private final RouteRepository routeRepository;

    public MediTrackService(HospitalRepository hospitalRepository, WasteRepository wasteRepository, RouteRepository routeRepository) {
        this.hospitalRepository = hospitalRepository;
        this.wasteRepository = wasteRepository;
        this.routeRepository = routeRepository;
    }

    public List<Hospital> getHospitals() {
        return hospitalRepository.findAll();
    }

    public void registerHospital(HospitalForm form) {
        hospitalRepository.save(form);
    }

    public void addWaste(WasteEntryForm form) {
        wasteRepository.save(form);
    }

    public List<WasteEntry> getWasteEntries() {
        return wasteRepository.findAll();
    }

    public List<RoutePlan> getRoutes() {
        return enrichRoutePlans(routeRepository.findAll(), hospitalRepository.findAll(), wasteRepository.findAll(), "All");
    }

    public List<RoutePlan> optimizeRoutes(RouteOptimizationRequest request) {
        String requestedZone = normalizeZone(request.getZone());
        List<RoutePlan> baseRoutes = "All".equals(requestedZone)
            ? routeRepository.findAll()
            : routeRepository.findByZone(requestedZone);

        if (baseRoutes.isEmpty()) {
            return List.of();
        }

        List<Hospital> hospitals = hospitalRepository.findAll();
        List<WasteEntry> wasteEntries = wasteRepository.findAll();
        Map<Long, Hospital> hospitalById = hospitals.stream()
            .collect(Collectors.toMap(Hospital::getId, hospital -> hospital));

        Map<String, Double> zonePendingWaste = new HashMap<>();
        Map<String, Integer> zoneActiveStops = new HashMap<>();
        Map<String, Set<Long>> zoneFacilityIds = new HashMap<>();

        for (WasteEntry entry : wasteEntries) {
            Hospital hospital = hospitalById.get(entry.getHospitalId());
            if (hospital == null || "Collected".equalsIgnoreCase(entry.getCollectionStatus())) {
                continue;
            }

            if (!"All".equals(requestedZone) && !requestedZone.equalsIgnoreCase(hospital.getZone())) {
                continue;
            }

            zonePendingWaste.merge(hospital.getZone(), entry.getQuantityKg(), Double::sum);
            zoneFacilityIds.computeIfAbsent(hospital.getZone(), key -> new java.util.LinkedHashSet<>()).add(hospital.getId());
        }

        zoneFacilityIds.forEach((zone, ids) -> zoneActiveStops.put(zone, ids.size()));

        Map<String, List<RoutePlan>> routesByZone = baseRoutes.stream()
            .collect(Collectors.groupingBy(RoutePlan::getZone, LinkedHashMap::new, Collectors.toList()));

        List<RoutePlan> optimized = new ArrayList<>();
        for (Map.Entry<String, List<RoutePlan>> entry : routesByZone.entrySet()) {
            String zone = entry.getKey();
            List<RoutePlan> zoneRoutes = entry.getValue();
            double pendingWaste = zonePendingWaste.getOrDefault(zone, zoneRoutes.stream()
                .mapToDouble(route -> route.getEstimatedWasteKg() == null ? 0.0 : route.getEstimatedWasteKg())
                .sum());
            int activeStops = Math.max(1, zoneActiveStops.getOrDefault(zone, 1));
            double zoneUrgency = calculateZoneUrgency(zone, wasteEntries, hospitalById, requestedZone);

            double baseWeight = zoneRoutes.stream()
                .mapToDouble(route -> route.getEstimatedWasteKg() == null ? 0.0 : route.getEstimatedWasteKg())
                .sum();
            double fallbackWeight = zoneRoutes.isEmpty() ? 1.0 : zoneRoutes.size();

            for (RoutePlan route : zoneRoutes) {
                double routeWeight = route.getEstimatedWasteKg() == null ? 0.0 : route.getEstimatedWasteKg();
                double share = baseWeight > 0 ? (routeWeight / baseWeight) : (1.0 / fallbackWeight);
                double optimizedWaste = roundToTwoDecimals(Math.max(6.0, pendingWaste * share));
                double distanceReduction = Math.min(
                    5.5,
                    (activeStops * 0.55) + ((pendingWaste / Math.max(1, zoneRoutes.size())) * 0.025) + (zoneUrgency * 0.01)
                );
                double optimizedDistance = roundToTwoDecimals(Math.max(
                    6.0,
                    route.getOptimizedDistanceKm() - distanceReduction
                ));
                double efficiencyScore = calculateEfficiencyScore(
                    route.getOptimizedDistanceKm(),
                    optimizedDistance,
                    optimizedWaste,
                    zoneUrgency,
                    activeStops
                );

                optimized.add(new RoutePlan(
                    route.getRouteId(),
                    route.getZone(),
                    route.getVehicleNumber(),
                    route.getDriverName(),
                    optimizedWaste,
                    optimizedDistance,
                    efficiencyScore
                ));
            }
        }

        optimized.sort(Comparator.comparing(RoutePlan::getZone).thenComparing(RoutePlan::getOptimizedDistanceKm));
        return optimized;
    }

    public Map<String, Object> dashboardSummary() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        List<WasteEntry> wasteEntries = wasteRepository.findAll();
        List<RoutePlan> routes = enrichRoutePlans(routeRepository.findAll(), hospitals, wasteEntries, "All");
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("hospitalCount", hospitals.size());
        summary.put("wasteEntries", wasteEntries.size());
        summary.put("routes", routes.size());
        summary.put("northZoneWasteKg", wasteRepository.totalWasteByZone("North"));
        summary.put("scheduledCount", countByStatus(wasteEntries, "Scheduled"));
        summary.put("inTransitCount", countByStatus(wasteEntries, "In Transit"));
        summary.put("collectedCount", countByStatus(wasteEntries, "Collected"));
        summary.put("urgentPickups", getPriorityWasteEntries().stream()
            .filter(item -> !"Low".equals(item.getPriorityLabel()))
            .count());
        summary.put("averageEfficiencyScore", roundToTwoDecimals(routes.stream()
            .mapToDouble(route -> route.getEfficiencyScore() == null ? 0.0 : route.getEfficiencyScore())
            .average()
            .orElse(0.0)));
        return summary;
    }

    public List<PriorityWasteView> getPriorityWasteEntries() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        Map<Long, Hospital> hospitalById = hospitals.stream()
            .collect(Collectors.toMap(Hospital::getId, hospital -> hospital));

        return wasteRepository.findAll().stream()
            .filter(entry -> !"Collected".equalsIgnoreCase(entry.getCollectionStatus()))
            .map(entry -> {
                Hospital hospital = hospitalById.get(entry.getHospitalId());
                String hospitalName = hospital == null ? "Unknown Facility" : hospital.getName();
                String zone = hospital == null ? "Unassigned" : hospital.getZone();
                double priorityScore = calculatePriorityScore(entry);
                return new PriorityWasteView(
                    hospitalName,
                    zone,
                    entry.getWasteType(),
                    entry.getQuantityKg(),
                    entry.getCollectionStatus(),
                    entry.getPickupDate().toString(),
                    priorityLabel(priorityScore),
                    priorityScore
                );
            })
            .sorted(Comparator.comparing(PriorityWasteView::getPriorityScore).reversed())
            .limit(6)
            .collect(Collectors.toList());
    }

    public List<FacilityMapPoint> getFacilityMapPoints() {
        Map<String, List<Double>> coordinates = new HashMap<>();
        coordinates.put("City Heart Hospital", Arrays.asList(12.9719, 77.5948));
        coordinates.put("Riverfront Clinic", Arrays.asList(9.9667, 76.2813));
        coordinates.put("North Diagnostic Lab", Arrays.asList(13.0829, 80.2707));
        coordinates.put("Metro Blood Bank", Arrays.asList(17.3852, 78.4867));
        coordinates.put("Sunrise Medical Center", Arrays.asList(18.5204, 73.8567));
        coordinates.put("Green Valley Hospital", Arrays.asList(11.0168, 76.9558));
        coordinates.put("CarePoint Specialty Clinic", Arrays.asList(17.6868, 83.2185));
        coordinates.put("Unity Trauma Centre", Arrays.asList(12.2958, 76.6394));

        List<FacilityMapPoint> points = new ArrayList<>();
        for (Hospital hospital : hospitalRepository.findAll()) {
            List<Double> coordinate = coordinates.getOrDefault(hospital.getName(), defaultCoordinatesForZone(hospital.getZone()));
            points.add(new FacilityMapPoint(
                hospital.getId(),
                hospital.getName(),
                hospital.getZone(),
                hospital.getAddress(),
                coordinate.get(0),
                coordinate.get(1)
            ));
        }
        return points;
    }

    public List<RouteMapPath> getRouteMapPaths() {
        Map<String, String> colors = Map.of(
            "North", "#0fa48f",
            "South", "#2a6fdb",
            "East", "#f08c2e",
            "West", "#915eff"
        );
        Map<String, List<List<Double>>> zonePaths = buildZonePaths();

        List<RouteMapPath> paths = new ArrayList<>();
        for (RoutePlan route : routeRepository.findAll()) {
            paths.add(new RouteMapPath(
                route.getZone(),
                route.getVehicleNumber(),
                route.getDriverName(),
                colors.getOrDefault(route.getZone(), "#0fa48f"),
                zonePaths.getOrDefault(route.getZone(), zonePaths.get("North"))
            ));
        }
        return paths;
    }

    private List<RoutePlan> enrichRoutePlans(
        List<RoutePlan> baseRoutes,
        List<Hospital> hospitals,
        List<WasteEntry> wasteEntries,
        String requestedZone
    ) {
        Map<Long, Hospital> hospitalById = hospitals.stream()
            .collect(Collectors.toMap(Hospital::getId, hospital -> hospital));

        Map<String, Double> pendingWasteByZone = new HashMap<>();
        Map<String, Set<Long>> activeStopsByZone = new HashMap<>();
        Map<String, Double> urgencyByZone = new HashMap<>();

        for (WasteEntry entry : wasteEntries) {
            Hospital hospital = hospitalById.get(entry.getHospitalId());
            if (hospital == null || "Collected".equalsIgnoreCase(entry.getCollectionStatus())) {
                continue;
            }

            if (!"All".equals(requestedZone) && !requestedZone.equalsIgnoreCase(hospital.getZone())) {
                continue;
            }

            pendingWasteByZone.merge(hospital.getZone(), entry.getQuantityKg(), Double::sum);
            activeStopsByZone.computeIfAbsent(hospital.getZone(), key -> new java.util.LinkedHashSet<>()).add(hospital.getId());
            urgencyByZone.merge(hospital.getZone(), calculatePriorityScore(entry), Double::sum);
        }

        return baseRoutes.stream()
            .map(route -> {
                int activeStops = Math.max(1, activeStopsByZone.getOrDefault(route.getZone(), Set.of()).size());
                double pendingWaste = pendingWasteByZone.getOrDefault(route.getZone(), route.getEstimatedWasteKg());
                double urgency = urgencyByZone.getOrDefault(route.getZone(), 15.0);
                double efficiencyScore = calculateEfficiencyScore(
                    route.getOptimizedDistanceKm(),
                    route.getOptimizedDistanceKm(),
                    pendingWaste / activeStops,
                    urgency,
                    activeStops
                );
                return new RoutePlan(
                    route.getRouteId(),
                    route.getZone(),
                    route.getVehicleNumber(),
                    route.getDriverName(),
                    route.getEstimatedWasteKg(),
                    route.getOptimizedDistanceKm(),
                    efficiencyScore
                );
            })
            .collect(Collectors.toList());
    }

    private Map<String, List<List<Double>>> buildZonePaths() {
        Map<String, List<List<Double>>> depotByZone = Map.of(
            "North", Arrays.asList(Arrays.asList(12.9980, 77.6030)),
            "South", Arrays.asList(Arrays.asList(10.0159, 76.3419)),
            "East", Arrays.asList(Arrays.asList(17.4018, 78.5204)),
            "West", Arrays.asList(Arrays.asList(18.5074, 73.8077))
        );

        Map<String, List<List<Double>>> zonePaths = new HashMap<>();
        for (FacilityMapPoint point : getFacilityMapPoints()) {
            zonePaths.computeIfAbsent(point.getZone(), key -> new ArrayList<>())
                .add(Arrays.asList(point.getLatitude(), point.getLongitude()));
        }

        zonePaths.replaceAll((zone, coordinates) -> {
            coordinates.sort(Comparator
                .comparing((List<Double> point) -> point.get(0))
                .thenComparing(point -> point.get(1)));
            List<List<Double>> finalPath = new ArrayList<>(depotByZone.getOrDefault(zone, List.of()));
            finalPath.addAll(coordinates);
            return finalPath;
        });

        return zonePaths;
    }

    private List<Double> defaultCoordinatesForZone(String zone) {
        return switch (zone) {
            case "North" -> Arrays.asList(12.9980, 77.6030);
            case "South" -> Arrays.asList(10.0080, 76.3020);
            case "East" -> Arrays.asList(17.4100, 78.5050);
            case "West" -> Arrays.asList(12.9200, 77.4900);
            default -> Arrays.asList(12.9716, 77.5946);
        };
    }

    private String normalizeZone(String zone) {
        if (zone == null || zone.isBlank()) {
            return "All";
        }
        return zone.trim();
    }

    private long countByStatus(List<WasteEntry> wasteEntries, String status) {
        return wasteEntries.stream()
            .filter(entry -> status.equalsIgnoreCase(entry.getCollectionStatus()))
            .count();
    }

    private double calculateZoneUrgency(
        String zone,
        List<WasteEntry> wasteEntries,
        Map<Long, Hospital> hospitalById,
        String requestedZone
    ) {
        return wasteEntries.stream()
            .filter(entry -> {
                Hospital hospital = hospitalById.get(entry.getHospitalId());
                if (hospital == null || "Collected".equalsIgnoreCase(entry.getCollectionStatus())) {
                    return false;
                }
                if (!zone.equalsIgnoreCase(hospital.getZone())) {
                    return false;
                }
                return "All".equals(requestedZone) || requestedZone.equalsIgnoreCase(hospital.getZone());
            })
            .mapToDouble(this::calculatePriorityScore)
            .average()
            .orElse(15.0);
    }

    private double calculatePriorityScore(WasteEntry entry) {
        long daysUntilPickup = ChronoUnit.DAYS.between(LocalDate.now(), entry.getPickupDate());

        double dateScore;
        if (daysUntilPickup < 0) {
            dateScore = 42.0;
        } else if (daysUntilPickup == 0) {
            dateScore = 34.0;
        } else if (daysUntilPickup == 1) {
            dateScore = 28.0;
        } else if (daysUntilPickup <= 3) {
            dateScore = 18.0;
        } else {
            dateScore = 10.0;
        }

        double statusScore = switch (entry.getCollectionStatus()) {
            case "Scheduled" -> 18.0;
            case "In Transit" -> 12.0;
            default -> 0.0;
        };

        double binScore = switch (entry.getBinColor()) {
            case "Yellow" -> 12.0;
            case "White" -> 10.0;
            case "Red" -> 7.0;
            case "Blue" -> 4.0;
            default -> 3.0;
        };

        double loadScore = Math.min(24.0, entry.getQuantityKg() * 0.75);
        return roundToTwoDecimals(dateScore + statusScore + binScore + loadScore);
    }

    private String priorityLabel(double priorityScore) {
        if (priorityScore >= 72.0) {
            return "Critical";
        }
        if (priorityScore >= 54.0) {
            return "High";
        }
        if (priorityScore >= 36.0) {
            return "Medium";
        }
        return "Low";
    }

    private double calculateEfficiencyScore(
        double baseDistance,
        double optimizedDistance,
        double routeWaste,
        double urgencyScore,
        int activeStops
    ) {
        double distanceGain = baseDistance <= 0 ? 0.0 : Math.max(0.0, (baseDistance - optimizedDistance) / baseDistance);
        double loadUtilization = Math.min(1.0, routeWaste / Math.max(18.0, activeStops * 14.0));
        double urgencyFactor = Math.min(1.0, urgencyScore / 90.0);
        double score = (distanceGain * 42.0) + (loadUtilization * 33.0) + (urgencyFactor * 25.0);
        return roundToTwoDecimals(Math.min(99.0, Math.max(48.0, score + 28.0)));
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
