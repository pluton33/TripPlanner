package com.sp.model

import com.sp.db.StopPlaceDAO
import com.sp.db.TripDAOToModel
import com.sp.db.TripDAO
import com.sp.db.TripTable
import com.sp.db.suspendTransaction
import com.sp.model.data.StopPlace
import com.sp.model.data.Trip
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere

class PostgresTripRepository : TripRepository {
    override suspend fun allTrips(userId: Int): GetTripsResponse {
        return suspendTransaction { GetTripsResponse(TripDAO
            .find { TripTable.userId eq userId }
            .map { TripDAOToModel(it) }) }
    }


    override suspend fun tripByName(name: String): Trip? {
        return suspendTransaction {
            TripDAO
                .find { (TripTable.name eq name) }
                .limit(1)
                .map(::TripDAOToModel) //to samo co .map{TripDAOToModel(it)}
                .firstOrNull()
        }
    }

    override suspend fun addTrip(trip: Trip) {
        return suspendTransaction {
            TripDAO.new {
                name = trip.name
                description = trip.description
            }
        }
    }

    override suspend fun addStop(tripId: Int, stopPlace: StopPlace) {
        return suspendTransaction {
            val trip = TripDAO[tripId]
            StopPlaceDAO.new {
                name = stopPlace.name
                description = stopPlace.description
                this.trip = trip
            }
        }
    }

    override suspend fun removeTrip(name: String): Boolean {
        return suspendTransaction {
            val rowsDeleted = TripTable.deleteWhere {
                TripTable.name eq name
            }
            rowsDeleted == 1
        }
    }

}