package com.sp.routing

import com.sp.trip.StopPlace
import com.sp.trip.Trip
import com.sp.trip.TripRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.log
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlin.text.toIntOrNull

fun Route.tripRoutes(tripRepository: TripRepository) {
    authenticate("basic-auth") {
        route("/trips") {
            get() {
                val principal = call.principal<UserIdPrincipal>()?.name?.toIntOrNull()
                if(principal == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                val trips = tripRepository.allTrips(principal)
                call.respond(trips)
            }
            post() {
                val trip = call.receive<Trip>()


                if (trip.name.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                try {
                    tripRepository.addTrip(
                        trip
                    )
                    call.respond(HttpStatusCode.Created)
                } catch (exception: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            route("/{tripId}") {
                post("/addStop") {
                    val tripId = call.parameters["tripId"]?.toIntOrNull()
                    val stopPlace = call.receive<StopPlace>()
                    if (tripId == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    call.application.log.debug("Adding trip stop: $tripId")
                    try {
                        tripRepository.addStop(tripId, stopPlace)
                        call.respond(HttpStatusCode.Created)
                    } catch (exception: IllegalStateException) { //TODO zająć się poprawnym rzucaniem wyjątków
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
            delete("/{tripName}") {
                val name = call.parameters["tripName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (tripRepository.removeTrip(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}