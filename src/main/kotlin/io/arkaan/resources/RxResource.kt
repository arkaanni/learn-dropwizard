package io.arkaan.resources

import io.arkaan.api.Ability
import io.arkaan.api.Wizard
import io.arkaan.db.repository.ReqresClient
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.logging.Logger
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType

/**
 * Resource class for mapping "/rx" path
 * Suspended is used to do asynchronous
 * response processing
 */
@Path("/rx")
@Produces(MediaType.APPLICATION_JSON)
class RxResource(
    private val reqresClient: ReqresClient
) {

    private val logger: Logger = Logger.getLogger("resource")

    /**
     * Zip operator example
     */
    @GET
    @Path("zip")
    fun zipExample(@Suspended response: AsyncResponse) {
        val start = System.currentTimeMillis()
        Single.zip(
            reqresClient
                .getUser()
                .subscribeOn(Schedulers.io()),
            reqresClient
                .getRandom()
                .subscribeOn(Schedulers.io())
        ) { t1, t2 -> arrayOf(t1, t2) }
            .subscribe(Consumer {
                response.resume(it)
                logger.info("total (rx): ${System.currentTimeMillis() - start} ms")
            })
    }

    /**
     * Map operator example
     */
    @GET
    @Path("map")
    fun mapExample(@Suspended asyncResponse: AsyncResponse) {
        reqresClient.getUser()
            .subscribeOn(Schedulers.io())
            .map { it.get("data") }
            .map { Wizard(it["id"].asText(), it["first_name"].textValue(), Ability.random()) }
            .subscribe(
                { wizard -> asyncResponse.resume(wizard) },
                { _ -> asyncResponse.resume("oops") }
            )
    }

    /**
     * Filter operator example
     */
    @GET
    @Path("filter")
    fun filterExample(@Suspended asyncResponse: AsyncResponse) {
        reqresClient.getRandomList()
            .subscribeOn(Schedulers.io())
            .filter {
                it["id"].intValue() % 2 == 1
            }
            .subscribe(
                { data -> println("/filter received: $data") },
                { e -> asyncResponse.resume(e)}
            )
        asyncResponse.resume("ok")
    }
}