package io.arkaan.resources

import io.arkaan.db.repository.ReqresClient
import kotlinx.coroutines.*
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(@Suspended asyncResponse: AsyncResponse) {
        // using Deferred. easier to write
        coroutineScope.launch {
            val start = System.currentTimeMillis()
            val user = async { reqresClient.getUserSync() }
            val random = async { reqresClient.getRandomSync(null) }
            val combine = awaitAll(user, random)
            asyncResponse.resume(combine)
            logger.info("total (coroutine): ${System.currentTimeMillis() - start} ms")
        }
    }
}