package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber400
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurfaceElevated
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate900
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Material 3 Pull-to-Refresh Container
 * Provides an intuitive pull-down gesture to manually refresh dashboard data, student info, and assignments.
 */
@Composable
fun PullToRefreshLayout(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    refreshThreshold: Dp = 80.dp,
    feedbackMessage: String? = null,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val thresholdPx = with(density) { refreshThreshold.toPx() }

    // Distance pulled down (pixels)
    val pullDistance = remember { Animatable(0f) }
    var isPulling by remember { mutableStateOf(false) }

    // When isRefreshing changes from true to false, animate back to 0
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            pullDistance.animateTo(
                targetValue = 0f,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)
            )
            isPulling = false
        } else {
            // Keep indicator visible at threshold while refreshing
            pullDistance.animateTo(
                targetValue = thresholdPx,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)
            )
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // When dragging up while pullDistance > 0, consume scroll to retract indicator
                if (source == NestedScrollSource.UserInput && available.y < 0 && pullDistance.value > 0f) {
                    val newDistance = (pullDistance.value + available.y).coerceAtLeast(0f)
                    val consumed = pullDistance.value - newDistance
                    coroutineScope.launch {
                        pullDistance.snapTo(newDistance)
                    }
                    return Offset(0f, -consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // When dragging down past top edge
                if (source == NestedScrollSource.UserInput && available.y > 0 && !isRefreshing) {
                    isPulling = true
                    // Apply resistance damping
                    val damping = 0.45f
                    val newDistance = pullDistance.value + (available.y * damping)
                    coroutineScope.launch {
                        pullDistance.snapTo(newDistance)
                    }
                    return available
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: androidx.compose.ui.unit.Velocity): androidx.compose.ui.unit.Velocity {
                if (isPulling && !isRefreshing) {
                    if (pullDistance.value >= thresholdPx) {
                        onRefresh()
                    } else {
                        pullDistance.animateTo(0f, spring(dampingRatio = 0.8f))
                        isPulling = false
                    }
                }
                return androidx.compose.ui.unit.Velocity.Zero
            }
        }
    }

    val progress = (pullDistance.value / thresholdPx).coerceIn(0f, 1.8f)

    // Infinite rotation for spinning icon during active refresh
    val infiniteTransition = rememberInfiniteTransition(label = "refresh_spin")
    val spinRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .testTag("pull_to_refresh_container")
    ) {
        // Main Content translated downward as user pulls
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = if (isRefreshing) {
                        thresholdPx * 0.7f
                    } else {
                        pullDistance.value * 0.65f
                    }
                }
        ) {
            content()
        }

        // Pull to Refresh Animated Indicator Banner
        if (pullDistance.value > 8f || isRefreshing || feedbackMessage != null) {
            val indicatorOffset = if (isRefreshing) {
                16.dp
            } else {
                with(density) { (pullDistance.value * 0.45f).coerceIn(0f, thresholdPx * 0.75f).toDp() }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = indicatorOffset)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.TopCenter)
                    .testTag("pull_to_refresh_indicator"),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = DarkCardSurfaceElevated,
                    border = BorderStroke(
                        1.dp,
                        if (isRefreshing) Indigo500.copy(alpha = 0.6f) else DarkBorder
                    ),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isRefreshing) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Refreshing",
                                tint = Indigo400,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(spinRotation)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Updating student info & assignments...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate100
                            )
                        } else if (feedbackMessage != null && feedbackMessage.contains("up to date")) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = Emerald400,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feedbackMessage,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald400
                            )
                        } else {
                            val isReadyToRelease = pullDistance.value >= thresholdPx
                            Icon(
                                imageVector = if (isReadyToRelease) Icons.Default.Refresh else Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = if (isReadyToRelease) Amber400 else Slate400,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(if (isReadyToRelease) 180f else progress * 180f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isReadyToRelease) "Release to refresh..." else "Pull down to update student info",
                                fontSize = 12.sp,
                                fontWeight = if (isReadyToRelease) FontWeight.Bold else FontWeight.Medium,
                                color = if (isReadyToRelease) Amber400 else Slate300
                            )
                        }
                    }
                }
            }
        }
    }
}
