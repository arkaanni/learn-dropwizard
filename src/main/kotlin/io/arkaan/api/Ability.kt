package io.arkaan.api

import kotlin.random.Random

object Ability {
    private val abilities = arrayOf("Dragon Slayer", "Dark Magic", "Lost Magic", "Ice Magic")
    fun random(): String {
        val i = Random.nextInt(abilities.size)
        return abilities[i]
    }
}