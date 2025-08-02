package dev.byjtech.erp.document_management.application.service

import dev.byjtech.erp.document_management.domain.repository.CompanyValidationRepository
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class DocumentExpirationScheduler(
    private val notificationService: NotificationService,
    private val companyValidationRepository: CompanyValidationRepository
) {
    
    private val schedulerExecutor = Executors.newScheduledThreadPool(1)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    private val isTestingMode = true // Cambiar a false para producción
    private val testingIntervalMinutes = 1L // Cambiado a 1 minuto
    private val productionIntervalHours = 24L
    
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
            // Modo produccion: cada 24 horas
            schedulerExecutor.scheduleAtFixedRate(
                ::runExpirationCheck,
                productionIntervalHours,
                productionIntervalHours,
                TimeUnit.HOURS
            )
        }
    }

    fun stop() {
        scope.cancel()
        schedulerExecutor.shutdown()
    }
    

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
                    }
                }
                
            } catch (e: Exception) {
            }
        }
    }

    fun runManualCheck() {
        runExpirationCheck()
    }
}
