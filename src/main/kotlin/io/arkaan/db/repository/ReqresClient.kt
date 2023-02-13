package io.arkaan.db.repository

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.rxjava3.core.Single
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

class ReqresClient(private val client: Client) {

    private val baseUrl: String = "https://reqres.in/api"

    fun getRandomSync(): JsonNode {
        val result = client.target("${baseUrl}/random")
            .request()
            .header("accept", MediaType.APPLICATION_JSON)
            .get(JsonNode::class.java)
        println("getRandomSync:\t ${System.currentTimeMillis()}")
        return result
    }

    fun getUserSync(): JsonNode {
        val result = client.target("${baseUrl}/users/3")
            .request()
            .header("accept", MediaType.APPLICATION_JSON)
            .get(JsonNode::class.java)
        println("getUserSync:\t ${System.currentTimeMillis()}")
        return result
    }

    fun getRandom(): Single<JsonNode> {
        return Single.create {
            val response = client.target("${baseUrl}/random")
                .request()
                .header("accept", MediaType.APPLICATION_JSON)
                .get(JsonNode::class.java)
            println("getRandom:\t ${System.currentTimeMillis()}")
            it.onSuccess(response)
        }
    }

    fun getUser(): Single<JsonNode> {
        return Single.create {
            val response = client.target("${baseUrl}/users/2")
                .request()
                .header("accept", MediaType.APPLICATION_JSON)
                .get(JsonNode::class.java)
            println("getUser:\t ${System.currentTimeMillis()}")
            it.onSuccess(response)
        }
    }
}