package br.com.bank.application.account

import br.com.bank.core.account.AccountService
import br.com.bank.core.account.ports.AccountRepository
import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import br.com.bank.core.user.ports.Consumer
import br.com.bank.infrastructure.account.AccountRepositoryAdapter
import br.com.bank.infrastructure.account.transaction.KafkaTransferPublisherAdapter
import br.com.bank.infrastructure.user.consumer.user.KafkaUserConsumerAdapter
import br.com.bank.infrastructure.user.consumer.event.UserCreatedEvent
import org.koin.dsl.module
import java.util.UUID

val accountModule = module {

    single { AccountController(get(), get()) }
    single { AccountService(get(), get(), get()) }
    single { AccountListener(get(), get(), get()) }

    single<AccountRepository> { AccountRepositoryAdapter() }

    single<Consumer<String, UserCreatedEvent>> {
        KafkaUserConsumerAdapter(
            bootstrapServer = "localhost:9092",
            topic = "usersservicedb.users"
        )
    }

    single<Publisher<UUID, TransferTransaction>> {
        KafkaTransferPublisherAdapter(
            bootstrapServer = "localhost:9092",
            topic = "transfer-transactions",
            schemaRegistryUrl = "localhost:8081"
        )
    }
}
