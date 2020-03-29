package br.com.bank.application.user

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

fun Routing.users(controller: UserController) {

    post("/users") { controller.create(call) }
    get("/users/{id}") { controller.details(call) }
}


