package com.sp.model

import com.sp.db.DAOToModel
import com.sp.db.TripDAO
import com.sp.db.TripTable
import com.sp.db.suspendTransaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresTripRepository : TripRepository {
    override suspend fun allTrips(): List<Trip> {
        return suspendTransaction { TripDAO.all().map( ::DAOToModel) }
    }

    override suspend fun tripByName(name: String): Trip? {
        return suspendTransaction {
            TripDAO
                .find { (TripTable.name eq name) }
                .limit(1)
                .map(::DAOToModel)
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

    override suspend fun removeTrip(name: String): Boolean {
        return suspendTransaction {
            val rowsDeleted = TripTable.deleteWhere {
                TripTable.name eq name
            }
            rowsDeleted == 1
        }
    }

}