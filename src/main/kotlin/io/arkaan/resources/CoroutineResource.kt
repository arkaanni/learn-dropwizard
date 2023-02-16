package io.arkaan.resources

import io.arkaan.db.repository.ReqresClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOn
import java.util.logging.Logger
import javax.ws.rs.*
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType

/**
 * Resource class for mapping "/coroutine" path
 * Suspended is used to do asynchronous
 * response processing
 */
@Path("/coroutine")
class CoroutineResource(
    private val coroutineScope: CoroutineScope,
    private val reqresClient: ReqresClient
) {

    private val logger: Logger = Logger.getLogger("resource")

    /**
     * Deferred result using coroutine async
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun deferred(@Suspended asyncResponse: AsyncResponse) {
        coroutineScope.launch {
            val start = System.currentTimeMillis()
            val user = async { reqresClient.getUserSync() }
            val random = async { reqresClient.getRandomSync(null) }
            val combine = awaitAll(user, random)
            asyncResponse.resume(combine)
            logger.info("total (coroutine): ${System.currentTimeMillis() - start} ms")
        }
    }

    /**
     * Kotlin Flow example
     */
    @GET
    @Path("/flow1")
    fun flow1(@Suspended asyncResponse: AsyncResponse) {
        coroutineScope.launch {
            reqresClient.getRandomListFlow()
                .flowOn(Dispatchers.IO)
                .collect {
                    delay(500L)
                    println(it)
                }
        }
        asyncResponse.resume("running kotlin flow")
    }
}