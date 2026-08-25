package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.Amber400
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate900

@Composable
fun SchoolLogoBadge(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shapeRadius: Dp = 12.dp,
    borderAlpha: Float = 0.4f
) {
    Surface(
        shape = RoundedCornerShape(shapeRadius),
        color = Color.Transparent,
        border = BorderStroke(1.5.dp, Amber400.copy(alpha = borderAlpha)),
        modifier = modifier.size(size)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Indigo600,
                            Slate900
                        )
                    ),
                    shape = RoundedCornerShape(shapeRadius)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_school_logo_vector),
                contentDescription = "Graziel Royal Schools Emblem",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(shapeRadius))
            )
        }
    }
}
