package br.com.bank.core.account.ports.transfer

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class TransferRequest(
    @get:JsonProperty("amount")
    val amount: BigDecimal,
    @get:JsonProperty("recipient")
    val recipient: RecipientRequest
)
