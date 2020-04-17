package br.com.bank.core.user

import br.com.bank.core.user.ports.CreateUserRequest
import br.com.bank.core.user.ports.UserRepository
import org.slf4j.LoggerFactory
import java.util.UUID

class UserService(
    private val repository: UserRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(request: CreateUserRequest) = User
        .create(request)
        .also { repository.save(it) }

    fun addAccount(userId: String, accountId: UUID) {
        logger.info("Adding account: $accountId to user: $userId")

        val user = findById(userId)

        user.addAccount(accountId)

        repository.update(user)
    }

    fun findById(id: String) = repository.findById(id) ?: throw Exception()

    fun findByCpf(cpf: String) = repository.findByCpf(cpf) ?: throw Exception()
}
