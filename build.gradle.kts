plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.sp"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
//    implementation("io.ktor:ktor-client-websockets:3.3.0")
    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
    testImplementation("io.ktor:ktor-client-content-negotiation:2.1.1")
    implementation("io.ktor:ktor-server-status-pages:3.3.0")
    implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-2")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-2")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0-rc-2")

    //migrations
    implementation("org.jetbrains.exposed:exposed-migration-core:1.0.0-rc-2")
    implementation("org.jetbrains.exposed:exposed-migration-jdbc:1.0.0-rc-2")
    implementation("org.flywaydb:flyway-core:10.12.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.12.0")

    implementation("com.h2database:h2:2.3.232")
    implementation("org.postgresql:postgresql:42.7.3")
}
