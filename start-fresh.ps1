$ErrorActionPreference = 'Stop'
Write-Host 'Starting TeamTask PostgreSQL...' -ForegroundColor Cyan
docker compose up -d
if ($LASTEXITCODE -ne 0) { throw 'docker compose up failed' }
$ready = $false
for ($i = 1; $i -le 30; $i++) {
    docker exec teamtask-postgres pg_isready -U teamtask -d teamtask *> $null
    if ($LASTEXITCODE -eq 0) { $ready = $true; break }
    Start-Sleep -Seconds 2
}
if (-not $ready) { throw 'PostgreSQL did not become ready. Check: docker compose logs postgres' }
docker exec teamtask-postgres psql -U teamtask -d teamtask -c 'SELECT current_database(), current_user;'
if ($LASTEXITCODE -ne 0) { throw 'Database login verification failed' }
Write-Host 'Database is ready. Starting Spring Boot...' -ForegroundColor Green
mvn spring-boot:run
