package br.com.bank.application.account

import br.com.bank.application.account.transfer.event.TransferEvent
import br.com.bank.core.account.AccountService
import br.com.bank.core.user.ports.Consumer
import br.com.bank.infrastructure.user.consumer.event.UserCreatedEvent
import org.slf4j.LoggerFactory

class AccountListener(
    private val service: AccountService,
    private val userConsumer: Consumer<UserCreatedEvent>,
    private val transferConsumer: Consumer<TransferEvent>
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun createAccount() = userConsumer.consumeMessage {
        logger.info("Create account event received for user: ${it.fullDocument.id}")

        val account = service.create(it.fullDocument.id)

        logger.info("Account created successfully for user: ${it.fullDocument.id} account: ${account.id}")
    }

    fun transfer() = transferConsumer.consumeMessage {
        logger.info("Transfer transaction event received for user: ${it.userId}")

        service.updateBalance()
    }
}

