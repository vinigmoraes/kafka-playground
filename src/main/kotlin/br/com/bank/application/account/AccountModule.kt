package br.com.bank.application.account

import br.com.bank.core.account.AccountService
import br.com.bank.core.account.ports.AccountRepository
import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import br.com.bank.core.user.ports.Consumer
import br.com.bank.infrastructure.account.AccountRepositoryAdapter
import br.com.bank.infrastructure.account.transaction.KafkaTransferPublisherAdapter
import br.com.bank.infrastructure.user.consumer.KafkaUserConsumerAdapter
import org.koin.dsl.module

val accountModule = module {

    single { AccountController(get(), get()) }
    single { AccountService(get(), get()) }
    single { AccountListener(get(), get()) }

    single<AccountRepository> { AccountRepositoryAdapter() }
    single<Consumer> {
        KafkaUserConsumerAdapter(
            bootstrapServer = "localhost:9092",
            topic = "usersservicedb.users"
        )
    }
    single<Publisher<String, TransferTransaction>> { KafkaTransferPublisherAdapter(
        bootstrapServer = "localhost:9092",
        topic = "transfer-transactions",
        schemaRegistryUrl = "localhost:8081"
    ) }
}
