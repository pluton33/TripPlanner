package com.sp.db

import com.sp.model.Trip
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object TripTable : IntIdTable(name = "trip", columnName = "trip_id") {
    val name = varchar("name", 50)
    val description = varchar("description", 50)
//    val stopPlace =
}

class TripDAO(tripId: EntityID<Int>) : IntEntity(tripId) {
    companion object : IntEntityClass<TripDAO>(TripTable)

    var name by TripTable.name
    var description by TripTable.description
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun DAOToModel(dao: TripDAO) = Trip (
    name = dao.name,
    description = dao.description
)