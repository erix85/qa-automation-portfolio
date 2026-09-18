#requires -Version 5.0

<#
.SYNOPSIS
    Extrae informacion del proyecto Java + Selenium + Cucumber para el portafolio

.DESCRIPTION
    Genera un reporte completo en Markdown con la estructura, archivos,
    dependencias y estadisticas del proyecto de automatizacion QA

.PARAMETER OutputPath
    Ruta donde se guardara el reporte generado (por defecto: project-info.md)

.PARAMETER DryRun
    Muestra la informacion en consola sin generar el archivo de salida

.EXAMPLE
    .\Extract-ProjectInfo.ps1 -OutputPath "reporte.md"

.EXAMPLE
    .\Extract-ProjectInfo.ps1 -DryRun
#>

[CmdletBinding()]
param(
    [string]$OutputPath = "project-info.md",
    [switch]$DryRun
)

# ============================================
# CONFIGURACION INICIAL
# ============================================

$ErrorActionPreference = 'Stop'
$ProjectRoot = (Resolve-Path -LiteralPath .).ProviderPath

# Colores para la consola
$ColorInfo = "Cyan"
$ColorSuccess = "Green"
$ColorWarning = "Yellow"
$ColorError = "Red"

# ============================================
# FUNCIONES DE UTILIDAD
# ============================================

function Write-Info {
    param([string]$Message)
    Write-Host $Message -ForegroundColor $ColorInfo
}

function Write-Success {
    param([string]$Message)
    Write-Host $Message -ForegroundColor $ColorSuccess
}

function Write-WarningMsg {
    param([string]$Message)
    Write-Host $Message -ForegroundColor $ColorWarning
}

function Write-Header {
    param([string]$Title)
    Write-Host ""
    Write-Host "========================================" -ForegroundColor $ColorInfo
    Write-Host "  $Title" -ForegroundColor $ColorInfo
    Write-Host "========================================" -ForegroundColor $ColorInfo
}

function Get-FileSize {
    param([long]$Bytes)
    if ($Bytes -gt 1MB) {
        $result = "$([math]::Round($Bytes / 1MB, 2)) MB"
    } elseif ($Bytes -gt 1KB) {
        $result = "$([math]::Round($Bytes / 1KB, 2)) KB"
    } else {
        $result = "$Bytes B"
    }
    return $result
}

function Read-FileContent {
    param([string]$FilePath)
    if (Test-Path -LiteralPath $FilePath) {
        try {
            $resolvedPath = (Resolve-Path -LiteralPath $FilePath).ProviderPath
            return [System.IO.File]::ReadAllText($resolvedPath)
        } catch {
            return ""
        }
    }
    return ""
}

function Get-PackageName {
    param([string]$FilePath)
    $content = Read-FileContent -FilePath $FilePath
    $result = ""
    if ($content -match 'package\s+([^;]+);') {
        $result = $matches[1]
    }
    return $result
}

# ============================================
# FUNCIONES DE EXTRACCION
# ============================================

function Get-ProjectInfo {
    Write-Header "INFORMACION DEL PROYECTO"
    
    $projectName = (Get-Item -LiteralPath $ProjectRoot).Name
    Write-Host "  Nombre: $projectName"
    Write-Host "  Fecha: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
    Write-Host "  PowerShell Version: $($PSVersionTable.PSVersion)"
    Write-Host "  Ruta: $ProjectRoot"
}

function Get-FileStructure {
    Write-Header "ESTRUCTURA DE ARCHIVOS"
    
    $excludeDirs = @("target", ".idea", ".vscode", ".git", "logs", "node_modules")
    
    Write-Host "  Estructura principal:"
    
    $dirs = Get-ChildItem -Path $ProjectRoot -Directory | Where-Object { $_.Name -notin $excludeDirs }
    
    foreach ($dir in $dirs) {
        Write-Host "    [DIR] $($dir.Name)/"
        $files = Get-ChildItem -Path $dir.FullName -Recurse -File | Where-Object {
            $ext = $_.Extension
            ($ext -eq ".java") -or ($ext -eq ".xml") -or ($ext -eq ".feature") -or ($ext -eq ".properties") -or ($ext -eq ".md")
        }
        
        foreach ($file in $files) {
            $relativePath = $file.FullName.Substring($ProjectRoot.Length + 1)
            $size = Get-FileSize -Bytes $file.Length
            Write-Host "      [FILE] $relativePath ($size)"
        }
    }
}

