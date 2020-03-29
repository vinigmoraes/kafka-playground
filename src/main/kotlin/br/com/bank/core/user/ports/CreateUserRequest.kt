package br.com.bank.core.user.ports

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateUserRequest(
    @get:JsonProperty("name")
    val name: String,
    @get:JsonProperty("cpf")
    val cpf: String
)
