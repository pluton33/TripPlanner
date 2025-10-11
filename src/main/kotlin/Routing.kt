package com.sp

import com.sp.model.Trip
import com.sp.model.TripsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        staticResources("/resources", "static")
        get("/") {
            call.respondText("Hello World!")
        }
        route("/trips"){
            get() {
                val trips = TripsRepository.allTrips()
                call.respondText(trips.toString())
            }
            post() {
                val trip = call.receive<Trip>()


                if (trip.name.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                try {
                    TripsRepository.addTrip(
                        trip
                    )
                    call.respond(HttpStatusCode.NoContent)
                } catch (exception: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            delete("/{tripName}") {
                val name = call.parameters["tripName"]
                if(name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if(TripsRepository.removeTrip(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
