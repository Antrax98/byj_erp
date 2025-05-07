package dev.byjtech.erp.core.database.categories

import dev.byjtech.erp.core.dto.category.CategoryDTO
import dev.byjtech.erp.utils.datetime.toKotlinx

// Extensión para convertir CategoryEntity a CategoryDTO
fun dev.byjtech.erp.core.database.categories.CategoryEntity.toDTO(): dev.byjtech.erp.core.dto.category.CategoryDTO {
    return dev.byjtech.erp.core.dto.category.CategoryDTO(
        id = this.id.value,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt.toKotlinx(), // Asegúrate de que 'createdAt' sea del tipo adecuado (LocalDateTime)
        updatedAt = this.updatedAt.toKotlinx(), // Asegúrate de que 'updatedAt' sea del tipo adecuado (LocalDateTime)
        moduleId = this.module.id.value // Si 'module' es nullable, maneja el valor como nullable también
    )
}