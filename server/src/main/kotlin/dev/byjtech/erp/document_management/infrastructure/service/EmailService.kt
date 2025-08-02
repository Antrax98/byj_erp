package dev.byjtech.erp.document_management.infrastructure.service

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.core.domain.model.User
import org.simplejavamail.api.email.Email
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

class EmailService {
    
    private val emailConfig = EmailConfig()
    
    private val mailer: Mailer by lazy {
        if (emailConfig.enabled) {
            MailerBuilder
                .withSMTPServer(emailConfig.smtpHost, emailConfig.smtpPort, emailConfig.username, emailConfig.password)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer()
        } else {
            throw IllegalStateException("Email service is disabled")
        }
    }
    
    fun sendDocumentExpirationNotification(
        user: User,
        document: Document,
        daysUntilExpiration: Int
    ) {
        if (!emailConfig.enabled) {
            return
        }
        
        try {
            val email = createDocumentExpirationEmail(user, document, daysUntilExpiration)
            mailer.sendMail(email)
        } catch (e: Exception) {
            // Log error silently - production behavior
            e.printStackTrace()
        }
    }
    
    private fun createDocumentExpirationEmail(
        user: User,
        document: Document,
        daysUntilExpiration: Int
    ): Email {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dueDate = document.dueDate?.toJavaLocalDate()?.format(dateFormatter) ?: "No definida"
        
        val subject = when {
            daysUntilExpiration <= 0 -> "⚠️ URGENTE: Documento vencido - ${document.documentNumber}"
            daysUntilExpiration == 1 -> "⚠️ ALERTA: Documento vence mañana - ${document.documentNumber}"
            else -> "📋 RECORDATORIO: Documento vence en $daysUntilExpiration días - ${document.documentNumber}"
        }
        
        val htmlBody = createEmailTemplate(user, document, daysUntilExpiration, dueDate)
        
        return EmailBuilder.startingBlank()
            .from(emailConfig.fromName, emailConfig.fromEmail)
            .to(user.name, user.email)
            .withSubject(subject)
            .withHTMLText(htmlBody)
            .buildEmail()
    }
    
    private fun createEmailTemplate(
        user: User,
        document: Document,
        daysUntilExpiration: Int,
        dueDate: String
    ): String {
        val urgencyClass = when {
            daysUntilExpiration <= 0 -> "urgent"
            daysUntilExpiration <= 3 -> "warning"
            else -> "info"
        }
        
        val statusMessage = when {
            daysUntilExpiration <= 0 -> "Este documento ya está vencido"
            daysUntilExpiration == 1 -> "Este documento vence mañana"
            else -> "Este documento vence en $daysUntilExpiration días"
        }
        
        val urgencyIcon = when {
            daysUntilExpiration <= 0 -> "🚨"
            daysUntilExpiration <= 3 -> "⚠️"
            else -> "📋"
        }
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #2563eb; color: white; padding: 25px; border-radius: 8px 8px 0 0; text-align: center; }
                    .content { background: #f8fafc; padding: 25px; border-radius: 0 0 8px 8px; }
                    .document-info { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    .urgent { border-left: 5px solid #dc2626; }
                    .warning { border-left: 5px solid #ea580c; }
                    .info { border-left: 5px solid #2563eb; }
                    .footer { text-align: center; margin-top: 30px; color: #6b7280; font-size: 14px; }
                    .status-badge {
                        display: inline-block;
                        padding: 8px 16px;
                        border-radius: 20px;
                        font-weight: bold;
                        font-size: 14px;
                    }
                    .status-urgent { background: #fef2f2; color: #dc2626; }
                    .status-warning { background: #fff7ed; color: #ea580c; }
                    .status-info { background: #eff6ff; color: #2563eb; }
                    .detail-row { display: flex; justify-content: space-between; margin: 10px 0; }
                    .detail-label { font-weight: bold; color: #4b5563; }
                    .detail-value { color: #1f2937; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>$urgencyIcon Sistema ERP BYJ</h1>
                        <h2>Notificación de Documento</h2>
                    </div>
                    <div class="content">
                        <p style="font-size: 18px;">Hola <strong>${user.name}</strong>,</p>
                        
                        <div class="status-badge status-$urgencyClass">
                            ${statusMessage.uppercase()}
                        </div>
                        
                        <div class="document-info $urgencyClass">
                            <h3 style="margin-top: 0;">📄 Información del Documento</h3>
                            
                            <div class="detail-row">
                                <span class="detail-label">Número de Documento:</span>
                                <span class="detail-value">${document.documentNumber}</span>
                            </div>
                            
                            <div class="detail-row">
                                <span class="detail-label">Tipo:</span>
                                <span class="detail-value">${document.type}</span>
                            </div>
                            
                            <div class="detail-row">
                                <span class="detail-label">Fecha de Vencimiento:</span>
                                <span class="detail-value">${dueDate}</span>
                            </div>
                            
                            <div class="detail-row">
                                <span class="detail-label">Monto Total:</span>
                                <span class="detail-value">$${String.format("%,.2f", document.totalAmount)} ${document.currency}</span>
                            </div>
                            
                            <div class="detail-row">
                                <span class="detail-label">Estado:</span>
                                <span class="detail-value">${document.status}</span>
                            </div>
                        </div>
                        
                        <p style="font-size: 16px;">
                            ${if (daysUntilExpiration <= 0) 
                                "⚠️ <strong>ACCIÓN REQUERIDA:</strong> Este documento ya está vencido. Te recomendamos gestionarlo inmediatamente."
                            else 
                                "Te recomendamos revisar este documento a la brevedad para evitar problemas de vencimiento."
                            }
                        </p>
                        
                        <p style="color: #6b7280; font-size: 14px;">
                            Si ya has gestionado este documento, puedes ignorar este mensaje.
                        </p>
                    </div>
                    <div class="footer">
                        <p><strong>Sistema ERP BYJ</strong></p>
                        <p>Este es un mensaje automático. No responder a este correo.</p>
                        <p style="font-size: 12px;">Enviado el ${java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}

data class EmailConfig(
    val smtpHost: String = "smtp.gmail.com",
    val smtpPort: Int = 587,
    val username: String = System.getProperty("EMAIL_USERNAME") ?: System.getenv("EMAIL_USERNAME") ?: "cuentamiercoles21@gmail.com",
    val password: String = System.getProperty("EMAIL_PASSWORD") ?: System.getenv("EMAIL_PASSWORD") ?: "vtke bbjf ufzy vgmz",
    val fromEmail: String = System.getProperty("FROM_EMAIL") ?: System.getenv("FROM_EMAIL") ?: "cuentamiercoles21@gmail.com",
    val fromName: String = "Sistema ERP BYJ",
    val enabled: Boolean = (System.getProperty("EMAIL_ENABLED") ?: System.getenv("EMAIL_ENABLED"))?.toBoolean() ?: true
)
