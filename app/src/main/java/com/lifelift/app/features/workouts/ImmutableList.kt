package com.lifelift.app.features.workouts

import androidx.compose.runtime.Immutable

/**
 * A stable wrapper for lists to help Compose skip recompositions.
 * Compose marks standard Kotlin [List] as unstable because it could be a [MutableList].
 */
@Immutable
data class ImmutableList<T>(
    val items: List<T> = emptyList()
) {
    val size: Int get() = items.size
    fun isEmpty(): Boolean = items.isEmpty()
}
