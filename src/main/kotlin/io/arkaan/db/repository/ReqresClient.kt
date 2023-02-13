package io.arkaan.db.repository

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.rxjava3.core.Single
import java.util.logging.Logger
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

class ReqresClient(private val client: Client) {

    private val baseUrl: String = "https://reqres.in/api"
    private val logger: Logger = Logger.getLogger("repository")

    fun getRandomSync(): JsonNode {
        val result = client.target("${baseUrl}/random")
            .request()
            .header("accept", MediaType.APPLICATION_JSON)
            .get(JsonNode::class.java)
        logger.info("getRandom:\t ${System.currentTimeMillis()}")
        return result
    }

    fun getUserSync(): JsonNode {
        val result = client.target("${baseUrl}/users/3")
            .request()
            .header("accept", MediaType.APPLICATION_JSON)
            .get(JsonNode::class.java)
        logger.info("getUser:\t ${System.currentTimeMillis()}")
        return result
    }

    fun getRandom(): Single<JsonNode> {
        return Single.create {
            val response = getRandomSync()
            it.onSuccess(response)
        }
    }

    fun getUser(): Single<JsonNode> {
        return Single.create {
            val response = getUserSync()
            it.onSuccess(response)
        }
    }
}