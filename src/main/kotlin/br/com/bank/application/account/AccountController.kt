package br.com.bank.application.account

import br.com.bank.application.account.transfer.TransferResponse
import br.com.bank.core.account.AccountService
import br.com.bank.core.account.ports.transfer.TransferRequest
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.request.receiveText
import io.ktor.response.respond
import java.util.UUID

class AccountController(
    private val service: AccountService,
    private val mapper: ObjectMapper
) {

    suspend fun transfer(call: ApplicationCall) {
        val json = call.receiveText()
        val accountId = UUID.fromString(call.parameters["id"]!!)

        val request = mapper.readValue(json, TransferRequest::class.java)

        val transaction = service.transfer(accountId, request)

        call.respond(Created, TransferResponse.create(transaction))
    }
}
