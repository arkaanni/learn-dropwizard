package io.arkaan

import io.arkaan.db.repository.ReqresClient
import io.arkaan.resources.CoroutineResource
import io.arkaan.resources.RxResource
import io.dropwizard.Application
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.setup.Environment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class App: Application<AppConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            App().run(*args)
        }
    }

    override fun run(configuration: AppConfiguration, environment: Environment) {
        val jerseyClient = JerseyClientBuilder(environment).using(configuration.jerseyClient).build(name)
        val coroutine = CoroutineScope(Dispatchers.IO)
        val reqresClient = ReqresClient(jerseyClient)
        val coroutineResource = CoroutineResource(coroutine, reqresClient)
        val rxResource = RxResource(reqresClient)
        JerseyClientBuilder(environment).apply {
            using(configuration.jerseyClient)
        }
        environment.jersey().apply {
            register(rxResource)
            register((coroutineResource))
        }
    }
}