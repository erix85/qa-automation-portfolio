# Inicia Selenium Grid Standalone en segundo plano
$jarPath = "C:\selenium-grid\selenium-server-4.49.0.jar" # <-- Ajusta esta ruta
if (-not (Test-Path $jarPath)) {
    Write-Host "❌ No se encontró el JAR en: $jarPath" -ForegroundColor Red
    Write-Host "Descarga 'selenium-server-4.49.0.jar' y actualiza la ruta en este script." -ForegroundColor Yellow
    exit 1
}

Write-Host "🚀 Iniciando Selenium Grid Standalone..." -ForegroundColor Cyan
# Inicia el proceso sin bloquear la terminal
Start-Process java -ArgumentList "-jar", $jarPath, "standalone", "--selenium-manager", "true" -WindowStyle Minimized

Write-Host "✅ Grid iniciado. Espera 10-15 segundos y verifica en: http://localhost:4444/ui" -ForegroundColor Green