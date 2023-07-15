package com.example.todoapp.ui.view.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.view.compose.themes.DarkColorScheme
import com.example.todoapp.ui.view.compose.themes.LightColorScheme
import com.example.todoapp.ui.view.compose.themes.LocalCustomColors
import com.example.todoapp.ui.view.compose.themes.ProvideColorScheme
import com.example.todoapp.ui.view.compose.themes.customTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomEditTextField(
    text: String,
    onChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onChanged,
        label = {
            Text(
                text ="Ваша заметка",
                style = customTypography.body1
            )
        },
        singleLine = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = LocalCustomColors.current.labelPrimary,
            unfocusedTextColor = LocalCustomColors.current.labelSecondary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp) // Adjust the maximum height as needed
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(color = LocalCustomColors.current.backPrimary)
    )
}

@Composable
@Preview
private fun CustomEditTextFieldLightPreview(){
    ProvideColorScheme(LightColorScheme) {
        CustomEditTextField("Пример текста", {})
    }
}

@Composable
@Preview
private fun CustomEditTextFieldDarkPreview(){
    ProvideColorScheme(DarkColorScheme) {
        androidx.compose.material.MaterialTheme {
            CustomEditTextField("Пример текста", {})
        }
    }
}

