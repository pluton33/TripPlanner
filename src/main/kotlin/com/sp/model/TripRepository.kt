package com.sp.model

interface TripRepository {
    suspend fun allTrips() : List<Trip>
    suspend fun tripByName(name: String) : Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun removeTrip(name: String): Boolean
}