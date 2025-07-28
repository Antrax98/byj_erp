package dev.byjtech.erp.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * Utilidades para manejo de fechas
 */
object DateUtils {
    
    /**
     * Obtiene la fecha actual del sistema
     */
    fun today(): LocalDate {
        return Clock.System.todayIn(TimeZone.currentSystemDefault())
    }
}
