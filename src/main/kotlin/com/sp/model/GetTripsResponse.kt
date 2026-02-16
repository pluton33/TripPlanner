package com.sp.model

import com.sp.model.data.Trip
import kotlinx.serialization.Serializable

@Serializable
data class GetTripsResponse (
    var trips: List<Trip>
)