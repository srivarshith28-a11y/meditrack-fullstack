const { useEffect, useRef, useState } = React;

function DashboardCard({ title, value }) {
  return (
    <article className="stat-card">
      <span>{title}</span>
      <strong>{value}</strong>
    </article>
  );
}

function RouteMap({ facilities, mapRoutes, zone }) {
  const mapRef = useRef(null);
  const leafletMapRef = useRef(null);
  const activeLayersRef = useRef([]);

  useEffect(() => {
    if (!window.L || !mapRef.current || leafletMapRef.current) {
      return;
    }

    leafletMapRef.current = window.L.map(mapRef.current).setView([12.9716, 77.5946], 5);

    window.L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "&copy; OpenStreetMap contributors"
    }).addTo(leafletMapRef.current);
  }, []);

  useEffect(() => {
    const map = leafletMapRef.current;
    if (!map) {
      return;
    }

    activeLayersRef.current.forEach((layer) => map.removeLayer(layer));
    activeLayersRef.current = [];

    const visibleFacilities = zone === "All"
      ? facilities
      : facilities.filter((facility) => facility.zone === zone);
    const visibleMapRoutes = zone === "All"
      ? mapRoutes
      : mapRoutes.filter((route) => route.zone === zone);

    const bounds = [];

    visibleFacilities.forEach((facility) => {
      const marker = window.L.circleMarker([facility.latitude, facility.longitude], {
        radius: 8,
        color: "#0d5c63",
        weight: 2,
        fillColor: "#0fa48f",
        fillOpacity: 0.92
      }).addTo(map);

      marker.bindPopup(`
        <strong>${facility.name}</strong><br/>
        Zone: ${facility.zone}<br/>
        ${facility.address}
      `);

      activeLayersRef.current.push(marker);
      bounds.push([facility.latitude, facility.longitude]);
    });

    visibleMapRoutes.forEach((route) => {
      const polyline = window.L.polyline(route.path, {
        color: route.color,
        weight: 4,
        opacity: 0.85
      }).addTo(map);

      polyline.bindPopup(`
        <strong>${route.vehicleNumber}</strong><br/>
        Driver: ${route.driverName}<br/>
        Zone: ${route.zone}
      `);

      activeLayersRef.current.push(polyline);
      route.path.forEach((point) => bounds.push(point));
    });

    if (bounds.length > 0) {
      map.fitBounds(bounds, { padding: [28, 28] });
    }
  }, [facilities, mapRoutes, zone]);

  return <div id="route-map" ref={mapRef}></div>;
}

function App() {
  const [summary, setSummary] = useState({});
  const [report, setReport] = useState({ routes: [], facilities: [], mapRoutes: [], priorityPickups: [] });
  const [zone, setZone] = useState("All");
  const [optimizedRoutes, setOptimizedRoutes] = useState([]);

  useEffect(() => {
    fetch("/api/summary").then((res) => res.json()).then(setSummary);
    fetch("/api/getReport").then((res) => res.json()).then(setReport);
  }, []);

  const optimize = async () => {
    const response = await fetch("/api/optimizeRoute", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ zone })
    });
    const data = await response.json();
    setOptimizedRoutes(data);
  };

  return (
    <div className="page-grid">
      <section className="stats-grid">
        <DashboardCard title="Facilities" value={summary.hospitalCount ?? "-"} />
        <DashboardCard title="Waste Records" value={summary.wasteEntries ?? "-"} />
        <DashboardCard title="Urgent Pickups" value={summary.urgentPickups ?? "-"} />
        <DashboardCard title="Active Routes" value={summary.routes ?? "-"} />
        <DashboardCard title="Avg Route Score" value={summary.averageEfficiencyScore ? `${summary.averageEfficiencyScore}%` : "-"} />
        <DashboardCard title="North Zone Load" value={`${summary.northZoneWasteKg ?? "-"} kg`} />
      </section>

      <section className="card">
        <h2>Collection Route Map</h2>
        <p className="table-subtitle">Track facility locations and route coverage across active service zones.</p>
        <RouteMap facilities={report.facilities || []} mapRoutes={report.mapRoutes || []} zone={zone} />
        <p className="dashboard-note">Markers represent registered facilities. Colored lines represent route coverage by zone.</p>
      </section>

      <section className="card">
        <h2>Optimize Route by Zone</h2>
        <p className="table-subtitle">Select a zone and generate the current optimized route view.</p>
        <div className="toolbar">
          <select value={zone} onChange={(event) => setZone(event.target.value)}>
            <option>All</option>
            <option>North</option>
            <option>South</option>
            <option>East</option>
            <option>West</option>
          </select>
          <button onClick={optimize}>Run Optimization</button>
        </div>
        <div className="table-wrap">
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
            {(optimizedRoutes.length > 0 ? optimizedRoutes : report.routes || []).map((route) => (
              <tr key={route.routeId}>
                <td>{route.zone}</td>
                <td>{route.vehicleNumber}</td>
                <td>{route.driverName}</td>
                <td>{route.estimatedWasteKg}</td>
                <td>{route.optimizedDistanceKm}</td>
                <td>{route.efficiencyScore ? `${route.efficiencyScore}%` : "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
        <p className="dashboard-note">Scores reflect route distance efficiency, active load, and pickup urgency within the selected zone.</p>
      </section>

      <section className="dashboard-grid">
        <section className="card">
        <h2>Route Report Output</h2>
        <p className="table-subtitle">Consolidated output of the current route performance dataset.</p>
        <div className="table-wrap">
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
            {(report.routes || []).map((route) => (
              <tr key={route.routeId}>
                <td>{route.zone}</td>
                <td>{route.vehicleNumber}</td>
                <td>{route.driverName}</td>
                <td>{route.estimatedWasteKg}</td>
                <td>{route.optimizedDistanceKm}</td>
                <td>{route.efficiencyScore ? `${route.efficiencyScore}%` : "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </section>

      <section className="card">
        <h2>Priority Dispatch Queue</h2>
        <p className="table-subtitle">Entries are ranked by pickup date urgency, current status, bin type, and waste load.</p>
        <div className="table-wrap">
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
            {(report.priorityPickups || []).map((pickup, index) => (
              <tr key={`${pickup.hospitalName}-${pickup.pickupDate}-${index}`}>
                <td>{pickup.hospitalName}</td>
                <td>{pickup.zone}</td>
                <td>{pickup.wasteType}</td>
                <td>{pickup.collectionStatus}</td>
                <td>{pickup.pickupDate}</td>
                <td>{pickup.priorityLabel}</td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </section>
      </section>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById("root")).render(<App />);
