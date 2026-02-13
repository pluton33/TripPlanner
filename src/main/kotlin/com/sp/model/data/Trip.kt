package com.sp.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: Int,
    val name: String,
    val description: String,
    val stopPlaces: List<StopPlace>
)