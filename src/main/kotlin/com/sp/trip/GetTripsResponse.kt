package com.sp.trip

import kotlinx.serialization.Serializable

@Serializable
data class GetTripsResponse (
    var trips: List<Trip>
)