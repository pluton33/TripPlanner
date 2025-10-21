package com.sp

import com.sp.db.TripDAO
import com.sp.db.TripTable
import com.sp.model.Trip
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File


fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/tripplaner_db",
        user = "postgres",
        password = "password"
    )

    val missingColStatement
}