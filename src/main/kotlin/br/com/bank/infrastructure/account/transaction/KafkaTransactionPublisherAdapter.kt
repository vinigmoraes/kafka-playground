package br.com.bank.infrastructure.account.transaction

import br.com.bank.core.account.ports.Publisher
import br.com.bank.core.transaction.TransferTransaction
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

class KafkaTransactionPublisherAdapter(
    private val bootstrapServer: String,
    private val topic: String,
    private val schemaRegistryUrl: String
) : Publisher<String, TransferTransaction> {

    private val file = ClassLoader.getSystemResource("avro/transactions.asvc").readText()

    private val transactionSchema = Schema.Parser().parse(file)

    private val producer by lazy { KafkaProducer<String, GenericRecord>(setProperties()) }

    override fun sendMessage(key: String, value: TransferTransaction) {
        val transactionAVRO = GenericRecordBuilder(transactionSchema)
            .apply {
                set("id", value.id.toString())
                set("user_id", value.userId)
                set("amount", value.amount.toString())
            }.build()

        val record = ProducerRecord<String, GenericRecord>(topic, key, transactionAVRO)

        producer.send(record)
    }

    private fun setProperties() = Properties().apply {
        setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer::class.java.name)
        setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
    }
}
