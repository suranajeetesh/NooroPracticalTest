package com.nooro.weathertracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.nooro.weathertracker.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
private fun poppinsFontFamilyData(): FontFamily {
    val poppinsExtraBold = Font(R.font.poppins_medium, FontWeight.ExtraBold) //800
    val poppinsBold = Font(R.font.poppins_bold, FontWeight.Bold) //700
    val poppinsSemiBold = Font(R.font.poppins_bold, FontWeight.SemiBold) //600
    val poppinsMedium = Font(R.font.poppins_medium, FontWeight.Medium) //500
    val poppinsRegular = Font(R.font.poppins_regular, FontWeight.Normal) //400
    val poppinsLight = Font(R.font.poppins_light, FontWeight.Light) //300
    val poppinsExtraLight = Font(R.font.poppins_light, FontWeight.ExtraLight) //200
    val poppinsThin = Font(R.font.poppins_thin, FontWeight.Thin) //100

    return FontFamily(
        poppinsRegular,
        poppinsMedium,
        poppinsBold,
        poppinsExtraBold,
        poppinsThin,
        poppinsLight,
        poppinsExtraLight,
        poppinsSemiBold
    )
}

@Composable
fun font(fontSize: TextUnit = 16.sp, fontWeight: FontWeight = FontWeight.Normal, color: Color? = null, darkTheme: Boolean = isSystemInDarkTheme()): TextStyle {
    val poppinsFontFamily: FontFamily = poppinsFontFamilyData()
    return TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize, color = color ?: getDefaultTextColor(darkTheme)
    )
}

@Composable
fun getDarkColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (darkTheme) {
        SilverSand
    } else {
        CharlestonGreen
    }
}

@Composable
fun getTextFiledColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (darkTheme) {
        White
    } else {
        AntiFlashWhite
    }
}
@Composable
fun getDefaultTextColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (darkTheme) {
        White
    } else {
        CharlestonGreen
    }
}

@Composable
fun getBackgroundColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (darkTheme) {
        CharlestonGreen
    } else {
        White
    }
}