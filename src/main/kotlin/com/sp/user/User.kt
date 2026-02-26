package com.sp.user

import com.sp.trip.Trip
import kotlinx.serialization.Serializable

@Serializable
data class User (
    val userId: Int? = null,
    val username: String,
    val login: String,
    val password: String,
    val trips: List<Trip> = emptyList(),
)