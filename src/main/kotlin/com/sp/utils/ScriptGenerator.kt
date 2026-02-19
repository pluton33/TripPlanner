package com.sp.utils

import com.sp.db.StopPlaceTable
import com.sp.db.TripTable
import com.sp.db.UserTable
import com.sp.utils.Constants.DB_PASSWORD
import com.sp.utils.Constants.DB_URL
import com.sp.utils.Constants.DB_USER
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

fun main() {
    Database.connect(DB_URL, driver = "org.postgresql.Driver", user = DB_USER, password = DB_PASSWORD)

    transaction {

        val statements = MigrationUtils.statementsRequiredForDatabaseMigration(TripTable, StopPlaceTable, UserTable)

        if (statements.isEmpty()) {
            println("Brak zmian w strukturze tabel.")
            return@transaction
        }

        val time = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"))
        val fileName = "V${time}__Auto_migration.sql"

        // 4. Zapis do pliku
        val file = java.io.File("src/main/kotlin/com/sp/db/migration/$fileName")
        file.parentFile.mkdirs() // tworzy folder jeśli nie istnieje

        // Exposed nie dodaje średników, więc je doklejamy
        file.writeText(statements.joinToString(";\n") + ";")

        println("Stworzono plik: ${file.absolutePath}")
    }
}