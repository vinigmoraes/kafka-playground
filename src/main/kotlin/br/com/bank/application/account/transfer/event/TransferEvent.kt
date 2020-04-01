package br.com.bank.application.account.transfer.event

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class TransferEvent(
    @get:JsonProperty("id")
    val id: String,
    @get:JsonProperty("user_id")
    val userId: String,
    @get:JsonProperty("amount")
    val amount: BigDecimal,
    @get:JsonProperty("recipient_account")
    val recipientAccount: String
)
