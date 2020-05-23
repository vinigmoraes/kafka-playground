package br.com.bank.core

import br.com.bank.core.account.Account
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.util.UUID

class AccountTest {

    private val account = Account(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        number = "12837891"
    )

    @Test
    fun `given positive amount should increase account balance`() {
        val amount = BigDecimal(50)

        account.increaseBalance(amount)

        assertEquals(account.balance, BigDecimal(50))
    }

    @Test
    fun `given negative amount should decrease account balance`() {
        account.balance = BigDecimal(100)
        val amount = BigDecimal(-50)

        account.decreaseBalance(amount)

        assertEquals(account.balance, BigDecimal(50))
    }
}
