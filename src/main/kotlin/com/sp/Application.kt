package com.sp

import com.sp.trip.PostgresTripRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val tripRepository = PostgresTripRepository()
    configureSerialization()
    configureSecurity()
    configureRouting(tripRepository)
    configureDatabases()
}
