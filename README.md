# TeamTask — Team Task Tracking & Management Backend

Spring Boot REST API for user authentication, teams, projects, tasks, comments and file attachments.

## Stack
- Java 17+ (Java 21 is also fine)
- Spring Boot 3.4.5, Maven
- PostgreSQL 16 (Docker Compose)
- Spring Data JPA / Hibernate
- Spring Security + JWT, BCrypt

## Important: isolated PostgreSQL port
This project maps Docker PostgreSQL **host port 5433** to container port **5432**. The application connects to `127.0.0.1:5433` intentionally. This avoids accidentally connecting to a separate PostgreSQL installation already running on Windows at port 5432.

## Start on Windows PowerShell
1. Install Docker Desktop and Maven; ensure Docker Desktop is running.
2. Open PowerShell in this folder (`teamtask-manager`).
3. Start the database and verify its credentials:

   ```powershell
   docker compose up -d
   docker exec -it teamtask-postgres psql -U teamtask -d teamtask -c "SELECT current_database(), current_user;"
   ```

   Expected: database `teamtask`, user `teamtask`.
4. Start the API:

   ```powershell
   mvn clean package
   mvn spring-boot:run
   ```

   Or use `.\start-fresh.ps1` to start PostgreSQL, wait for readiness, verify login, and launch Spring Boot.
5. API base URL: `http://localhost:8080`.

## Database reset (destructive)
If you intentionally want a completely fresh database, run `.\reset-database.ps1` and type `DELETE` when prompted. This removes the Compose volume and permanently deletes its database contents. Do not run it if you need existing data.

## API overview
- `POST /api/auth/register` — register
- `POST /api/auth/login` — login and receive JWT
- `/api/teams` — create/list teams and manage members
- `/api/projects` — create/list projects
- `/api/tasks` — create, list, update and filter tasks
- `/api/tasks/{taskId}/comments` — task comments
- `/api/tasks/{taskId}/attachments` — upload/list attachments

Most endpoints other than `/api/auth/**` require `Authorization: Bearer <token>`.

## Notes
- Hibernate `ddl-auto=update` creates/updates tables during local development.
- Uploaded files are stored under `uploads/` locally.
- Replace the development JWT secret before any production deployment.
- This package is a development starter. Run the build and API smoke tests in your environment before submitting; do not represent it as production-audited.


## Project Status

TeamTask backend development is in progress.