package com.example.todoapp.ui.view.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Importance
import com.example.todoapp.ui.view.compose.themes.DarkColorScheme
import com.example.todoapp.ui.view.compose.themes.LightColorScheme
import com.example.todoapp.ui.view.compose.themes.LocalCustomColors
import com.example.todoapp.ui.view.compose.themes.ProvideColorScheme
import java.util.Locale


@Composable
fun ImportanceBottomSheet(
    onImportanceSelected: (Importance) -> Unit
) {
    val importanceValues = Importance.values()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColors.current.backPrimary)
            .padding(16.dp, bottom = 30.dp)
    ) {
        Column{
            importanceValues.forEach { importance ->
                Text(
                    text = when (importance) {
                        Importance.LOW -> "Нет"
                        Importance.BASIC -> "Низкий"
                        Importance.IMPORTANT -> "Высокий"
                    },
                    color = if (importance == Importance.IMPORTANT)
                        LocalCustomColors.current.colorRed
                    else LocalCustomColors.current.labelSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onImportanceSelected(importance) }
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
@Preview("ImportanceBottomSheetLightPreview", widthDp = 360, heightDp = 640)
fun ImportanceBottomSheetLightPreview() {
    ProvideColorScheme(LightColorScheme) {
        ImportanceBottomSheet(onImportanceSelected = {})
    }
}

@Composable
@Preview("ImportanceBottomSheetDarkPreview", widthDp = 360, heightDp = 640)
fun ImportanceBottomSheetDarkPreview() {
    ProvideColorScheme(DarkColorScheme) {
        ImportanceBottomSheet(onImportanceSelected = {})
    }
}