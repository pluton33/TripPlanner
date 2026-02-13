package com.sp.db

import com.sp.model.data.StopPlace
import com.sp.model.data.Trip
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction


object TripTable : IntIdTable() {
    val name = varchar("name", 50)
    val description = varchar("description", 50)
//    val stop : StopPlace = StopPlace()
//    val stopPlace =
}

object StopPlaceTable : IntIdTable() {
    val name = varchar("name", 50)
    val description = varchar("description", 50).nullable()
    val tripId = reference("trip_id", TripTable.id, ReferenceOption.CASCADE)
}

class TripDAO(tripId: EntityID<Int>) : IntEntity(tripId) {
    companion object : IntEntityClass<TripDAO>(TripTable)

    var name by TripTable.name
    var description by TripTable.description
    val stopPlaces by StopPlaceDAO referrersOn StopPlaceTable.tripId
//    var stopPlace
}

class StopPlaceDAO(stopPlaceId: EntityID<Int>) : IntEntity(stopPlaceId) {
    companion object : IntEntityClass<StopPlaceDAO>(StopPlaceTable)
    var name by StopPlaceTable.name
    var description by StopPlaceTable.description
    var tripId by TripDAO referencedOn StopPlaceTable.tripId
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun TripDAOToModel(dao: TripDAO) = Trip (
    id = dao.id.value,
    name = dao.name,
    description = dao.description,
    stopPlaces = dao.stopPlaces.map { StopPlace(it.id.value, it.name, it.description) },
)