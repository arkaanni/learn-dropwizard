package io.arkaan

import com.fasterxml.jackson.annotation.JsonProperty
import io.arkaan.db.configuration.MongodbConnection
import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientConfiguration

class AppConfiguration: Configuration() {
    @JsonProperty("jerseyClient") val jerseyClient = JerseyClientConfiguration()
    @JsonProperty("mongodbConnection") lateinit var mongodbConnection: MongodbConnection
}