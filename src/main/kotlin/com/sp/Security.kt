package com.sp

import com.sp.db.UserDAO
import com.sp.db.UserTable
import com.sp.db.suspendTransaction
import com.sp.user.User
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.eq

fun Application.configureSecurity() {
    install(Authentication) {
        basic ("basic-auth") {
            realm = "authentication of all things test whatever description"
            validate { credentials ->
                suspendTransaction {
                    val user = UserDAO.find { UserTable.login eq credentials.name }.singleOrNull()
                    println("user.name: ${ user?.login }, credentials.name: ${ credentials.name }\n" +
                            " credentials.password: ${ credentials.password }")
                    if (credentials.name == user?.login && credentials.password == user.password) {
                        UserIdPrincipal(user.id.value.toString())
                    } else {
                        null
                    }
                }
            }
        }
    }


//    install(Sessions) {
//        cookie<MySession>("MY_SESSION") {
//            cookie.extensions["SameSite"] = "lax"
//        }
//    }
}
//@Serializable
//data class MySession(val count: Int = 0)
