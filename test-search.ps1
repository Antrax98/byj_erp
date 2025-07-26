# Script de prueba para el sistema de búsqueda de documentos
# Primero generar token para anaysmr21@gmail.com (usuario que tiene empresa)

Write-Host "=== Pruebas del Sistema de Búsqueda de Documentos ===" -ForegroundColor Green

# 1. Generar token para usuario con empresa
Write-Host "`n1. Generando token para anaysmr21@gmail.com..." -ForegroundColor Yellow
try {
    $tokenResponse = Invoke-RestMethod -Uri "http://localhost:8090/auth/generate-admin-token" -Method GET
    $token = $tokenResponse
    Write-Host "Token generado: $token" -ForegroundColor Green
} catch {
    Write-Host "Error generando token: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# 2. Headers para las siguientes peticiones
$headers = @{
    'Content-Type' = 'application/json'
    'Authentication' = "Bearer $token"
}

# 3. Probar búsqueda básica (sin filtros)
Write-Host "`n2. Probando búsqueda básica..." -ForegroundColor Yellow
$basicSearchBody = @{
    page = 1
    page_size = 10
    sort_by = "created_at"
    sort_direction = "desc"
} | ConvertTo-Json

try {
    $basicSearchResponse = Invoke-RestMethod -Uri "http://localhost:8090/api/document_management/documents/search" -Method POST -Headers $headers -Body $basicSearchBody
    Write-Host "Búsqueda básica exitosa:" -ForegroundColor Green
    Write-Host "Total documentos: $($basicSearchResponse.total_count)" -ForegroundColor Cyan
    Write-Host "Documentos en página: $($basicSearchResponse.documents.Count)" -ForegroundColor Cyan
    Write-Host "Página: $($basicSearchResponse.page) de $($basicSearchResponse.total_pages)" -ForegroundColor Cyan
} catch {
    Write-Host "Error en búsqueda básica: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. Probar búsqueda con filtros de texto
Write-Host "`n3. Probando búsqueda con texto..." -ForegroundColor Yellow
$textSearchBody = @{
    search_text = "factura"
    page = 1
    page_size = 10
    sort_by = "created_at"
    sort_direction = "desc"
} | ConvertTo-Json

try {
    $textSearchResponse = Invoke-RestMethod -Uri "http://localhost:8090/api/document_management/documents/search" -Method POST -Headers $headers -Body $textSearchBody
    Write-Host "Búsqueda con texto exitosa:" -ForegroundColor Green
    Write-Host "Total documentos encontrados: $($textSearchResponse.total_count)" -ForegroundColor Cyan
} catch {
    Write-Host "Error en búsqueda con texto: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. Probar búsqueda con filtros específicos
Write-Host "`n4. Probando búsqueda con filtros específicos..." -ForegroundColor Yellow
$filterSearchBody = @{
    document_type = "FACTURA"
    status = "UPLOADED"
    currency = "CLP"
    min_amount = 1000
    max_amount = 100000
    page = 1
    page_size = 5
    sort_by = "total_amount"
    sort_direction = "desc"
} | ConvertTo-Json

try {
    $filterSearchResponse = Invoke-RestMethod -Uri "http://localhost:8090/api/document_management/documents/search" -Method POST -Headers $headers -Body $filterSearchBody
    Write-Host "Búsqueda con filtros exitosa:" -ForegroundColor Green
    Write-Host "Total documentos filtrados: $($filterSearchResponse.total_count)" -ForegroundColor Cyan
    if ($filterSearchResponse.documents.Count -gt 0) {
        Write-Host "Primer documento encontrado:" -ForegroundColor Cyan
        $firstDoc = $filterSearchResponse.documents[0]
        Write-Host "  - Tipo: $($firstDoc.documentType)" -ForegroundColor White
        Write-Host "  - Número: $($firstDoc.documentNumber)" -ForegroundColor White
        Write-Host "  - Estado: $($firstDoc.status)" -ForegroundColor White
        Write-Host "  - Total: $($firstDoc.totalAmount) $($firstDoc.currency)" -ForegroundColor White
    }
} catch {
    Write-Host "Error en búsqueda con filtros: $($_.Exception.Message)" -ForegroundColor Red
}

# 6. Probar paginación
Write-Host "`n5. Probando paginación (página 2)..." -ForegroundColor Yellow
$paginationSearchBody = @{
    page = 2
    page_size = 3
    sort_by = "created_at"
    sort_direction = "asc"
} | ConvertTo-Json

try {
    $paginationResponse = Invoke-RestMethod -Uri "http://localhost:8090/api/document_management/documents/search" -Method POST -Headers $headers -Body $paginationSearchBody
    Write-Host "Paginación exitosa:" -ForegroundColor Green
    Write-Host "Página actual: $($paginationResponse.page)" -ForegroundColor Cyan
    Write-Host "Páginas totales: $($paginationResponse.total_pages)" -ForegroundColor Cyan
    Write-Host "Tiene página anterior: $($paginationResponse.has_previous_page)" -ForegroundColor Cyan
    Write-Host "Tiene página siguiente: $($paginationResponse.has_next_page)" -ForegroundColor Cyan
} catch {
    Write-Host "Error en paginación: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Pruebas completadas ===" -ForegroundColor Green
