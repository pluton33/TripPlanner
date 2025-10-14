package com.sp

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.sp.model.Trip
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun getTripsTest() = testApplication {
        application {
            module()
        }
        val tripsJson = client.getAsJsonPath("/trips")
        val result: List<String> = tripsJson.read("$[*].name")
        assertEquals("zakopane", result[0])
    }

    @Test
    fun addTripsTest() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val trip = Trip("testowe miasto", "testowy opis")
        val response1 = client.post("/trips") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )
            setBody(trip)
        }
        assertEquals(HttpStatusCode.Created, response1.status)

        val response2 = client.get("/trips")
        assertEquals(HttpStatusCode.OK, response2.status)

        val taskNames = response2
            .body<List<Trip>>()
            .map { it.name }

        assertContains(taskNames, "testowe miasto")

    }

    @Test
    fun removeTripsTest() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val trip = Trip("testowe miasto", "testowy opis")
        val response1 = client.post("/trips") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )
            setBody(trip)
        }
        assertEquals(HttpStatusCode.Created, response1.status)

        val response2 = client.delete("/trips/${trip.name}")
        assertEquals(HttpStatusCode.NoContent, response2.status)

        val response3 = client.get("/trips")
        assertEquals(HttpStatusCode.OK, response3.status)
        val taskNames = response3
            .body<List<Trip>>()
            .map { it.name }
        assertFalse(taskNames.contains(trip.name))
    }

    suspend fun HttpClient.getAsJsonPath(url: String): DocumentContext {
        val response = this.get(url) {
            accept(ContentType.Application.Json)
        }
        return JsonPath.parse(response.bodyAsText())
    }

}
