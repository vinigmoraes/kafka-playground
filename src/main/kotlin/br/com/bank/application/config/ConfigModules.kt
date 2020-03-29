package br.com.bank.application.config

import br.com.bankservice.application.config.ObjectMapperProvider
import org.koin.dsl.module

val configModules = module {
    single { ObjectMapperProvider.provide() }
}
