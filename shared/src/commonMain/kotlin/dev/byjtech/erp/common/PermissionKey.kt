package dev.byjtech.erp.common
import kotlinx.serialization.Serializable

@Serializable
data class PermissionKey(
    val module: String,
    val category: String,
    val action: String
) {
    override fun toString(): String = "$module:$category:$action"
}