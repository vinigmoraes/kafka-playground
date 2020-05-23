package br.com.bank.infrastructure.user.consumer.user

import br.com.bank.core.user.ports.Consumer
import br.com.bank.infrastructure.user.consumer.event.UserCreatedEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class KafkaUserConsumerAdapter(
    private val bootstrapServer: String,
    private val topic: String
) : Consumer<String, UserCreatedEvent> {

    private val groupId = javaClass::getSimpleName.name
    private val scope = CoroutineScope(Dispatchers.Default)

    private val logger = LoggerFactory.getLogger(javaClass)

    private val kafkaConsumer by lazy {
        KafkaConsumer<String, UserCreatedEvent>(setProperties()).also { it.subscribe(listOf(topic)) }
    }

    override fun consumeMessage(action: (UserCreatedEvent) -> Unit) {
        logger.info("Starting listening to topics: $topic from server: $bootstrapServer")

        scope.launch {
            while (true) {
                val records = kafkaConsumer.poll(Duration.ofMillis(5000))

                records.forEach {
                    logger.info("Consuming message with key: ${it.key()} value: ${it.value()} at offset: ${it.offset()}")

                    runCatching {
                        action(it.value())
                    }.getOrElse { ex ->
                        ex.printStackTrace()
                    }
                    kafkaConsumer.commitAsync()
                }
            }
        }
    }

    private fun setProperties() = Properties()
        .apply {
            setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
            setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserMessageDeserializer::class.java.name)
            setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId)
            setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1")
            setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        }
}
