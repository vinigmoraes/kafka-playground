package br.com.bank.core.account

import br.com.bank.core.account.ports.AccountRepository
import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import br.com.bank.core.account.ports.transfer.TransferRequest
import br.com.bank.core.user.UserService
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.UUID

class AccountService(
    private val repository: AccountRepository,
    private val userService: UserService,
    private val publisher: Publisher<UUID, TransferTransaction>
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(userId: String): Account {
        val user = userService.findById(userId)

        val account = Account.create(user.id)

        userService.addAccount(userId, account.id)

        repository.save(account)

        return account
    }

    fun executeTransfer(accountId: UUID, request: TransferRequest): TransferTransaction {
        val account = findById(accountId)
        val recipient = userService.findByCpf(request.recipient.cpf)

        account.hasEnoughBalance(request.amount)

        val transfer = TransferTransaction.create(account, request.amount, recipient)

        publisher.sendMessage(account.userId, transfer)

        account
            .decreaseBalance(request.amount)
            .also { repository.updateBalance(account) }

        return transfer
    }

    fun receiveTransfer(accountId: UUID, amount: BigDecimal) {
        val account = findById(accountId)

        logger.info("Updating account: $accountId balance")

        account.increaseBalance(amount)

        repository.updateBalance(account)
    }

    private fun findById(accountId: UUID) = repository.findById(accountId) ?: throw Exception()
}
