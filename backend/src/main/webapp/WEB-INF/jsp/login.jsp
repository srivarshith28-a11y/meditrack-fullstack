<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MediTrack Login</title>
  <link rel="stylesheet" href="/css/site.css">
</head>
<body>
  <main class="form-page">
    <section class="login-shell">
      <article class="card login-side">
        <p class="eyebrow">Secure Operations</p>
        <h1>Access the command center.</h1>
        <p>Enter the MediTrack portal to review route readiness, urgent pickups, facility activity, and operational reporting.</p>
        <div class="flow-grid">
          <div><strong>Live route review</strong><span>Monitor route score, zone allocation, and dispatch coverage from one dashboard.</span></div>
          <div><strong>Priority pickup focus</strong><span>Keep urgent and time-sensitive waste entries visible before dispatch decisions are made.</span></div>
          <div><strong>Clear operational records</strong><span>Move from hospital registration to waste entry and reporting without losing context.</span></div>
        </div>
        <div class="login-meta">
          <span>Protected access</span>
          <span>Role-ready workflow</span>
          <span>Presentation-ready dashboard</span>
        </div>
      </article>
      <section class="card form-card login-card">
        <p class="eyebrow">Secure Access</p>
        <h1>Portal Login</h1>
        <p class="muted">Sign in to access facility operations, waste records, route reports, and dashboard visibility.</p>
        <form method="post" action="/login" class="stack-form">
          <label>Username <input type="text" name="username" required></label>
          <label>Password <input type="password" name="password" required></label>
          <button type="submit">Sign In</button>
        </form>
        <p class="message">${message}</p>
        <div class="page-actions">
          <a class="pill-link" href="/">Back to home</a>
        </div>
      </section>
    </section>
  </main>
</body>
</html>
