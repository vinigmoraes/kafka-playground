package br.com.bank.application.account

import br.com.bank.core.user.ports.Consumer
import com.fasterxml.jackson.databind.ObjectMapper

class AccountListener(
    private val consumer: Consumer,
    private val mapper: ObjectMapper
) {

    fun createAccount() = consumer.consumeMessage {

    }
}

