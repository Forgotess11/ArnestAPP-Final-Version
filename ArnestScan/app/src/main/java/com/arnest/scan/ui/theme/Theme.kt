package com.arnest.scan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SafeGreen = Color(0xFFD5EDDB)
val SafeGreenText = Color(0xFF2E7D32)
val ModerateYellow = Color(0xFFFFF3CD)
val ModerateYellowText = Color(0xFFF9A825)
val RiskyRed = Color(0xFFF8D7DA)
val RiskyRedText = Color(0xFFC62828)
val PrimaryBlue = Color(0xFF3B5EE8)
val BackgroundWhite = Color(0xFFF8F9FA)
val CardBackground = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF1A1A2E)
val TextSecondary = Color(0xFF6C757D)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    background = BackgroundWhite,
    surface = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    secondaryContainer = Color(0xFFE8EAFF),
    onSecondaryContainer = PrimaryBlue
)

@Composable
fun ArnestScanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
