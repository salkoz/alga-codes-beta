package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AlgaDarkColorScheme = darkColorScheme(
    primary = VividOrange,
    onPrimary = TextPrimary,
    primaryContainer = CoralRed,
    onPrimaryContainer = TextPrimary,
    secondary = ElectricAmber,
    onSecondary = DarkBackground,
    secondaryContainer = DarkMahogany,
    onSecondaryContainer = ElectricAmber,
    tertiary = CyberCyan,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextMuted,
    outline = DarkBorder,
    outlineVariant = DarkBorderHighlight
)

@Composable
fun AlgaCodesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AlgaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
