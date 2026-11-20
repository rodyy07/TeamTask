$ErrorActionPreference = 'Stop'
Write-Host 'WARNING: This deletes the TeamTask PostgreSQL volume and all data in it.' -ForegroundColor Yellow
$confirm = Read-Host 'Type DELETE to continue'
if ($confirm -ne 'DELETE') { Write-Host 'Cancelled.'; exit 0 }
docker compose down -v
if ($LASTEXITCODE -ne 0) { throw 'docker compose down failed' }
docker compose up -d
if ($LASTEXITCODE -ne 0) { throw 'docker compose up failed' }
Write-Host 'Fresh PostgreSQL container created. Wait a few seconds, then run .\start-fresh.ps1' -ForegroundColor Green
