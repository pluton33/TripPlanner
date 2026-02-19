package com.sp.trip

import kotlinx.serialization.Serializable

@Serializable
data class StopPlace (
    val id: Int? = null,
    val name: String,
    val description: String?
)