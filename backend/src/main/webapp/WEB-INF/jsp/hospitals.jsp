<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Hospital Registration</title>
  <link rel="stylesheet" href="/css/site.css">
</head>
<body>
  <main class="page-shell">
    <section class="page-banner">
      <div>
        <p class="eyebrow">Facility Directory</p>
        <h1>Facility Registration</h1>
        <p>Add new facilities and maintain a clean operational list across service zones.</p>
      </div>
      <div class="page-actions">
        <a class="pill-link" href="/">Back to home</a>
      </div>
    </section>
    <section class="split-page">
    <section class="card form-card">
      <h1>Facility Registration</h1>
      <form method="post" action="/hospitals" class="stack-form validate-form">
        <label>Hospital Name <input type="text" name="name" required></label>
        <label>Zone
          <select name="zone" required>
            <option>North</option>
            <option>South</option>
            <option>East</option>
            <option>West</option>
          </select>
        </label>
        <label>Contact Person <input type="text" name="contactPerson" required></label>
        <label>Email <input type="email" name="email" required></label>
        <label>Phone <input type="text" name="phone" required></label>
        <label>Address <textarea name="address" rows="3" required></textarea></label>
        <button type="submit">Save Facility</button>
        <p class="message">${message}</p>
      </form>
    </section>

    <section class="card">
      <h2>Registered Facilities</h2>
      <p class="table-subtitle">Manage facility records by zone, contact person, and communication details.</p>
      <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Zone</th>
            <th>Contact</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${hospitals}" var="hospital">
            <tr>
              <td>${hospital.name}</td>
              <td>${hospital.zone}</td>
              <td>${hospital.contactPerson}</td>
              <td>${hospital.email}</td>
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
