package com.sp.user

import com.sp.user.User

interface UserRepository {
    suspend fun register(user: User): Int
    suspend fun deleteById(id: Int): Int
    suspend fun findById(id: Int): User?
}