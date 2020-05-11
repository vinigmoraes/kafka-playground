package br.com.bank.core.account.ports

import br.com.bank.core.account.Account
import java.util.UUID

interface AccountRepository {

    fun save(account: Account)

    fun findById(accountId: UUID) : Account?

    fun updateBalance(account: Account)
}
