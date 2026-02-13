package com.sp

import com.sp.db.StopPlaceTable
import com.sp.db.TripDAO
import com.sp.db.TripTable
import com.sp.model.data.StopPlace
import com.sp.model.data.Trip
import com.sp.utils.Constants.DB_PASSWORD
import com.sp.utils.Constants.DB_URL
import com.sp.utils.Constants.DB_USER
import io.ktor.server.application.Application
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import java.io.File


@OptIn(ExperimentalDatabaseMigrationApi::class)
fun Application.configureDatabases() {
    val database = Database.connect(
        url = DB_URL,
        user = DB_USER,
        password = DB_PASSWORD
    )

    transaction(database) {
//        MigrationUtils.generateMigrationScript(
//            TripTable, StopPlaceTable,
//            scriptDirectory = "src/main/kotlin/com/sp/db/migration",
//            scriptName = "V5",
//        )
        val flyway = Flyway.configure()
            .dataSource(DB_URL, DB_USER, DB_PASSWORD)
// .dataSource("$DB_URL;DB_CLOSE_DELAY=-1", DB_USER, DB_PASSWORD)
            .locations("filesystem:src/main/kotlin/com/sp/db/migration")
            .baselineOnMigrate(true)
            .load()
            .migrate()
// val statements = MigrationUtils.statementsRequiredForDatabaseMigration(TripTable)
// println(statements.toString())
// statements.forEach { stmt ->
// exec(stmt)
// }
    }
}