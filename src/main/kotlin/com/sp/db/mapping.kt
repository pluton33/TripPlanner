package com.sp.db

import com.sp.model.data.StopPlace
import com.sp.model.data.Trip
import com.sp.model.data.User
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
    val userId = reference("user_id", UserTable)
//    val stop : StopPlace = StopPlace()
//    val stopPlace =
}

object StopPlaceTable : IntIdTable() {
    val name = varchar("name", 50)
    val description = varchar("description", 50).nullable()
    val tripId = reference("trip_id", TripTable.id, ReferenceOption.CASCADE)
}

object UserTable : IntIdTable() {
    val name = varchar("name", 50)
    val login = varchar("login", 50).uniqueIndex()
    val password = varchar("password", 50)
}

class TripDAO(tripId: EntityID<Int>) : IntEntity(tripId) {
    companion object : IntEntityClass<TripDAO>(TripTable)

    var name by TripTable.name
    var description by TripTable.description
    val stopPlaces by StopPlaceDAO referrersOn StopPlaceTable.tripId
    val user by UserDAO referencedOn TripTable.userId
//    var stopPlace
}

class StopPlaceDAO(stopPlaceId: EntityID<Int>) : IntEntity(stopPlaceId) {
    companion object : IntEntityClass<StopPlaceDAO>(StopPlaceTable)

    var name by StopPlaceTable.name
    var description by StopPlaceTable.description
    var trip by TripDAO referencedOn StopPlaceTable.tripId //trip bo exposed zamienia referencje id na obiekt
}

class UserDAO(tripId: EntityID<Int>) : IntEntity(tripId) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var name by UserTable.name
    var login by UserTable.login
    var password by UserTable.password
    val trips by TripDAO referrersOn TripTable.userId

}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun TripDAOToModel(dao: TripDAO) = Trip(
    id = dao.id.value,
    name = dao.name,
    description = dao.description,
    stopPlaces = dao.stopPlaces.map { StopPlace(it.id.value, it.name, it.description) },
)

fun UserDAOToModel(user: UserDAO) = User(
    userId = user.id.value,
    username = user.name,
    login = user.login,
    password = user.password,
    trips = user.trips.map { tripDAO ->
        Trip(tripDAO.id.value, tripDAO.name, tripDAO.description, tripDAO.stopPlaces.map { stopPlaceDAO ->
            StopPlace(stopPlaceDAO.id.value, stopPlaceDAO.name, stopPlaceDAO.description)
        })
    },
)

//fun StopPlace.toModel() = StopPlace(
//    id = this.id,
//    name = this.name,
//    description = this.description,
//)