package io.arkaan.core.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import io.arkaan.api.Wizard
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer

class JsonDeserializer: Deserializer<Wizard> {
    private val mapper = ObjectMapper()
    override fun deserialize(topic: String?, data: ByteArray?): Wizard {
        println("deserializing: $data")
        try {
            return mapper.readValue(data, Wizard::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            throw SerializationException()
        }
    }
}