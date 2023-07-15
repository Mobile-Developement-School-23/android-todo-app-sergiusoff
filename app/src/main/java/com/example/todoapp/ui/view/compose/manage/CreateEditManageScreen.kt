package com.example.todoapp.ui.view.compose.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.view.compose.components.CustomDatePicker
import com.example.todoapp.ui.view.compose.components.CustomEditTextField
import com.example.todoapp.ui.view.compose.components.CustomTimePicker
import com.example.todoapp.ui.view.compose.components.CustomToolbar
import com.example.todoapp.ui.view.compose.components.DeadlineBox
import com.example.todoapp.ui.view.compose.components.ButtonDelete
import com.example.todoapp.ui.view.compose.components.ImportanceBottomSheet
import com.example.todoapp.ui.view.compose.components.ImportanceBox
import com.example.todoapp.ui.view.compose.themes.LocalCustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditManageScreen(
    uiState: State<TodoItem>,
    onEvent: (CreateEditScreenEvent) -> Unit,
    close: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val openDatePicker = remember { mutableStateOf(false) }
    val openTimePicker = remember { mutableStateOf(false) }
    val saveCreateState = remember { mutableStateOf(if (uiState.value.text == "") "Создать" else "Сохранить") }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(true)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        containerColor = LocalCustomColors.current.backSecondary,
        topBar = {
            CustomToolbar(
                onCloseClick = {
                    close()
               },
                onSaveClick = {
                    if (saveCreateState.value == "Сохранить") onEvent(CreateEditScreenEvent.SaveTodoItem)
                    else onEvent(CreateEditScreenEvent.CreateTodoItem)
                    close()
                },
                scrollBehavior = scrollBehavior,
                saveCreateState = saveCreateState.value
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
                    .background(LocalCustomColors.current.backPrimary),
            ) {
                CustomEditTextField(
                    text = uiState.value.text,
                    onChanged = { newText -> onEvent(CreateEditScreenEvent.ChangeText(newText)) })

                ImportanceBox(
                    selectedImportance = uiState.value.importance,
                    openBottomSheet = { openBottomSheet = true }
                )

                Divider()

                DeadlineBox(
                    deadline = uiState.value.deadline,
                    clearDeadline = { onEvent(CreateEditScreenEvent.ClearDeadline) },
                    showDatePicker = { openDatePicker.value = true })

                Divider()

                ButtonDelete(
                    onClick = {
                        onEvent(CreateEditScreenEvent.DeleteTodoItem)
                        close()
                    }
                )

                if (openBottomSheet) {
                    ModalBottomSheet(
                        sheetState = bottomSheetState,
                        onDismissRequest = { openBottomSheet = false },
                        containerColor = LocalCustomColors.current.backPrimary
                    ) {
                        ImportanceBottomSheet { importance ->
                            onEvent(CreateEditScreenEvent.ChangeImportance(importance))
                            if (bottomSheetState.isVisible) {
                                openBottomSheet = false
                            }
                        }
                    }
                }

                if (openDatePicker.value) {
                    CustomDatePicker(
                        openDatePicker = openDatePicker,
                        openTimePicker = openTimePicker
                    ) { time ->
                        uiState.value.deadline = time
                    }
                }

                if (openTimePicker.value) {
                    CustomTimePicker(
                        showTimePicker = openTimePicker,
                        selectedDate = uiState.value.deadline
                    ) { time ->
                        onEvent(CreateEditScreenEvent.ChangeDeadline(time))
                    }
                }
            }
        }
    )
}