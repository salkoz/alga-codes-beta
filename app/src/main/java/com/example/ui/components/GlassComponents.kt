package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

/**
 * High-end Dark Glassmorphic card container with subtle borders and dark glass background.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderColor: Color = DarkBorder,
    borderWidth: Dp = 1.dp,
    backgroundColor: Color = CardGlassBg,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
            .padding(16.dp),
        content = content
    )
}

/**
 * Ambient Aura Backdrops: Large radial glowing light auras in vivid warm orange and coral red.
 */
@Composable
fun AmbientAuraBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aura_anim")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.40f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top-right vivid orange aura
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = 180.dp, y = (-40).dp)
                .blur(90.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            VividOrange.copy(alpha = pulseAlpha),
                            CoralRed.copy(alpha = pulseAlpha * 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Center-left dark mahogany & coral aura
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = (-120).dp, y = 260.dp)
                .blur(100.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CoralRed.copy(alpha = pulseAlpha * 0.7f),
                            DarkMahogany.copy(alpha = 0.8f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Bottom electric amber aura
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 80.dp)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            ElectricAmber.copy(alpha = pulseAlpha * 0.5f),
                            DarkMahogany,
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Main content on top
        content()
    }
}

/**
 * Phone frame indicator representing iPhone 16 Pro Dynamic Island and status header styling.
 */
@Composable
fun IPhoneHeaderBar(
    isSynced: Boolean = true,
    isSyncing: Boolean = false,
    onSyncClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(if (isSyncing) ElectricAmber else NeonGreen, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Alga.codes",
                color = TextPrimary,
                style = androidx.compose.material3.MaterialTheme.typography.titleSmall
            )
        }

        // Dynamic Island Capsule
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(26.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color.Black)
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(VividOrange, CircleShape)
                )
                Text(
                    text = if (isSyncing) "Синхронизация..." else "ЕНТ 2026",
                    color = TextMuted,
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall
                )
            }
        }

        // Cloud sync badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSyncing) ElectricAmber.copy(alpha = 0.2f) else DarkSurfaceElevated)
                .border(0.5.dp, if (isSyncing) ElectricAmber else DarkBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = if (isSyncing) "Syncing..." else "Cloud OK",
                color = if (isSyncing) ElectricAmber else NeonGreen,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall
            )
        }
    }
}
