package com.sp.model

interface TripRepository {
    suspend fun allTrips() : GetTripsResponse
    suspend fun tripByName(name: String) : Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun removeTrip(name: String): Boolean
}