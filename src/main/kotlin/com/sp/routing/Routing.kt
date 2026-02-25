package com.sp.routing

import com.sp.trip.Trip
import com.sp.trip.TripRepository
import com.sp.trip.StopPlace
import com.sp.user.UserRepository

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting(
    tripsRepository: TripRepository,
    userRepository: UserRepository
) {
    routing {
        staticResources("/resources", "static")
        get("/") {
            call.respondText("Hello World!")
        }
        tripRoutes(tripsRepository)

        this.userRoutes(userRepository)
    }
}
