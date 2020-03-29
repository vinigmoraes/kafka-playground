package br.com.bank.core.user.ports

import br.com.bank.infrastructure.user.consumer.UserMessageDeserializer
import br.com.bank.infrastructure.user.consumer.event.UserCreatedEvent

interface Consumer {

    fun consumeMessage(action: (UserCreatedEvent) -> Unit)
}
