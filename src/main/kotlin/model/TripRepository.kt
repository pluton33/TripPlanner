package com.sp.model

object TripsRepository {
    val trips = mutableListOf(
        Trip("zakopane", "rysy"),
        Trip("Nowy jork", "World trade center")
    )

    fun allTrips(): List<Trip> = trips

    fun tripByName(name: String) =trips.find {
        it.name.equals(name, ignoreCase = true)
    }

    fun addTrip(trip: Trip) {
        if(tripByName(trip.name) != null) {
            throw IllegalStateException("Cannot duplicate trip names!")
        }
        trips.add(trip)
    }

    fun removeTrip(name: String): Boolean {
        return trips.removeIf { it.name  == name }
    }
}