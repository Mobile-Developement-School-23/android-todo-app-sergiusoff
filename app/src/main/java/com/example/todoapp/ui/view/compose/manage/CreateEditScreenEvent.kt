package com.example.todoapp.ui.view.compose.manage

import com.example.todoapp.model.Importance
import java.util.Date

sealed interface CreateEditScreenEvent {
    data class ChangeText(val text: String) : CreateEditScreenEvent
    data class ChangeImportance(val importance: Importance) : CreateEditScreenEvent
    data class ChangeDeadline(val deadline: Date?) : CreateEditScreenEvent
    object ClearDeadline : CreateEditScreenEvent
    object CreateTodoItem : CreateEditScreenEvent
    object SaveTodoItem : CreateEditScreenEvent
    object DeleteTodoItem : CreateEditScreenEvent
}