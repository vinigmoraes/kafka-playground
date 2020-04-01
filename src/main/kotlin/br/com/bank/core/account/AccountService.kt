package br.com.bank.core.account

import br.com.bank.core.account.ports.AccountRepository
import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import br.com.bank.core.account.ports.transfer.TransferRequest
import br.com.bank.core.user.UserService
import java.math.BigDecimal
import java.util.UUID

class AccountService(
    private val repository: AccountRepository,
    private val userService: UserService,
    private val publisher: Publisher<UUID, TransferTransaction>
) {

    fun create(userId: String): Account {
        val user = userService.findById(userId)

        user.hasAccount()

        return Account
            .create(user.id)
            .also { repository.save(it) }
    }

    fun transfer(accountId: UUID, request: TransferRequest): TransferTransaction {
        val account = findById(accountId)

        account.hasEnoughBalance(request.amount)

        val transaction = TransferTransaction.create(account, request)

        publisher.sendMessage(account.userId, transaction)

        return transaction
    }

    fun updateBalance(accountId: UUID, amount: BigDecimal) = findById(accountId).updateBalance(amount)

    private fun findById(accountId: UUID) = repository.findById(accountId) ?: throw Exception()
}
