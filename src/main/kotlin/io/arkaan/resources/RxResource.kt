package io.arkaan.resources

import io.arkaan.db.repository.ReqresClient
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType

@Path("/rx")
class RxResource(
    private val reqresClient: ReqresClient
) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(@Suspended response: AsyncResponse) {
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
                println("total (rx): ${System.currentTimeMillis() - start}")
            })
    }
}