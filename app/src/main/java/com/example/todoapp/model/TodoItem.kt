package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Модель данных для элемента списка дел.
 * Этот класс является сущностью, которая может быть сохранена в базе данных.
 *
 * @property id Уникальный идентификатор элемента.
 * @property text Текстовое описание элемента.
 * @property importance Уровень важности элемента.
 * @property deadline Дата и время крайнего срока выполнения элемента (может быть null).
 * @property isDone Флаг, указывающий, выполнен ли элемент.
 * @property creationDate Дата и время создания элемента.
 * @property lastModificationDate Дата и время последнего изменения элемента.
 */
@Entity(tableName = "TodoItems")
data class TodoItem(
    @PrimaryKey
    var id: UUID,
    var text: String,
    var importance: Importance,
    var deadline: Date?,
    var isDone: Boolean,
    var creationDate: Date,
    var lastModificationDate: Date
) : Serializable