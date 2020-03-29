package br.com.bank.infrastructure.user.consumer

import br.com.bank.infrastructure.user.consumer.event.UserCreatedEvent
import br.com.bankservice.application.config.ObjectMapperProvider
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory

class UserMessageDeserializer : Deserializer<UserCreatedEvent> {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val mapper = ObjectMapperProvider.provide()

    override fun deserialize(topic: String, data: ByteArray) : UserCreatedEvent{
        val message = String(data, Charsets.UTF_8)

        logger.info("Message received: $message")

        val payload = mapper.readTree(message)["payload"].asText()

        logger.info("Deserializing payload: $payload")

        return mapper.readValue(payload, UserCreatedEvent::class.java)
    }
}


