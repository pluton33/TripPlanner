package com.sp.user

import com.sp.db.TripDAO
import com.sp.db.UserDAO
import com.sp.db.UserDAOToModel
import com.sp.db.UserTable
import com.sp.db.suspendTransaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere

class PostgresUserRepository : UserRepository {
    override suspend fun register(user: User): Int {
        return suspendTransaction {
            UserDAO.new {
                name = user.username
                login = user.login
                password = user.password
            }.id.value
        }
    }

    override suspend fun deleteById(id: Int): Int {
        return suspendTransaction {
            UserTable.deleteWhere { UserTable.id eq id }
        }
    }

    override suspend fun findById(id: Int): User? {
        return suspendTransaction {
            UserDAO
                .findById(id)
                ?.let { UserDAOToModel(it) }
        }
    }
}