<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Waste Entry</title>
  <link rel="stylesheet" href="/css/site.css">
</head>
<body>
  <main class="page-shell">
    <section class="page-banner">
      <div>
        <p class="eyebrow">Waste Operations</p>
        <h1>Waste Entry</h1>
        <p>Capture scheduled collection records with waste type, quantity, bin category, and pickup date.</p>
      </div>
      <div class="page-actions">
        <a class="pill-link" href="/">Back to home</a>
      </div>
    </section>
    <section class="split-page">
    <section class="card form-card">
      <h1>Waste Entry</h1>
      <form method="post" action="/waste-entry" class="stack-form validate-form">
        <label>Hospital
          <select name="hospitalId" required>
            <c:forEach items="${hospitals}" var="hospital">
              <option value="${hospital.id}">${hospital.name} - ${hospital.zone}</option>
            </c:forEach>
          </select>
        </label>
        <label>Waste Type <input type="text" name="wasteType" required></label>
        <label>Quantity (kg) <input type="number" step="0.1" name="quantityKg" required></label>
        <label>Bin Color
          <select name="binColor" required>
            <option>Yellow</option>
            <option>Red</option>
            <option>Blue</option>
            <option>White</option>
          </select>
        </label>
        <label>Pickup Date <input type="date" name="pickupDate" required></label>
        <button type="submit">Save Record</button>
        <p class="message">${message}</p>
      </form>
    </section>

    <section class="card">
      <h2>Recent Waste Records</h2>
      <p class="table-subtitle">Latest waste activity across facilities, including type, quantity, current status, and collection date.</p>
      <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Hospital ID</th>
            <th>Type</th>
            <th>Quantity</th>
            <th>Bin</th>
            <th>Status</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody id="wasteTableBody">
          <c:forEach items="${wasteEntries}" var="entry">
            <tr>
              <td>${entry.hospitalId}</td>
              <td>${entry.wasteType}</td>
              <td>${entry.quantityKg}</td>
              <td>${entry.binColor}</td>
              <td>${entry.collectionStatus}</td>
              <td>${entry.pickupDate}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      </div>
    </section>
    </section>
  </main>
  <script src="/js/forms.js"></script>
</body>
</html>
