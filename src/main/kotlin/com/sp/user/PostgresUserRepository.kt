package com.sp.user

import com.sp.db.UserDAO
import com.sp.db.UserDAOToModel
import com.sp.db.suspendTransaction

class PostgresUserRepository : UserRepository {
    override suspend fun findById(id: Int): User? {
        return suspendTransaction {
            UserDAO
                .findById(id)
                ?.let { UserDAOToModel(it) }
        }
    }

    override suspend fun register(user: User): User {
        TODO("Not yet implemented")
    }
}