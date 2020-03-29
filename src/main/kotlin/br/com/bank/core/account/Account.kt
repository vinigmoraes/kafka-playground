package br.com.bank.core.account

import java.math.BigDecimal
import java.util.UUID

class Account(
    val id: UUID,
    val userId: UUID,
    val number: String
) {

    val balance: BigDecimal = BigDecimal(0)

    fun hasEnoughBalance(amount: BigDecimal) = check(amount > balance)
}
