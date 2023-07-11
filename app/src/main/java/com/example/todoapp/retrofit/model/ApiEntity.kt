package com.example.todoapp.retrofit.model

import com.example.todoapp.model.TodoItem

/**
 * Класс данных, представляющий универсальную сущность запроса/ответа API.
 * (я слепой и сначала не заметил, потом начал запихивать в одну)
 * (в принципе, удобно (в связке с сериализаторами) и без тонны бойлерплейта)
 * (хотя, конечно, если будет расти, стоит переписать на отдельные сущности)
 *
 * @param status Статус запроса API в виде строки.
 * @param element Одиночный элемент задачи, возвращенный в ответе API.
 * @param list Список элементов задач, возвращенных в ответе API.
 * @param revision Ревизия данных, возвращенная в ответе API.
 */
data class ApiEntity(
    val status: String?,
    val element: TodoItem?,
    val list: List<TodoItem>?,
    val revision: Int?
)