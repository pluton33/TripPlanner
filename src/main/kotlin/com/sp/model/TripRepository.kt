package com.sp.model

import com.sp.model.data.StopPlace
import com.sp.model.data.Trip

interface TripRepository {
    suspend fun allTrips(userId: Int) : GetTripsResponse
    suspend fun tripByName(name: String) : Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun removeTrip(name: String): Boolean
    suspend fun addStop(tripId: Int, stopPlace: StopPlace)
}