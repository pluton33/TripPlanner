package com.sp

import com.sp.routing.configureRouting
import com.sp.trip.PostgresTripRepository
import com.sp.user.PostgresUserRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val tripRepository = PostgresTripRepository()
    val userRepository = PostgresUserRepository()
    configureSerialization()
    configureSecurity()
    configureRouting(tripRepository, userRepository)
    configureDatabases()
}
