package br.com.bank.application

import br.com.bank.application.account.AccountListener
import br.com.bank.application.account.accountModule
import br.com.bank.application.account.accounts
import br.com.bank.application.config.configModules
import br.com.bankservice.application.config.ObjectMapperProvider
import br.com.bank.application.user.userModule
import br.com.bank.application.user.users
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get

fun Application.main() {

    install(Koin) {
        modules(listOf(configModules, userModule, accountModule))
    }

    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(ObjectMapperProvider.provide()))
    }

    startCreateAccountListener(get())

    routing {
        users(get())
        accounts(get())
    }
}

fun main() {
    embeddedServer(Netty, 8080) {
        main()
    }.start(wait = true)
}

private fun startCreateAccountListener(accountListener: AccountListener) = accountListener.createAccount()
