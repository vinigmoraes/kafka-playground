package br.com.bank.core.account.ports.transfer

import com.fasterxml.jackson.annotation.JsonProperty

data class RecipientRequest(
    @get:JsonProperty("full_name")
    val fullName: String,
    @get:JsonProperty("cpf")
    val cpf: String
)
