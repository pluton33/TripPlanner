package com.sp.model

import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val name: String,
    val description: String
)