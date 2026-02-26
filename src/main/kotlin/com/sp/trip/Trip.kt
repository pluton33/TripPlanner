package com.sp.trip

import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: Int? = null,
    val name: String,
    val description: String,
    val stopPlaces: List<StopPlace> = emptyList()
)