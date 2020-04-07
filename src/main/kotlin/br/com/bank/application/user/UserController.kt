package br.com.bank.application.user

import br.com.bank.application.user.create.CreateUserResponse
import br.com.bank.application.user.details.UserDetailsResponse
import br.com.bank.core.user.UserService
import br.com.bank.core.user.ports.CreateUserRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receiveText
import io.ktor.response.respond
import java.util.UUID

class UserController(
    private val service: UserService,
    private val mapper: ObjectMapper
) {

    suspend fun create(call: ApplicationCall) {
        val json = call.receiveText()
        val request = mapper.readValue<CreateUserRequest>(json)

        val user = service.create(request)

        call.respond(Created, CreateUserResponse.create(user.id))
    }

    suspend fun details(call: ApplicationCall) {
        val id = call.parameters["id"]!!

        val user = service.findById(id)

        call.respond(OK, UserDetailsResponse.create(user))
    }
}
