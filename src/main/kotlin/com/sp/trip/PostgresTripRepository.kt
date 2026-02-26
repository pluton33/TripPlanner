package com.sp.trip

import com.sp.db.StopPlaceDAO
import com.sp.db.StopPlaceTable
import com.sp.db.TripDAOToModel
import com.sp.db.TripDAO
import com.sp.db.TripTable
import com.sp.db.UserDAO
import com.sp.db.UserTable
import com.sp.db.suspendTransaction
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inSubQuery
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select

class PostgresTripRepository : TripRepository {
    override suspend fun allTrips(userId: Int): GetTripsResponse {
        return suspendTransaction {
            GetTripsResponse(
                TripDAO
                .find { TripTable.userId eq userId }
                .map { TripDAOToModel(it) })
        }
    }


    override suspend fun tripByName(userId: Int, name: String): Trip? {
        return suspendTransaction {
            TripDAO
                .find { (TripTable.name eq name and (TripTable.userId eq userId)) }
                .limit(1)
                .map(::TripDAOToModel) //to samo co .map{TripDAOToModel(it)}
                .firstOrNull()
        }
    }

    override suspend fun addTrip(userId: Int, trip: Trip) {
        return suspendTransaction {
            TripDAO.new {
                name = trip.name
                description = trip.description
                user = UserDAO[userId]
            }
        }
    }

    override suspend fun addStop(userId: Int, tripId: Int, stopPlace: StopPlace) {
        return suspendTransaction {
            val trip = TripDAO.find { (TripTable.id eq tripId) and (TripTable.userId eq userId) }.singleOrNull()
                ?: throw IllegalStateException("Trip with id $tripId not found or unauthorized")
            StopPlaceDAO.new {
                name = stopPlace.name
                description = stopPlace.description
                this.trip = trip
            }
        }
    }

    override suspend fun removeStop(userId: Int, stopId: Int) {
        return suspendTransaction {
            StopPlaceTable.deleteWhere {
                (StopPlaceTable.id eq stopId) and
                        (StopPlaceTable.tripId inSubQuery TripTable.select(TripTable.id).where { TripTable.userId eq userId })
            }
        }
    }

    override suspend fun removeTrip(userId: Int, tripId: Int): Boolean {
        return suspendTransaction {
            val rowsDeleted = TripTable.deleteWhere {
                TripTable.id eq tripId and (TripTable.userId eq userId)
            }
            rowsDeleted == 1
        }
    }

}