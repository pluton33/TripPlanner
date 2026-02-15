package com.sp

import com.sp.model.data.Trip
import com.sp.model.TripRepository
import com.sp.model.data.StopPlace

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting(tripsRepository: TripRepository) {
    routing {
        staticResources("/resources", "static")
        get("/") {
            call.respondText("Hello World!")
        }
        route("/trips"){
            get() {
                val trips = tripsRepository.allTrips()
                call.respond(trips)
            }
            post() {
                val trip = call.receive<Trip>()


                if (trip.name.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                try {
                    tripsRepository.addTrip(
                        trip
                    )
                    call.respond(HttpStatusCode.Created)
                } catch (exception: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            route("/{tripId}"){
                post ("/addStop") {
                    val tripId = call.parameters["tripId"]?.toIntOrNull()
                    val stopPlace = call.receive<StopPlace>()
                    if (tripId == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    log.debug("Adding trip stop: $tripId")
                    try {
                        tripsRepository.addStop(tripId, stopPlace)
                        call.respond(HttpStatusCode.Created)
                    } catch (exception: IllegalStateException) { //TODO zająć się poprawnym rzucaniem wyjątków
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
            delete("/{tripName}") {
                val name = call.parameters["tripName"]
                if(name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if(tripsRepository.removeTrip(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
