package io.arkaan.api

import com.fasterxml.jackson.annotation.JsonProperty

data class Wizard(
    @JsonProperty("id") var id: String?,
    @JsonProperty("name") val name: String,
    @JsonProperty("ability") val ability: String
)
