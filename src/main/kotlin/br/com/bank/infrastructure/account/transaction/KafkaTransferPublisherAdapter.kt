package br.com.bank.infrastructure.account.transaction

import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import kotlinx.coroutines.flow.callbackFlow
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.slf4j.LoggerFactory
import java.util.Properties
import java.util.UUID

class KafkaTransferPublisherAdapter(
    private val bootstrapServer: String,
    private val topic: String,
    private val schemaRegistryUrl: String
) : Publisher<UUID, TransferTransaction> {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val file = ClassLoader.getSystemResource("avro/transactions.asvc").readText()
    private val transactionSchema = Schema.Parser().parse(file)

    private val producer by lazy { KafkaProducer<UUID, GenericRecord>(setProperties()) }

    override fun sendMessage(key: UUID, value: TransferTransaction) {
        val transactionAVRO = GenericRecordBuilder(transactionSchema)
            .apply {
                set("id", value.id.toString())
                set("user_id", value.userId.toString())
                set("amount", value.amount.toString())
                set("recipient_account", value.recipient.accountId.toString())
            }.build()

        val record = ProducerRecord<UUID, GenericRecord>(topic, key, transactionAVRO)

        producer.send(record) { data, ex ->
            if (ex == null) {
                logger.info("Message sent successfully to topic: ${data.topic()} partition: ${data.partition()}")
                producer.close()
            } else {
                ex.printStackTrace()
            }
        }
    }

    private fun setProperties() = Properties().apply {
        setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer::class.java.name)
        setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer::class.java.name)
        setProperty(ProducerConfig.ACKS_CONFIG, "all")
        setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
        setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
    }
}
