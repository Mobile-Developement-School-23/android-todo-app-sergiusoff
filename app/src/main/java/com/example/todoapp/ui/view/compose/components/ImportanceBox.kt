package com.example.todoapp.ui.view.compose.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Importance
import com.example.todoapp.ui.view.compose.themes.DarkColorScheme
import com.example.todoapp.ui.view.compose.themes.LightColorScheme
import com.example.todoapp.ui.view.compose.themes.LocalCustomColors
import com.example.todoapp.ui.view.compose.themes.ProvideColorScheme
import com.example.todoapp.ui.view.compose.themes.customTypography
import java.util.Locale

@Composable
fun ImportanceBox(
    selectedImportance: Importance,
    openBottomSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .background(LocalCustomColors.current.backPrimary)
            .clickable { openBottomSheet() }
    ) {
        Text(
            text = "Приоритет дела",
            style = customTypography.h3,
            color = LocalCustomColors.current.labelPrimary,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Text(
            text = when (selectedImportance){
                Importance.LOW -> "Нет"
                Importance.BASIC -> "Низкий"
                Importance.IMPORTANT -> "Высокий"
            },
            color = if (selectedImportance == Importance.IMPORTANT)
                LocalCustomColors.current.colorRed
            else LocalCustomColors.current.labelSecondary
        )
    }
}

@Composable
@Preview("Light Theme", widthDp = 360, heightDp = 640)
fun ImportanceBoxLightPreview() {
    ProvideColorScheme(LightColorScheme) {
        ImportanceBox(selectedImportance = Importance.BASIC, openBottomSheet = {})
    }
}

@Composable
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
fun ImportanceBoxDarkPreview() {
    ProvideColorScheme(DarkColorScheme) {
        ImportanceBox(selectedImportance = Importance.BASIC, openBottomSheet = {})
    }
}