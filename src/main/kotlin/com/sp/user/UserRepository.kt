package com.sp.user

import com.sp.user.User

interface UserRepository {
    suspend fun register(user: User): User
    suspend fun findById(id: Int): User?
}