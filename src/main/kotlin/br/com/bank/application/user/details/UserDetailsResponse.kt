package br.com.bank.application.user.details

import br.com.bank.core.user.User
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID

class UserDetailsResponse(
    @get:JsonProperty("id")
    val id: UUID,
    @get:JsonProperty("name")
    val name: String,
    @get:JsonProperty("cpf")
    val cpf: String,
    @get:JsonProperty("created_at")
    val createdAt: LocalDateTime
) {

    companion object {
        fun create(user: User) = UserDetailsResponse(
            id = user.id,
            name = user.name,
            cpf = user.cpf,
            createdAt = user.createdAt
        )
    }
}
