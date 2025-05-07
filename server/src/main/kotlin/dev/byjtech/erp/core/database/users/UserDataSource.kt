package dev.byjtech.erp.core.database.users

import dev.byjtech.erp.config.UserInfo
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Clock
import java.time.LocalDateTime

//eliminar database, ya que no se usara
class UserDataSource(database: Database) {
    companion object {
        fun findUserById(userId: Int): UserEntity? {
            return transaction {
                UserEntity.findById(userId)
            }
        }
        //CREAR MAS FUNCIONES AQUI ABAJO


    }

}


//moverlas al datasource de users
fun findUserByEmail(email: String): UserEntity? {
    return transaction {
        UserEntity.find { Users.email eq email }.singleOrNull()
    }
}

fun updateUserFromGoogleInfo(userEntity: UserEntity, googleUserInfo: UserInfo) {
    transaction {
        val now: LocalDateTime = LocalDateTime.now()
        userEntity.apply {
            if (name == null) name = googleUserInfo.name
            if (googleId == null) googleId = googleUserInfo.sub
            if (pictureUrl == null) pictureUrl = googleUserInfo.picture
            lastLoginAt = now
            updatedAt = now
        }
    }
}