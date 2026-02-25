package com.sp.routing

import com.sp.db.UserDAO
import com.sp.db.UserDAOToModel
import com.sp.db.UserTable
import com.sp.db.suspendTransaction
import com.sp.user.User
import com.sp.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.log
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Route.userRoutes(userRepository: UserRepository) {
    route("/user") {
        post ("/register") {
            val user = call.receive<User>()
            if (user.login.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest) //TODO sprawdzić czy użytkownik ma unikalny login
                return@post
            }
            userRepository.register(user)
            return@post call.respond(HttpStatusCode.Created) //TODO owinąć w try catch
        }
        authenticate ("basic-auth") {
            delete ("/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                val principal = call.principal<UserIdPrincipal>()!!
                val loggedUser = suspendTransaction{ UserDAO.find { UserTable.id eq principal.name.toInt() }.singleOrNull()?.let { UserDAOToModel(it) } }
                if (userId == null) {
                    return@delete call.respond(HttpStatusCode.BadRequest)
                }
                if (userId != loggedUser?.userId) {
                    return@delete call.respond(HttpStatusCode.Forbidden)
                }
                userRepository.deleteById(userId)
                return@delete call.respond(HttpStatusCode.OK)
            }
            get ("/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                val principal = call.principal<UserIdPrincipal>()!!
                println("principal.name ${principal.name}")
                val loggedUser = suspendTransaction{ UserDAO.find { UserTable.id eq principal.name.toInt() }.singleOrNull()?.let { UserDAOToModel(it) } }
                if (userId == null) {
                    return@get call.respond(HttpStatusCode.BadRequest)
                }
                if (userId != loggedUser?.userId) {
                    println("userId: ${userId}, loggedUser: ${loggedUser?.userId}")
                    return@get call.respond(HttpStatusCode.Forbidden)
                }
                val user = userRepository.findById(userId)
                if (user == null) {
                    return@get call.respond(HttpStatusCode.NotFound)
                }
                call.respond(user)
            }
        }
    }
}