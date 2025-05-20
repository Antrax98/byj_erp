package dev.byjtech.erp.shared.contracts.core.auth

//este class esta en el shared del proyecto completo, no en la carpeta core real
import dev.byjtech.erp.common.PermissionKey


//enviarlo a otro archivo compartido de ser necesario? (para juntar todos los dto y clases compartidas)
//¿contara como un modelo del domain?
//TODO: darle mas informacion de forma que cubra lo esencial
// lo mas probable es que sea todo lo relacionado con google, informacion basica del usuario y talvez diferencair si es un tenant o un SuperAdmin
data class ValidatedSessionInfo(
    val userId: Int,
    val sessionId: Int
    // no se cual usare por ahora, asi que lo dejare asi
    //agregar mas campos de ser necesarios
)

// Interfaz del servicio de autorización que otros módulos consumiran
interface AuthServiceContract {

    fun validateSessionAndAuthorize(
        authHeader: String?, //se obtiene del call y esta funcion valida que el token siquiera exista
        module: String, // obtenerlo de su propio class de inicialisacion (en el shared)
        requiredAnyPermissions: Set<PermissionKey>? = null, //asegurarse de no usar permissions de otros modulos (a excepcion de los de core)
        requiredSuperAdmin: Boolean = false
    ): ValidatedSessionInfo
}