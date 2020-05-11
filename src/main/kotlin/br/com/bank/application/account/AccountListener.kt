package br.com.bank.application.account

import br.com.bank.application.account.transfer.event.TransferEvent
import br.com.bank.core.account.AccountService
import br.com.bank.core.user.ports.Consumer
import br.com.bank.infrastructure.user.consumer.event.UserCreatedEvent
import org.slf4j.LoggerFactory
import java.util.UUID

class AccountListener(
    private val service: AccountService,
    private val userConsumer: Consumer<String, UserCreatedEvent>,
    private val transferConsumer: Consumer<String, TransferEvent>
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun createAccount() = userConsumer.consumeMessage {
        logger.info("Create account event received for user: ${it.fullDocument.id}")

        val account = service.create(it.fullDocument.id)

        logger.info("Account created successfully for user: ${it.fullDocument.id} account: ${account.id}")
    }

    fun transfer() = transferConsumer.consumeMessage {
        logger.info("Transfer transaction event received for user: ${it.userId}")

        val recipientAccountId = UUID.fromString(it.recipientAccount)

        service.receiveTransfer(recipientAccountId, it.amount)
    }
}

