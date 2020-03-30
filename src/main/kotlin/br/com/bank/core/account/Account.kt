package br.com.bank.core.account

import java.math.BigDecimal
import java.util.UUID
import kotlin.random.Random

class Account(
    val id: UUID,
    val userId: UUID,
    val number: String
) {

    val balance: BigDecimal = BigDecimal(0)

    companion object {
        fun create(userId: UUID) = Account(
            id = UUID.randomUUID(),
            userId = userId,
            number = Random.nextInt(0, 999999).toString()
        )
    }

    fun hasEnoughBalance(amount: BigDecimal) = check(amount > balance) { throw Exception() }
}
