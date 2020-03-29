package br.com.bank.application.user

import br.com.bank.core.user.UserService
import br.com.bank.core.user.ports.UserRepository
import br.com.bank.infrastructure.user.UserRepositoryAdapter
import org.koin.dsl.module

val userModule = module {
    single { UserController(get(), get()) }
    single { UserService(get()) }
    single<UserRepository> { UserRepositoryAdapter(get()) }
}
