package br.com.bank.application.account

import br.com.bank.application.account.transfer.event.TransferEvent
import br.com.bank.core.account.AccountService
import br.com.bank.core.account.ports.AccountRepository
import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import br.com.bank.core.user.ports.Consumer
import br.com.bank.infrastructure.account.AccountRepositoryAdapter
import br.com.bank.infrastructure.account.transaction.KafkaTransferPublisherAdapter
import br.com.bank.infrastructure.user.consumer.event.UserCreatedEvent
import br.com.bank.infrastructure.user.consumer.transfer.KafkaTransferConsumerAdapter
import br.com.bank.infrastructure.user.consumer.user.KafkaUserConsumerAdapter
import org.koin.core.qualifier.TypeQualifier
import org.koin.dsl.module
import java.util.UUID

val accountModule = module {

    single { AccountController(get(), get()) }
    single { AccountService(get(), get(), get()) }
    single {
        AccountListener(
            get(),
            get(qualifier = TypeQualifier(KafkaUserConsumerAdapter::class)),
            get(qualifier = TypeQualifier(KafkaTransferConsumerAdapter::class))
        )
    }

    single<AccountRepository> { AccountRepositoryAdapter() }

    single<Consumer<String, UserCreatedEvent>>(qualifier = TypeQualifier(KafkaUserConsumerAdapter::class)) {
        KafkaUserConsumerAdapter(
            bootstrapServer = "localhost:9092",
            topic = "usersservicedb.users"
        )
    }

    single<Consumer<String, TransferEvent>>(qualifier = TypeQualifier(KafkaTransferConsumerAdapter::class)) {
        KafkaTransferConsumerAdapter(
            bootstrapServer = "localhost:9092",
            topic = "transfer-transactions",
            schemaRegistryUrl = "http://localhost:8081"
        )
    }

    single<Publisher<UUID, TransferTransaction>> {
        KafkaTransferPublisherAdapter(
            bootstrapServer = "localhost:9092",
            topic = "transfer-transactions",
            schemaRegistryUrl = "http://localhost:8081"
        )
    }
}
