package com.sp

import com.sp.model.Trip
import com.sp.model.TripsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/trips") {
            val trips = TripsRepository.allTrips()
            call.respondText(trips.toString())
        }
        post("/trips") {
            val formContent = call.receiveParameters()

            val params = arrayOf(
                formContent["name"] ?: "",
                formContent["description"] ?: ""
            )

            if (params.any { it.isEmpty() }) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            try {
                TripsRepository.addTrip(
                    Trip(params[0], params[1])
                )
                call.respond(HttpStatusCode.NoContent)
            } catch (exception : IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
