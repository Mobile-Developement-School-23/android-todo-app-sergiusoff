package com.example.todoapp.ui.view.compose.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.view.compose.themes.CreateEditTheme
import com.example.todoapp.ui.view.compose.themes.LightColorScheme
import com.example.todoapp.ui.view.compose.themes.LocalCustomColors
import com.example.todoapp.ui.view.compose.themes.ProvideColorScheme
import com.example.todoapp.ui.view.compose.themes.customTypography

@Composable
fun ButtonDelete(onClick: () -> Unit) {
    val isButtonPressed = remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { isButtonPressed.value = true }
                )
            },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        elevation = (if (isButtonPressed.value) ButtonDefaults.elevation(2.dp) else ButtonDefaults.elevation(0.dp)),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить дело",
                tint = LocalCustomColors.current.colorRed
            )
            Text(
                text = "Удалить",
                style = customTypography.button,
                color = LocalCustomColors.current.colorRed,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Composable
@Preview
private fun DeleteButtonPreview(){
    ProvideColorScheme(LightColorScheme) {
        ButtonDelete(onClick = { })
    }
}