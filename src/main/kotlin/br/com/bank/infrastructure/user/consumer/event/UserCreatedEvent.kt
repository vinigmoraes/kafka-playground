package br.com.bank.infrastructure.user.consumer.event

import br.com.bank.infrastructure.user.consumer.UserDocument
import com.fasterxml.jackson.annotation.JsonProperty

class UserCreatedEvent(
    @get:JsonProperty("fullDocument")
    val fullDocument: UserDocument
)
