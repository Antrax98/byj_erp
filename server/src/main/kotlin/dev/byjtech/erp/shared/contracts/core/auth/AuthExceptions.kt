package dev.byjtech.erp.shared.contracts.core.auth

// Excepción base para errores de autenticación/autorización
open class AuthenticationException(message: String = "Authentication failed") : RuntimeException(message)

// Excepción si la sesión no es encontrada o es inválida
class SessionNotFoundException(sessionId: String) : AuthenticationException("Session $sessionId not found or invalid")

// Excepción si la sesión ha expirado
class SessionExpiredException(sessionId: String) : AuthenticationException("Session $sessionId has expired")

// Excepción base para errores de autorización
open class AuthorizationException(message: String = "Authorization failed") : RuntimeException(message)

// Excepción si el usuario no tiene los permisos necesarios
class MissingPermissionsException(val missingPermissions: List<String>) : AuthorizationException("User is missing required permissions: ${missingPermissions.joinToString()}")

class MissingRoleException(): AuthorizationException("User has no role")

class MissingSuperAdminException(): AuthorizationException("User is not a super admin")

// Excepción si el usuario no tiene acceso al módulo solicitado
class ModuleAccessException(val module: String) : AuthorizationException("User does not have access to module: $module")

// Podrías tener otras excepciones específicas, ej., UserNotFoundForSessionException