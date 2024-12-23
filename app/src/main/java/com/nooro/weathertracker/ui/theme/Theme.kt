package com.nooro.weathertracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun CommonMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {

    val colors = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(colors) {
        androidx.compose.material3.Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { },
            containerColor = getBackgroundColor(),
            snackbarHost = snackBarHost
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}