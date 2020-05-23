package br.com.bank.infrastructure.account

import br.com.bank.core.account.Account
import br.com.bank.core.account.ports.AccountRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.util.UUID

object AccountTable : Table("accounts") {

    private const val VARCHAR_MAX_LENGTH = 50

    val id = uuid("id").primaryKey()
    val userId = uuid("user_id")
    val accountNumber = varchar("number", VARCHAR_MAX_LENGTH)
    val balance = decimal("balance", 0, 2)

    fun toAccount(resultRow: ResultRow) = Account(
        id = resultRow[id],
        userId = resultRow[userId],
        number = resultRow[accountNumber]
    ).apply {
        balance = resultRow[AccountTable.balance]
    }
}

class AccountRepositoryAdapter : AccountRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun save(account: Account) {
        logger.info("Persisting account: ${account.id}")
        transaction {
            AccountTable.insert {
                it[id] = account.id
                it[accountNumber] = account.number
                it[userId] = account.userId
                it[balance] = account.balance
            }
        }
    }

    override fun findById(accountId: UUID): Account? {

        logger.info("Searching for account: $accountId")

        return transaction {
            AccountTable
                .select { AccountTable.id eq accountId }
                .firstOrNull()
                ?.let { AccountTable.toAccount(it) }
        }
    }

    override fun updateBalance(account: Account) {
        logger.info("Updating account: ${account.id}")

        transaction {
            AccountTable.update({AccountTable.id eq account.id}) {
                it[balance] = account.balance
            }
        }
    }
}
