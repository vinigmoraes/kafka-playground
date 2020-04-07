package br.com.bank.application.config

import br.com.bankservice.application.config.ObjectMapperProvider
import com.typesafe.config.ConfigFactory
import org.koin.dsl.module
import javax.sql.DataSource

val configModules = module {
    single { ObjectMapperProvider.provide() }
    single { EnvironmentConfig(ConfigFactory.load()) }
    single<DataSource> {
        with(get<EnvironmentConfig>()) {
            DataSourceProvider.provide(databaseUrl, databaseUsername, databasePassword)
        }
    }
}
