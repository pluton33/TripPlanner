package com.sp.model.data

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val userId: Int? = null,
    val username: String? = null,
    val login: String? = null,
    val password: String? = null,
    val trips: List<Trip> = emptyList(),
)