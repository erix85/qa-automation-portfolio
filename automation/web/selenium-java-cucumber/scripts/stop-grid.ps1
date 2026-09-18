$processes = Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object { $_.CommandLine -like "*selenium-server*" }
if ($processes) {
    $processes | Stop-Process -Force
    Write-Host "✅ Selenium Grid detenido." -ForegroundColor Green
} else {
    Write-Host "ℹ️ No se encontró ningún proceso de Selenium Grid." -ForegroundColor Yellow
}