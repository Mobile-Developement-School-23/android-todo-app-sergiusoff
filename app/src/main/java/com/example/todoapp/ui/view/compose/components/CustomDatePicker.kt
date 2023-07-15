package com.example.todoapp.ui.view.compose.components

import android.annotation.SuppressLint
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.view.compose.themes.LightColorScheme
import com.example.todoapp.ui.view.compose.themes.ProvideColorScheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    openDatePicker: MutableState<Boolean>,
    openTimePicker: MutableState<Boolean>,
    onTimeSelected: (Date) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = {
            openDatePicker.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(Date(datePickerState.selectedDateMillis?:Date().time))
                    openDatePicker.value = false
                    openTimePicker.value = true
                },
            ) {
                Text("Далее")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDatePicker.value = false
                }
            ) {
                Text("Отменить")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
private fun CustomDatePickerPreview(){
    ProvideColorScheme(LightColorScheme) {
        CustomDatePicker(mutableStateOf(false), mutableStateOf(false), {})
    }
}