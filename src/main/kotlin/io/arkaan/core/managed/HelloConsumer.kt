package io.arkaan.core.managed

import com.fasterxml.jackson.databind.ObjectMapper
import io.arkaan.api.Wizard
import io.arkaan.db.repository.HelloRepository
import io.dropwizard.lifecycle.Managed
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*
import kotlin.concurrent.thread

class HelloConsumer(private val helloRepository: HelloRepository): Managed {
    private lateinit var kafkaConsumer: KafkaConsumer<String, String>
    private val objectMapper = ObjectMapper();

    override fun start() {
        super.start()
        val properties = Properties()
        properties.apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.GROUP_ID_CONFIG, "hello-group")
        }
        kafkaConsumer = KafkaConsumer(properties)
        thread(start = true) {
            consume()
        }
    }

    private fun consume() {
        kafkaConsumer.use {
            it.subscribe(listOf("hello"))
            while (true) {
                val poll = it.poll(Duration.ofMillis(1000))
                for (record in poll) {
                    processMessage(record.value())
                }
                it.commitSync()
            }
        }
    }

    private fun processMessage(value: String) {
        try {
            val wizard = objectMapper.readValue(value, Wizard::class.java)
            println(wizard)
            helloRepository.addOne(wizard)
        } catch (e: Exception) {
            e.printStackTrace()
            kafkaConsumer.commitSync()
        }
    }

    override fun stop() {
        super.stop()
        kafkaConsumer.close()
    }
}