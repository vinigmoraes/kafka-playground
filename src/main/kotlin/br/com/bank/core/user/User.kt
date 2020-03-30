package br.com.bank.core.user

import br.com.bank.core.user.ports.CreateUserRequest
import java.lang.Exception
import java.time.LocalDateTime
import java.util.UUID

class User(
    val id: UUID,
    val name: String,
    val cpf: String,
    val accountId: UUID? = null
) {

    val createdAt: LocalDateTime = LocalDateTime.now()

    companion object {
        fun create(request: CreateUserRequest) = User(
            id = UUID.randomUUID(),
            name = request.name,
            cpf = request.cpf
        )

        fun create(name: String, cpf: String) = User(
            id = UUID.randomUUID(),
            name = name,
            cpf = cpf
        )
    }

    fun hasAccount() = check(accountId == null) { throw Exception() }
}