function Get-JavaFiles {
    Write-Header "ARCHIVOS JAVA"
    
    $javaFiles = Get-ChildItem -Path $ProjectRoot -Recurse -Filter "*.java" | Where-Object {
        $_.FullName -notlike "*target*"
    }
    
    if ($javaFiles) {
        Write-Host "  Total: $($javaFiles.Count) archivos Java"
        Write-Host ""
        Write-Host "  Lista de clases:"
        
        foreach ($file in $javaFiles) {
            $className = [System.IO.Path]::GetFileNameWithoutExtension($file.Name)
            $size = Get-FileSize -Bytes $file.Length
            $package = Get-PackageName -FilePath $file.FullName
            
            Write-Host "    - $className ($size)"
            if ($package) {
                Write-Host "      Package: $package"
            }
        }
    } else {
        Write-WarningMsg "  No se encontraron archivos Java"
    }
}

function Get-FeatureStats {
    Write-Header "ESTADISTICAS DE PRUEBAS"
    
    $featureFiles = Get-ChildItem -Path $ProjectRoot -Recurse -Filter "*.feature" | Where-Object {
        $_.FullName -notlike "*target*"
    }
    
    if ($featureFiles) {
        Write-Host "  Features encontrados: $($featureFiles.Count)"
        Write-Host ""
        
        $totalScenarios = 0
        $totalSteps = 0
        
        foreach ($file in $featureFiles) {
            Write-Host "    Feature: $($file.Name)"
            $content = Read-FileContent -FilePath $file.FullName
            
            $scenarioPattern = [regex]::new("(?m)^\s*(Scenario|Scenario Outline)")
            $stepPattern = [regex]::new("(?m)^\s*(Given|When|Then|And|But)")
            
            $scenarios = $scenarioPattern.Matches($content)
            $steps = $stepPattern.Matches($content)
            
            $scenarioCount = $scenarios.Count
            $stepCount = $steps.Count
            
            $totalScenarios = $totalScenarios + $scenarioCount
            $totalSteps = $totalSteps + $stepCount
            
            Write-Host "      Scenarios: $scenarioCount"
            Write-Host "      Steps: $stepCount"
        }
        
        Write-Host ""
        Write-Host "  TOTAL: $totalScenarios scenarios, $totalSteps steps" -ForegroundColor $ColorSuccess
    } else {
        Write-WarningMsg "  No se encontraron archivos .feature"
    }
}

function Get-Dependencies {
    Write-Header "DEPENDENCIAS"
    
    $pomPath = Join-Path -Path $ProjectRoot -ChildPath "pom.xml"
    if (Test-Path -LiteralPath $pomPath) {
        $content = Read-FileContent -FilePath $pomPath
        
        Write-Host "  Dependencias principales:"
        Write-Host ""
        
        $depPattern = [regex]::new("<dependency>(.*?)</dependency>", [System.Text.RegularExpressions.RegexOptions]::Singleline)
        $deps = $depPattern.Matches($content)
        
        $groupIdPattern = [regex]::new("<groupId>(.*?)</groupId>")
        $artifactIdPattern = [regex]::new("<artifactId>(.*?)</artifactId>")
        $versionPattern = [regex]::new("<version>(.*?)</version>")
        
        foreach ($dep in $deps) {
            $depContent = $dep.Groups[1].Value
            
            $groupIdMatch = $groupIdPattern.Match($depContent)
            $artifactIdMatch = $artifactIdPattern.Match($depContent)
            $versionMatch = $versionPattern.Match($depContent)
            
            $groupId = ""
            $artifactId = ""
            $version = ""
            
            if ($groupIdMatch.Success) { $groupId = $groupIdMatch.Groups[1].Value }
            if ($artifactIdMatch.Success) { $artifactId = $artifactIdMatch.Groups[1].Value }
            if ($versionMatch.Success) { $version = $versionMatch.Groups[1].Value }
            
            if ($groupId -and $artifactId -and ($groupId -notlike "*portafolio*")) {
                $depText = "- $groupId : $artifactId ($version)"
                Write-Host "    $depText"
            }
        }
    } else {
        Write-WarningMsg "  No se encontro pom.xml"
    }
}

