package com.sp.trip

interface TripRepository {
    suspend fun allTrips(userId: Int) : GetTripsResponse
    suspend fun tripByName(userId: Int, name: String) : Trip?
    suspend fun addTrip(userId: Int, trip: Trip)
    suspend fun removeTrip(userId: Int, tripId: Int): Boolean
    suspend fun addStop(userId: Int, tripId: Int, stopPlace: StopPlace)
    suspend fun removeStop(userId: Int, stopId: Int)
}