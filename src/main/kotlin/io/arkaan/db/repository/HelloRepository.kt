package io.arkaan.db.repository

import com.mongodb.client.MongoCollection
import io.arkaan.api.Wizard
import org.bson.Document
import org.bson.types.ObjectId

class HelloRepository(private val collection: MongoCollection<Document>) {

    fun getAll(): List<Document> = collection.find().toList()

    fun addOne(wizard: Wizard) {
        collection.insertOne(
            Document(mapOf("_id" to ObjectId(), "name" to wizard.name, "age" to wizard.ability))
        )
    }
}