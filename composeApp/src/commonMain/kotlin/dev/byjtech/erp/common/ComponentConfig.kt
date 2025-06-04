package dev.byjtech.erp.common

import kotlinx.serialization.Serializable

@Serializable
data class ComponentConfig(
    val module: String, //nombre del modulo al que pertenece
    val feature: String //nombre del componente/feature
)
