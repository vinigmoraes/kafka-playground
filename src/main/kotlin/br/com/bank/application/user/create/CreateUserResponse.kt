package br.com.bank.application.user.create

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class CreateUserResponse(
    @get:JsonProperty("id")
    val id: UUID
) {
    companion object {
        fun create(id: UUID) = CreateUserResponse(id = id)
    }
}
