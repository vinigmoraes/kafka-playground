package br.com.bank.core.transaction

import br.com.bank.core.account.Account
import br.com.bank.core.account.ports.transfer.TransferRequest
import br.com.bank.core.user.User
import java.math.BigDecimal
import java.util.UUID

class TransferTransaction(
    val id: UUID,
    val userId: UUID,
    val amount: BigDecimal,
    val recipient: User
) {

    companion object {
        fun create(account: Account, amount: BigDecimal, recipient: User) = TransferTransaction(
            id = UUID.randomUUID(),
            userId = account.userId,
            amount = amount,
            recipient = recipient
        )
    }
}
