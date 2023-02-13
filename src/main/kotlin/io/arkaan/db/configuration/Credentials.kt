package io.arkaan.db.configuration

import com.fasterxml.jackson.annotation.JsonProperty

data class Credentials (
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String
)