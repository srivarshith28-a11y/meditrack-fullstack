<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MediTrack Reports</title>
  <link rel="stylesheet" href="/css/site.css">
</head>
<body>
  <main class="page-shell">
    <section class="page-banner">
      <div>
        <p class="eyebrow">Performance Center</p>
        <h1>Operational Reports</h1>
        <p>Review route performance, service activity, and current zone-level load from one consolidated screen.</p>
      </div>
      <div class="page-actions">
        <a class="pill-link" href="/">Back to home</a>
        <a class="pill-link" href="/react/dashboard.html">Open live dashboard</a>
      </div>
    </section>
  <section class="page-grid">
    <section class="card">
      <h1>Operational Reports</h1>
      <div class="stats-grid">
        <article class="stat-card">
          <span>Total Facilities</span>
          <strong>${summary.hospitalCount}</strong>
        </article>
        <article class="stat-card">
          <span>Total Waste Records</span>
          <strong>${summary.wasteEntries}</strong>
        </article>
        <article class="stat-card">
          <span>Scheduled Pickups</span>
          <strong>${summary.scheduledCount}</strong>
        </article>
        <article class="stat-card">
          <span>In Transit</span>
          <strong>${summary.inTransitCount}</strong>
        </article>
        <article class="stat-card">
          <span>Urgent Pickups</span>
          <strong>${summary.urgentPickups}</strong>
        </article>
        <article class="stat-card">
          <span>Avg Route Score</span>
          <strong>${summary.averageEfficiencyScore}%</strong>
        </article>
        <article class="stat-card">
          <span>North Zone Load</span>
          <strong>${summary.northZoneWasteKg} kg</strong>
        </article>
      </div>
    </section>

    <section class="card">
      <h2>Route Performance Summary</h2>
      <p class="table-subtitle">Compare route assignments, projected waste load, assigned driver, and optimized distance by zone.</p>
      <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Route ID</th>
            <th>Zone</th>
            <th>Vehicle</th>
            <th>Driver</th>
            <th>Waste</th>
            <th>Distance</th>
            <th>Score</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${routes}" var="route">
            <tr>
              <td>${route.routeId}</td>
              <td>${route.zone}</td>
              <td>${route.vehicleNumber}</td>
              <td>${route.driverName}</td>
              <td>${route.estimatedWasteKg}</td>
              <td>${route.optimizedDistanceKm}</td>
              <td>${route.efficiencyScore}%</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      </div>
      <a href="/react/dashboard.html">Open live dashboard</a>
    </section>

    <section class="card">
      <h2>Priority Pickup Queue</h2>
      <p class="table-subtitle">Entries are ranked using pickup urgency, current status, waste load, and handling sensitivity.</p>
      <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Facility</th>
            <th>Zone</th>
            <th>Waste Type</th>
            <th>Status</th>
            <th>Pickup Date</th>
            <th>Priority</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${priorityPickups}" var="pickup">
            <tr>
              <td>${pickup.hospitalName}</td>
              <td>${pickup.zone}</td>
              <td>${pickup.wasteType}</td>
              <td>${pickup.collectionStatus}</td>
              <td>${pickup.pickupDate}</td>
              <td>${pickup.priorityLabel}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      </div>
    </section>
  </section>
  </main>
</body>
</html>
