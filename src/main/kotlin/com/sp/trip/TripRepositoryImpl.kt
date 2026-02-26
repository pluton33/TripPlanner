/*
package com.sp.model

class TripRepositoryImpl : TripRepository{
    val trips = mutableListOf(
        Trip("zakopane", "rysy"),
        Trip("Nowy jork", "World trade center")
    )

    override fun allTrips(): List<Trip> = trips

    override fun tripByName(name: String) =trips.find {
        it.name.equals(name, ignoreCase = true)
    }

    override fun addTrip(trip: Trip) {
        if(tripByName(trip.name) != null) {
            throw IllegalStateException("Cannot duplicate trip names!")
        }
        trips.add(trip)
    }

    override fun removeTrip(name: String): Boolean {
        return trips.removeIf { it.name  == name }
    }
}
*/
