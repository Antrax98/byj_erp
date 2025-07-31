package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.repository.CompanyValidationRepository
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Scheduler que ejecuta la verificación de documentos próximos a vencer diariamente
 */
class DocumentExpirationScheduler(
    private val notificationService: NotificationService,
    private val companyValidationRepository: CompanyValidationRepository
) {
    
    private val schedulerExecutor = Executors.newScheduledThreadPool(1)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    // Configuración de intervalos (cambiar isTestingMode para alternar)
    private val isTestingMode = true // Cambiar a false para producción
    private val testingIntervalMinutes = 1L // Cambiado a 1 minuto
    private val productionIntervalHours = 24L
    
    /**
     * Inicia el scheduler con configuración automática según modo
     */
    fun start() {
        // Ejecutar inmediatamente al iniciar
        runExpirationCheck()
        
        if (isTestingMode) {
            // Modo testing: cada 1 minuto
            schedulerExecutor.scheduleAtFixedRate(
                ::runExpirationCheck,
                testingIntervalMinutes,
                testingIntervalMinutes,
                TimeUnit.MINUTES
            )
        } else {
            // Modo producción: cada 24 horas
            schedulerExecutor.scheduleAtFixedRate(
                ::runExpirationCheck,
                productionIntervalHours,
                productionIntervalHours,
                TimeUnit.HOURS
            )
        }
    }
    
    /**
     * Detiene el scheduler
     */
    fun stop() {
        scope.cancel()
        schedulerExecutor.shutdown()
    }
    
    /**
     * Ejecuta la verificación de documentos próximos a vencer para todas las compañías
     */
    private fun runExpirationCheck() {
        scope.launch {
            try {
                val timestamp = java.time.LocalDateTime.now()
                
                // Obtener todas las compañías activas
                val companies = companyValidationRepository.findAllActiveCompanies()
                
                for (company in companies) {
                    try {
                        notificationService.checkExpiringDocuments(company.id)
                    } catch (e: Exception) {
                        // Log error silently
                    }
                }
                
            } catch (e: Exception) {
                // Log error silently
            }
        }
    }
    
    /**
     * Ejecuta manualmente la verificación (útil para testing)
     */
    fun runManualCheck() {
        runExpirationCheck()
    }
}
