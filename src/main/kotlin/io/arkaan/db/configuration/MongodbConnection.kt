package io.arkaan.db.configuration

import com.fasterxml.jackson.annotation.JsonProperty

data class MongodbConnection(
    @JsonProperty("credentials") val credentials: Credentials,
    @JsonProperty("database") val database: String
)