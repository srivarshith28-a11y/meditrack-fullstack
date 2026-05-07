# MediTrack Full Stack Project

MediTrack is a full stack biomedical waste collection and route optimization website built around the technologies mentioned in your sessions:

- `HTML`, `CSS`, `JavaScript`
- `JSP`
- `JDBC` with Spring `JdbcTemplate`
- `MySQL`
- `Spring Boot`
- `React`

## Project Structure

- `backend/`
  Spring Boot application with JSP pages, REST APIs, JDBC repositories, and static assets
- `database/schema.sql`
  MySQL schema for hospitals, waste entries, and routes
- `database/sample-data.sql`
  Sample rows for quick testing

## Main Features

- Home page with full stack overview
- Hospital login page
- Hospital registration page
- Waste entry page
- Reports page
- React dashboard page at `/react/dashboard.html`
- REST APIs:
  - `POST /api/addWaste`
  - `POST /api/optimizeRoute`
  - `GET /api/getReport`
  - `GET /api/summary`

## Database Setup

1. Create the MySQL schema:

```sql
source database/schema.sql;
```

2. Insert sample data:

```sql
source database/sample-data.sql;
```

3. Configure database credentials using environment variables:

- `MEDITRAK_DB_URL`
- `MEDITRAK_DB_USERNAME`
- `MEDITRAK_DB_PASSWORD`

You can also edit `backend/src/main/resources/application.properties` if you prefer local file configuration.

## Run the Backend

Run this as a normal Spring Boot application with the embedded server.

From `backend/`:

```bash
mvn spring-boot:run
```

Example PowerShell setup:

```powershell
$env:MEDITRAK_DB_URL='jdbc:mysql://localhost:3306/meditrak'
$env:MEDITRAK_DB_USERNAME='meditrak_user'
$env:MEDITRAK_DB_PASSWORD='your_password'
mvn spring-boot:run
```

Then open:

- `http://localhost:8080/`
- `http://localhost:8080/react/dashboard.html`

## Demo Login

- Username: `admin`
- Password: `meditrak123`

## Notes

- JSP is used for the classic web pages from the session flow.
- React is used for the dashboard layer.
- JDBC concepts are implemented through Spring Boot's `JdbcTemplate`.
- The project is scaffolded to match the academic technologies you asked for in simple full stack form.
