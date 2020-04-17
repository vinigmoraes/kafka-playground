package br.com.bank.infrastructure.user.consumer.user

import com.fasterxml.jackson.annotation.JsonProperty

class UserDocument(
    @get:JsonProperty("id")
    val id: String
)
