package br.com.bank.infrastructure.user.consumer.transfer

import br.com.bank.application.account.transfer.event.TransferEvent
import br.com.bank.core.user.ports.Consumer
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.Duration
import java.util.Properties

class KafkaTransferConsumerAdapter(
    private val bootstrapServer: String,
    private val topic: String,
    private val schemaRegistryUrl: String,
    private val deadLetterTopic: String
) : Consumer<String, TransferEvent> {

    private val groupId = javaClass::getSimpleName.name
    private val scope = CoroutineScope(Dispatchers.Default)
    private val logger = LoggerFactory.getLogger(javaClass)

    private val kafkaConsumer by lazy {
        KafkaConsumer<String, GenericRecord>(consumerProperties()).also { it.subscribe(listOf(topic)) }
    }

    private val kafkaProducer by lazy {
        KafkaProducer<String, TransferEvent>(producerProperties())
    }

    override fun consumeMessage(action: (TransferEvent) -> Unit) {
        logger.info("Starting listening to topics: $topic from server: $bootstrapServer")

        scope.launch {
            while (true) {
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))

                records.forEach {
                    logger.info("Consuming message with key: ${it.key()} value: ${it.value()} at offset: ${it.offset()}")

                    val transferAvro = it.value()
                    val transferEvent = TransferEvent(
                        id = transferAvro["id"].toString(),
                        userId = transferAvro["user_id"].toString(),
                        amount = BigDecimal(transferAvro["amount"].toString()),
                        recipientAccount = transferAvro["recipient_account"].toString()
                    )

                    runCatching {
                        action(transferEvent)
                    }.getOrElse { ex ->
                        ex.printStackTrace()
                        val key = it.value().get("id").toString()
                        val record = ProducerRecord(deadLetterTopic, key, transferEvent)
                        kafkaProducer.send(record)
                    }
                    kafkaConsumer.commitAsync()
                }
            }
        }
    }

    private fun consumerProperties() = Properties()
        .apply {
            setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
            setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer::class.java.name)
            setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId)
            setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1")
            setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
        }

    private fun producerProperties() = Properties()
        .apply {
            setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
            setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TransferEvent::class.java.name)
            setProperty(ProducerConfig.ACKS_CONFIG, "all")
            setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
        }
}
