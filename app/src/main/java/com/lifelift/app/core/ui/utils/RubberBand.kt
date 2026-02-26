package com.lifelift.app.core.ui.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue

fun Modifier.rubberBand(): Modifier = composed {
    val animatable = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source != NestedScrollSource.Drag) return Offset.Zero
                
                // If animating back, stop and resume from current
                if (animatable.isRunning) {
                     scope.launch { animatable.stop() }
                }

                val delta = available.y
                val current = animatable.value
                
                // If stretched and moving back towards 0
                if (current > 0 && delta < 0) {
                    val consumed = if (current + delta < 0) -current else delta
                    scope.launch { animatable.snapTo(current + consumed) }
                    return Offset(0f, consumed)
                }
                
                if (current < 0 && delta > 0) {
                    val consumed = if (current + delta > 0) -current else delta
                    scope.launch { animatable.snapTo(current + consumed) }
                    return Offset(0f, consumed)
                }

                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source != NestedScrollSource.Drag) return Offset.Zero

                val delta = available.y
                if (delta == 0f) return Offset.Zero

                val current = animatable.value
                val friction = (1f - (current.absoluteValue / 1200f)).coerceAtMost(1f).coerceAtLeast(0.15f)
                
                scope.launch {
                    animatable.snapTo(current + delta * friction)
                }
                
                return Offset(0f, delta)
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                val currentOffset = animatable.value
                if (currentOffset != 0f) {
                    animatable.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    return available
                }
                return Velocity.Zero
            }
        }
    }

    this
        .nestedScroll(connection)
        .graphicsLayer {
            translationY = animatable.value
        }
}
