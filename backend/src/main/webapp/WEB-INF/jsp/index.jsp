<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MediTrack | Full Stack Biomedical Waste Management</title>
  <link rel="stylesheet" href="/css/site.css">
</head>
<body>
  <header class="topbar">
    <div>
      <div class="brand-lockup">
        <div class="brand-mark">MT</div>
        <div class="brand-copy">
          <p class="eyebrow">MediTrack</p>
          <p>Collection control for healthcare waste operations.</p>
        </div>
      </div>
      <h1>Biomedical Waste Collection and Route Optimization System</h1>
      <p class="lead">A unified platform for biomedical waste tracking, pickup coordination, route visibility, and operational reporting across healthcare facilities.</p>
    </div>
    <nav class="nav-links">
      <a href="/login">Login</a>
      <a href="/hospitals">Hospital Registration</a>
      <a href="/waste-entry">Waste Entry</a>
      <a href="/reports">Reports</a>
      <a href="/react/dashboard.html">React Dashboard</a>
    </nav>
  </header>

  <main class="page-grid">
    <section class="hero-shell">
      <article class="card hero-panel">
        <span class="hero-ribbon">Active operational intelligence</span>
        <h2>Safer collection. Smarter dispatch. Better oversight.</h2>
        <p class="section-copy">
          MediTrack gives biomedical waste teams a calm, high-visibility workspace for facility records,
          waste movement, dispatch coordination, route monitoring, and review-ready reporting.
        </p>
        <div class="hero-actions">
          <a class="pill-link" href="/react/dashboard.html">Open Dashboard</a>
          <a class="ghost-link" href="/reports">View Reports</a>
        </div>
        <div class="hero-summary">
          <article>
            <strong>${summary.hospitalCount}</strong>
            <span>Facilities under visibility</span>
          </article>
          <article>
            <strong>${summary.urgentPickups}</strong>
            <span>Priority pickups requiring attention</span>
          </article>
          <article>
            <strong>${summary.averageEfficiencyScore}%</strong>
            <span>Average route performance score</span>
          </article>
        </div>
      </article>

      <aside class="signal-stack">
        <article class="signal-card">
          <p class="metric-kicker">Dispatch View</p>
          <h3>Today’s operational pulse</h3>
          <p>Watch route readiness, urgent pickups, and live service coverage from a single command view.</p>
          <div class="route-pulse">
            <div><strong>Scheduled pickups</strong><span>${summary.scheduledCount}</span></div>
            <div><strong>In transit</strong><span>${summary.inTransitCount}</span></div>
            <div><strong>Collected loads</strong><span>${summary.collectedCount}</span></div>
          </div>
        </article>
        <section class="info-cluster">
          <article>
            <strong>Priority handling</strong>
            <p class="muted">Urgent waste entries float to the top of the queue based on timing, status, and sensitivity.</p>
          </article>
          <article>
            <strong>Route confidence</strong>
            <p class="muted">Vehicle assignments and travel distance stay visible before teams move into collection windows.</p>
          </article>
        </section>
      </aside>
    </section>

    <section class="stats-grid">
      <article class="card stat-card">
        <span>Registered Facilities</span>
        <strong>${summary.hospitalCount}</strong>
      </article>
      <article class="card stat-card">
        <span>Waste Records</span>
        <strong>${summary.wasteEntries}</strong>
      </article>
      <article class="card stat-card">
        <span>Active Routes</span>
        <strong>${summary.routes}</strong>
      </article>
      <article class="card stat-card">
        <span>North Zone Load</span>
        <strong>${summary.northZoneWasteKg} kg</strong>
      </article>
    </section>

    <section class="feature-band">
      <article>
        <p class="metric-kicker">Facility Layer</p>
        <h3>Structured onboarding</h3>
        <p class="muted">Register hospitals, assign operating zones, and keep contact details organized for cleaner coordination.</p>
      </article>
      <article>
        <p class="metric-kicker">Waste Layer</p>
        <h3>Accurate movement records</h3>
        <p class="muted">Capture waste type, quantity, handling color, and pickup timing in one clean workflow.</p>
      </article>
      <article>
        <p class="metric-kicker">Route Layer</p>
        <h3>Dispatch-ready visibility</h3>
        <p class="muted">Review route score, driver allocation, and zone coverage before dispatch teams move.</p>
      </article>
    </section>

    <section class="card">
      <h2>Route Assignment Overview</h2>
      <p class="table-subtitle">Current route allocation by zone, assigned driver, projected load, and optimized travel distance.</p>
      <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Zone</th>
            <th>Vehicle</th>
            <th>Driver</th>
            <th>Waste (kg)</th>
            <th>Distance (km)</th>
            <th>Score</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${routes}" var="route">
            <tr>
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
    </section>
  </main>
</body>
</html>
