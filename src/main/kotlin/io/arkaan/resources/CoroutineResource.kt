package io.arkaan.resources

import io.arkaan.db.repository.ReqresClient
import kotlinx.coroutines.*
import javax.ws.rs.*
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType

@Path("/coroutine")
class CoroutineResource(
    private val coroutineScope: CoroutineScope,
    private val reqresClient: ReqresClient
) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(@Suspended asyncResponse: AsyncResponse) {
        // using Deferred. easier to write
        coroutineScope.launch {
            val start = System.currentTimeMillis()
            val user = async { reqresClient.getUserSync() }
            val random = async { reqresClient.getRandomSync() }
            val combine = awaitAll(user, random)
            asyncResponse.resume(combine)
            println("total (coroutine): ${System.currentTimeMillis() - start}")
        }
    }
}