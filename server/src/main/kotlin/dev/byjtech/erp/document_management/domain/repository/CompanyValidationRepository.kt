package dev.byjtech.erp.document_management.domain.repository

import java.util.UUID

/**
 * Repository para validar la existencia de companies desde el módulo document_management
 * Accede a la base de datos core para verificar referencias cruzadas
 */
interface CompanyValidationRepository {
    /**
     * Verifica si existe una compañía con el ID dado
     * @param companyId UUID de la compañía a validar
     * @return true si la compañía existe, false en caso contrario
     */
    fun existsById(companyId: UUID): Boolean
    
    /**
     * Verifica si existe un usuario con el ID dado
     * @param userId UUID del usuario a validar
     * @return true si el usuario existe, false en caso contrario
     */
    fun userExistsById(userId: UUID): Boolean
}
