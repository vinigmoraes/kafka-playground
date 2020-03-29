package br.com.bank.application.account.transfer

import br.com.bank.core.transaction.TransferTransaction
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class TransferResponse(
    @get:JsonProperty("id")
    val id: UUID
) {

    companion object {
        fun create(transaction: TransferTransaction) = TransferResponse(id = transaction.id)
    }
}
