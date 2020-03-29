package br.com.bank.application.account

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route

fun Routing.accounts(controller: AccountController) {

    route("/accounts") {
        route("/{id}") {
            post("/transfer") { controller.transfer(call) }
        }
    }
}
