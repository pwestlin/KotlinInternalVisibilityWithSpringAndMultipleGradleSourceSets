package nu.westlin.gradle.springgradlemultiplesourcesets.statistics

import java.time.Instant

sealed interface Statistics {
    val instant: Instant
        get() = Instant.now()
    val text: String
}

data class NewOrder(override val text: String): Statistics

data class OrderPackaged(override val text: String): Statistics