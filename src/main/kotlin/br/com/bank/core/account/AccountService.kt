package br.com.bank.core.account

import br.com.bank.core.account.ports.AccountRepository
import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import br.com.bank.core.account.ports.transfer.TransferRequest
import java.util.UUID

class AccountService(
    private val repository: AccountRepository,
    private val publisher: Publisher<String, TransferTransaction>
) {

    fun transfer(accountId: UUID, request: TransferRequest): TransferTransaction {
        val account = repository.findById(accountId) ?: throw Exception()

        account.hasEnoughBalance(request.amount)

        val transaction = TransferTransaction.create(account, request)

        publisher.sendMessage(account.userId.toString(), transaction)

        return transaction
    }
}
