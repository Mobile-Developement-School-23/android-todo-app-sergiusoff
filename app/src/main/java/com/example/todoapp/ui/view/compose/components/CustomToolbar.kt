package com.example.todoapp.ui.view.compose.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.view.compose.themes.CreateEditTheme
import com.example.todoapp.ui.view.compose.themes.DarkColorScheme
import com.example.todoapp.ui.view.compose.themes.LocalCustomColors
import com.example.todoapp.ui.view.compose.themes.ProvideColorScheme
import com.example.todoapp.ui.view.compose.themes.customTypography
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomToolbar(
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    saveCreateState : String,
) {

    TopAppBar(
        title = {},
        navigationIcon =
        {
            IconButton(onClick = { onCloseClick() }){
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = LocalCustomColors.current.labelPrimary
                )
            }
        },
        actions =
        {
            TextButton(onClick = { onSaveClick() }) {
                Text(
                    text = (saveCreateState).uppercase(Locale.ROOT),
                    style = customTypography.button,
                    color = LocalCustomColors.current.labelPrimary
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalCustomColors.current.backPrimary,

        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("CustomToolbarDarkPreview")
@Composable
fun CustomToolbarDarkPreview() {
    ProvideColorScheme(DarkColorScheme)  {
        MaterialTheme {
            CustomToolbar(
                onCloseClick = {},
                onSaveClick = {},
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                saveCreateState = "Схоронить"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("CustomToolbarLightPreview")
@Composable
fun CustomToolbarLightPreview() {
    CreateEditTheme {
        CustomToolbar(
            onCloseClick = {},
            onSaveClick = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            saveCreateState = "Схоронить"
        )
    }
}