function Export-ToMarkdown {
    param([string]$Path)
    
    Write-Info "`nGenerando reporte en: $Path"
    
    $javaFiles = Get-ChildItem -Path $ProjectRoot -Recurse -Filter "*.java" | Where-Object {
        $_.FullName -notlike "*target*"
    }
    
    $featureFiles = Get-ChildItem -Path $ProjectRoot -Recurse -Filter "*.feature" | Where-Object {
        $_.FullName -notlike "*target*"
    }
    
    $totalSize = 0
    $allFiles = Get-ChildItem -Path $ProjectRoot -Recurse -File
    foreach ($file in $allFiles) {
        $totalSize = $totalSize + $file.Length
    }
    
    # Construir contenido de forma segura
    $lines = @()
    $lines += "# QA Automation Project Report"
    $lines += ""
    $lines += "**Generated:** $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
    $lines += "**Project:** $((Get-Item -LiteralPath $ProjectRoot).Name)"
    $lines += "**PowerShell:** $($PSVersionTable.PSVersion)"
    $lines += ""
    $lines += "---"
    $lines += ""
    $lines += "## Java Files ($($javaFiles.Count) total)"
    $lines += ""
    
    foreach ($file in $javaFiles) {
        $className = [System.IO.Path]::GetFileNameWithoutExtension($file.Name)
        $size = Get-FileSize -Bytes $file.Length
        $lines += "- $className ($size)"
    }
    
    $lines += ""
    $lines += "---"
    $lines += ""
    $lines += "## Feature Files ($($featureFiles.Count) total)"
    $lines += ""
    
    foreach ($file in $featureFiles) {
        $content = Read-FileContent -FilePath $file.FullName
        $scenarioPattern = [regex]::new("(?m)^\s*(Scenario|Scenario Outline)")
        $stepPattern = [regex]::new("(?m)^\s*(Given|When|Then|And|But)")
        $scenarios = $scenarioPattern.Matches($content)
        $steps = $stepPattern.Matches($content)
        $lines += "- $($file.Name): $($scenarios.Count) scenarios, $($steps.Count) steps"
    }
    
    $lines += ""
    $lines += "---"
    $lines += ""
    $lines += "## Dependencies"
    $lines += ""
    
    $pomPath = Join-Path -Path $ProjectRoot -ChildPath "pom.xml"
    if (Test-Path -LiteralPath $pomPath) {
        $content = Read-FileContent -FilePath $pomPath
        $depPattern = [regex]::new("<dependency>(.*?)</dependency>", [System.Text.RegularExpressions.RegexOptions]::Singleline)
        $deps = $depPattern.Matches($content)
        
        $groupIdPattern = [regex]::new("<groupId>(.*?)</groupId>")
        $artifactIdPattern = [regex]::new("<artifactId>(.*?)</artifactId>")
        $versionPattern = [regex]::new("<version>(.*?)</version>")
        
        $foundDeps = $false
        foreach ($dep in $deps) {
            $depContent = $dep.Groups[1].Value
            $groupIdMatch = $groupIdPattern.Match($depContent)
            $artifactIdMatch = $artifactIdPattern.Match($depContent)
            $versionMatch = $versionPattern.Match($depContent)
            
            $groupId = ""
            $artifactId = ""
            $version = ""
            
            if ($groupIdMatch.Success) { $groupId = $groupIdMatch.Groups[1].Value }
            if ($artifactIdMatch.Success) { $artifactId = $artifactIdMatch.Groups[1].Value }
            if ($versionMatch.Success) { $version = $versionMatch.Groups[1].Value }
            
            if ($groupId -and $artifactId -and ($groupId -notlike "*portafolio*")) {
                $lines += "- $groupId : $artifactId ($version)"
                $foundDeps = $true
            }
        }
        if (-not $foundDeps) {
            $lines += "No dependencies found"
        }
    } else {
        $lines += "No pom.xml found"
    }
    
    $lines += ""
    $lines += "---"
    $lines += ""
    $lines += "## Summary"
    $lines += ""
    $lines += "| Metric | Value |"
    $lines += "|--------|-------|"
    $lines += "| Java Files | $($javaFiles.Count) |"
    $lines += "| Feature Files | $($featureFiles.Count) |"
    $lines += "| Total Size | $(Get-FileSize -Bytes $totalSize) |"
    $lines += ""
    $lines += "---"
    $lines += ""
    $lines += "*Report generated by QA Project Info Extractor*"
    
    $content = $lines -join "`n"
    
    try {
        [System.IO.File]::WriteAllText($Path, $content, [System.Text.Encoding]::UTF8)
        Write-Success "  Reporte generado exitosamente"
        Write-Host "  Ruta: $Path"
    } catch {
        Write-Error "  Error al guardar el archivo: $_"
        throw
    }
}

# ============================================
# EJECUCION PRINCIPAL
# ============================================

try {
    Write-Header "QA AUTOMATION PROJECT INFO EXTRACTOR"
    
    $pomPath = Join-Path -Path $ProjectRoot -ChildPath "pom.xml"
    if (-not (Test-Path -LiteralPath $pomPath)) {
        Write-WarningMsg "  No se encontro pom.xml. Verifica que estas en el directorio correcto."
        Write-Host "  Directorio actual: $ProjectRoot"
        exit 1
    }
    
    Get-ProjectInfo
    Get-FileStructure
    Get-JavaFiles
    Get-FeatureStats
    Get-Dependencies
    
    if (-not $DryRun) {
        Export-ToMarkdown -Path $OutputPath
    } else {
        Write-Info "`n[MODO DRY RUN] No se genero archivo de salida"
    }
    
    Write-Header "PROCESO COMPLETADO EXITOSAMENTE"
    exit 0
    
} catch {
    Write-Host ""
    Write-Host "ERROR: $_" -ForegroundColor $ColorError
    Write-Host "Linea: $($_.InvocationInfo.ScriptLineNumber)" -ForegroundColor $ColorError
    exit 1
}