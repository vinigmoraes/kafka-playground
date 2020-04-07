package br.com.bank.core.user

import br.com.bank.core.user.ports.CreateUserRequest
import br.com.bank.core.user.ports.UserRepository
import java.util.UUID

class UserService(
    private val repository: UserRepository
) {

    fun create(request: CreateUserRequest) = User
        .create(request)
        .also { repository.save(it) }

    fun findById(id: String) = repository.findById(id) ?: throw Exception()
}
