package io.arkaan.db.repository

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.logging.Logger
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

/**
 * An example for interacting with external
 * resource (Reqres.in) using http client
 */
class ReqresClient(private val client: Client) {

    private val baseUrl: String = "https://reqres.in/api"
    private val logger: Logger = Logger.getLogger("repository")

    fun getRandomSync(id: Int?): JsonNode? {
        val path = id ?: ""
        val result = client.target("${baseUrl}/random/${path}")
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

    /**
     * Single emits either one value or error
     */
    fun getRandom(): Single<JsonNode> {
        return Single.create {
            val response = getRandomSync(3)
            it.onSuccess(response!!)
        }
    }

    /**
     * Observable emits stream of data (cold)
     */
    fun getRandomList(): Observable<JsonNode> {
        return Observable.create {
            val response = getRandomSync(null)?.get("data") as ArrayNode
            for (data in response) {
                it.onNext(data)
            }
            it.onComplete()
        }
    }

    fun getUser(): Single<JsonNode> {
        return Single.create {
            val response = getUserSync()
            it.onSuccess(response)
        }
    }
}