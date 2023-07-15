package com.example.todoapp.ui.view.compose.themes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.view.compose.manage.CreateEditManageScreen
import java.util.Date
import java.util.UUID

@Stable
class ColorScheme(
    supportSeparator: Color,
    supportOverlay: Color,
    labelPrimary: Color,
    labelSecondary: Color,
    labelTertiary: Color,
    labelDisable: Color,
    colorRed: Color,
    colorGreen: Color,
    colorBlue: Color,
    colorGray: Color,
    colorGrayLight: Color,
    colorWhite: Color,
    backPrimary: Color,
    backSecondary: Color,
    backElevated: Color,
){
    var supportSeparator by mutableStateOf(supportSeparator)
        private set
    var supportOverlay by mutableStateOf(supportOverlay)
        private set
    var labelPrimary by mutableStateOf(labelPrimary)
        private set
    var labelSecondary by mutableStateOf(labelSecondary)
        private set
    var labelTertiary by mutableStateOf(labelTertiary)
        private set
    var labelDisable by mutableStateOf(labelDisable)
        private set
    var colorRed by mutableStateOf(colorRed)
        private set
    var colorGreen by mutableStateOf(colorGreen)
        private set
    var colorBlue by mutableStateOf(colorBlue)
        private set
    var colorGray by mutableStateOf(colorGray)
        private set
    var colorGrayLight by mutableStateOf(colorGrayLight)
        private set
    var colorWhite by mutableStateOf(colorWhite)
        private set
    var backPrimary by mutableStateOf(backPrimary)
        private set
    var backSecondary by mutableStateOf(backSecondary)
        private set
    var backElevated by mutableStateOf(backElevated)
        private set

    fun update(other: ColorScheme){
        supportSeparator = other.supportSeparator
        supportOverlay = other.supportOverlay
        labelPrimary = other.labelPrimary
        labelSecondary = other.labelSecondary
        labelTertiary = other.labelTertiary
        labelDisable = other.labelDisable
        colorRed = other.colorRed
        colorGreen = other.colorGreen
        colorBlue = other.colorBlue
        colorGray = other.colorGray
        colorGrayLight = other.colorGrayLight
        colorWhite = other.colorWhite
        backPrimary = other.backPrimary
        backSecondary = other.backSecondary
        backElevated = other.backElevated
    }
}

val DarkColorScheme = ColorScheme(
    supportSeparator = separator_dark,
    supportOverlay = overlay_dark,
    labelPrimary = primary_dark,
    labelSecondary = secondary_dark,
    labelTertiary = tertiary_dark,
    labelDisable = disable_dark,
    colorRed = red_dark,
    colorGreen = green_dark,
    colorBlue = blue_dark,
    colorGray = gray_dark,
    colorGrayLight = gray_light_dark,
    colorWhite = white_dark,
    backPrimary = back_primary_dark,
    backSecondary = back_secondary_dark,
    backElevated = back_elevated_dark,
)


val LightColorScheme = ColorScheme(
    supportSeparator = separator,
    supportOverlay = overlay,
    labelPrimary = primary,
    labelSecondary = secondary,
    labelTertiary = tertiary,
    labelDisable = disable,
    colorRed = red,
    colorGreen = green,
    colorBlue = blue,
    colorGray = gray,
    colorGrayLight = gray_light,
    colorWhite = white,
    backPrimary = back_primary,
    backSecondary = back_secondary,
    backElevated = back_elevated,
)

internal val LocalCustomColors = staticCompositionLocalOf<ColorScheme> { error("No colors provided") }

@Composable
fun ProvideColorScheme(
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    val palette = remember { colorScheme }
    palette.update(colorScheme)

    CompositionLocalProvider(LocalCustomColors provides palette, content = content)
}

@Composable
fun CreateEditTheme(content: @Composable () -> Unit) {
    Log.d("ColorScheme", isSystemInDarkTheme().toString())
    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    ProvideColorScheme(colorScheme = colorScheme) {
        MaterialTheme(
            typography = customTypography,
            content = content
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightThemePreview() {
    ProvideColorScheme(LightColorScheme) {
        CreateEditManageScreen(
            uiState = remember { mutableStateOf(TodoItem(UUID.randomUUID(), "ПРИМЕР ТЕКСТА",
                Importance.BASIC, null, false, Date(), Date())) },
            onEvent = {},
            close = {}
        )
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkThemePreview() {
    ProvideColorScheme(DarkColorScheme) {
        CreateEditManageScreen(
            uiState = remember { mutableStateOf(TodoItem(UUID.randomUUID(), "ПРИМЕР ТЕКСТА",
                Importance.BASIC, null, false, Date(), Date())) },
            onEvent = {},
            close = {}
        )
    }
}

@Preview
@Composable
fun AppThemePreview() {
    CreateEditTheme {
        Surface {
            Column {
                rowPreview(
                    background = PreviewColors.colors.backPrimary,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.h1,
                    text = "Primary",
                )
                rowPreview(
                    background = PreviewColors.colors.backSecondary,
                    textColor = PreviewColors.colors.labelSecondary,
                    textStyle = customTypography.h2,
                    text = "Secondary",
                )
                rowPreview(
                    background = PreviewColors.colors.backSecondary,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Background",
                )
                rowPreview(
                    background = PreviewColors.colors.colorRed,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Red",
                )
                rowPreview(
                    background = PreviewColors.colors.colorGreen,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Green",
                )
                rowPreview(
                    background = PreviewColors.colors.colorBlue,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Blue",
                )
                rowPreview(
                    background = PreviewColors.colors.colorGray,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Separator",
                )
                rowPreview(
                    background = PreviewColors.colors.colorGrayLight,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Gray",
                )
                rowPreview(
                    background = PreviewColors.colors.colorGray,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Disabled",
                )
                rowPreview(
                    background = PreviewColors.colors.labelTertiary,
                    textColor = PreviewColors.colors.labelPrimary,
                    textStyle = customTypography.body1,
                    text = "Tertiary",
                )
            }
        }
    }
}

@Composable
fun rowPreview(
    background: Color,
    textColor: Color,
    textStyle: TextStyle,
    text: String,
) {
    Box(
        modifier = Modifier
            .background(background)
            .fillMaxWidth()
            .border(0.5.dp, Color.Black)
            .height(50.dp)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}

object PreviewColors {
    val colors: ColorScheme
        @Composable
        get() = LocalCustomColors.current
}