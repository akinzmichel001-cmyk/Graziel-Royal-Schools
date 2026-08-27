package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

enum class AvatarBadgeType {
    NONE,
    ONLINE_DOT,
    VERIFIED_BADGE,
    SCHOLAR_STAR
}

/**
 * Material 3 Avatar Component
 * Displays a student/user avatar with support for images, initials fallback, gradient rings, and status badges.
 */
@Composable
fun Avatar(
    name: String,
    modifier: Modifier = Modifier,
    imageRes: Int? = R.drawable.img_student_avatar,
    size: Dp = 56.dp,
    shape: Shape = CircleShape,
    ringColor: Brush = Brush.linearGradient(listOf(Indigo500, Amber400)),
    ringWidth: Dp = 2.dp,
    badgeType: AvatarBadgeType = AvatarBadgeType.VERIFIED_BADGE,
    onClick: (() -> Unit)? = null
) {
    val initials = rememberInitials(name)

    Box(
        modifier = modifier
            .size(size)
            .testTag("avatar_component")
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer Ring Border
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(ringColor)
                .padding(ringWidth)
                .clip(shape)
                .background(DarkCardSurface),
            contentAlignment = Alignment.Center
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "$name's avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                )
            } else {
                // Initials Fallback
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Indigo600, Slate900)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontSize = (size.value * 0.36f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Status Badge Overlay
        when (badgeType) {
            AvatarBadgeType.ONLINE_DOT -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 1.dp, y = 1.dp)
                        .size((size.value * 0.28f).coerceAtLeast(10f).dp)
                        .clip(CircleShape)
                        .background(DarkCanvas)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Emerald400)
                        .testTag("avatar_online_dot")
                )
            }
            AvatarBadgeType.VERIFIED_BADGE -> {
                Surface(
                    shape = CircleShape,
                    color = Indigo600,
                    border = BorderStroke(1.5.dp, DarkCanvas),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size((size.value * 0.36f).coerceAtLeast(16f).dp)
                        .testTag("avatar_verified_badge")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Verified Student",
                            tint = Color.White,
                            modifier = Modifier.size((size.value * 0.22f).coerceAtLeast(10f).dp)
                        )
                    }
                }
            }
            AvatarBadgeType.SCHOLAR_STAR -> {
                Surface(
                    shape = CircleShape,
                    color = Amber500,
                    border = BorderStroke(1.5.dp, DarkCanvas),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size((size.value * 0.36f).coerceAtLeast(16f).dp)
                        .testTag("avatar_scholar_badge")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Scholar",
                            tint = DarkCanvas,
                            modifier = Modifier.size((size.value * 0.22f).coerceAtLeast(10f).dp)
                        )
                    }
                }
            }
            AvatarBadgeType.NONE -> { /* No badge */ }
        }
    }
}

private fun rememberInitials(fullName: String): String {
    val parts = fullName.trim().split("\\s+".toRegex())
    return when {
        parts.size >= 2 -> "${parts[0].firstOrNull()?.uppercaseChar() ?: ""}${parts[1].firstOrNull()?.uppercaseChar() ?: ""}"
        parts.isNotEmpty() && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
        else -> "ST"
    }
}
