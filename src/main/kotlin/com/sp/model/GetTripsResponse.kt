package com.sp.model

import kotlinx.serialization.Serializable

@Serializable
data class GetTripsResponse (
    var trips: List<Trip>
)